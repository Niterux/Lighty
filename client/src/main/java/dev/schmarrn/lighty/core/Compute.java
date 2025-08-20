// Copyright 2022-2023 The Lighty contributors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dev.schmarrn.lighty.core;

import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.schmarrn.lighty.api.OverlayData;
import dev.schmarrn.lighty.api.OverlayDataProvider;
import dev.schmarrn.lighty.api.OverlayRenderer;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import dev.schmarrn.lighty.mixin.accessors.WorldRendererCompiledChunksAccessor;
import dev.schmarrn.lighty.overlaystate.SMACH;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.world.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Compute {
    /// Private copy of the player's currently occupied section.
    /// Used to clean up cachedBuffers that are out of range.
    /// Used to prioritize closer chunks when computing the overlay.
    /// Updated each tick.
    private static SubChunkPositionHelper playerPos = new SubChunkPositionHelper(0, 0, 0);

    /// Cache of all computed GpuBuffers and so on.
    /// Gets used in LightyRenderer.
    static final Map<SubChunkPositionHelper, DrawListHolder> cachedBuffers = new HashMap<>();

    /// TreeSet used to create a priority hierarchy, while still
    /// avoiding duplicate entries.
    private static final TreeSet<SubChunkPositionHelper> toBeUpdated = new TreeSet<>(
            Comparator.comparingDouble(self -> {
                // As a distance measure, the taxicab distance is used,
                // additionally with a tie_breaker term based on the long-representation
                // of the section pos in question
                int taxicabDistance = self.getTaxicabDistance(playerPos);
                double tieBreaker = (double)self.packIntoLong() / (double)Long.MIN_VALUE;
                return taxicabDistance + tieBreaker;
            })
    );

    static int computationDistance;
    static {
        setComputationDistance();
    }

    private static boolean outOfRange(SubChunkPositionHelper subChunk) {
        // squared X and Z
        int absX = Math.abs(subChunk.x - playerPos.x);
        int absZ =  Math.abs(subChunk.z - playerPos.z);

        return absX > computationDistance || absZ > computationDistance;
    }

    public static void clear() {
        toBeUpdated.clear();
        cachedBuffers.forEach((subChunk, vertexBuffer) -> {
            // Important to avoid a Memory leak!
            vertexBuffer.close();
        });
        cachedBuffers.clear();
        setComputationDistance();
    }

    public static void updateBlockPos(BlockPos pos) {
        SubChunkPositionHelper subChunk = SubChunkPositionHelper.fromBlockPos(pos);
        if (subChunk.getBlockPos().y == pos.y) {
            // if we are on the y-border of a SubChunk, we need to update *both* SubChunks
            // see https://github.com/SchmarrnDevs/Lighty/issues/70
            updateSection(new SubChunkPositionHelper(subChunk.x, subChunk.y - 1, subChunk.x));
        }
        updateSection(subChunk);
    }

    public static void updateSection(SubChunkPositionHelper subChunk) {
        if (outOfRange(subChunk)) {
            return;
        }

        toBeUpdated.add(subChunk);
    }

    private static DrawListHolder buildChunk(OverlayRenderer renderer, List<OverlayDataProvider> dataProviders, SubChunkPositionHelper subChunk, World world, DrawListHolder buffer) {
        Map<String, List<OverlayData>> overlayData = new HashMap<>();

        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 16; ++y) {
                for (int z = 0; z < 16; ++z) {
                    BlockPos subChunkBlockPos = subChunk.getBlockPos();
                    BlockPos pos = new BlockPos(subChunkBlockPos.x + x, subChunkBlockPos.y + y, subChunkBlockPos.z + z);

                    for (var dataProvider : dataProviders) {
                        var data = dataProvider.compute(world, pos, new Vec3i(x, y, z));
                        overlayData.putIfAbsent(dataProvider.getResourceLocation().toString(), new ArrayList<>());
                        if (data.valid()) {
                            overlayData.get(dataProvider.getResourceLocation().toString()).add(data);
                        }
                    }
                }
            }
        }

        int overlayBrightness = Config.OVERLAY_BRIGHTNESS.getValue();
        int lightLuminescence = (int) (world.dimension.brightnessTable[overlayBrightness] * 255);
        int lightmap = 0xFF000000 | lightLuminescence << 16 | lightLuminescence << 8 | lightLuminescence;
        overlayData.forEach((key, dataList) -> {
            int list = MemoryTracker.getLists(1);
            GL11.glNewList(list, GL11.GL_COMPILE);
            BufferBuilder.INSTANCE.start(renderer.getDrawMode());
            boolean builtSomething = false;
            for (var data : dataList) {
                renderer.build(world, data.pos(), data, BufferBuilder.INSTANCE, lightmap);
                builtSomething = true;
            }
            BufferBuilder.INSTANCE.end();
            GL11.glEndList();
            // builder.build() can return null if there wasn't any data added
            // in that case, the buffer automatically gets set as invalid
            buffer.upload(list, key, builtSomething);
        });

        return buffer;
    }

    private static void queueNewChunksSlow(Minecraft minecraft) {
        for (int xx = -Compute.computationDistance + 1; xx < Compute.computationDistance; ++xx) {
            for (int zz = -Compute.computationDistance + 1; zz < Compute.computationDistance; ++zz) {
                ChunkPos chunkPos = new ChunkPos(playerPos.x + xx, playerPos.z + zz);
                for (int ii = 0; ii < 8; ++ii) {
                    SubChunkPositionHelper chunkSection = new SubChunkPositionHelper(chunkPos.x, ii, chunkPos.z);
                    if (!cachedBuffers.containsKey(chunkSection) && isSubchunkRendering(minecraft, chunkSection)) {
                        toBeUpdated.add(chunkSection);
                    }
                }
            }
        }
    }

/*    private static void queueNewChunksIncompatibleWithSodium(Minecraft minecraft) {
        for (var section : minecraft.levelRenderer.getVisibleSections()) {
            SectionPos sectionPos = SectionPos.of(section.getRenderOrigin());
            if (!cachedBuffers.containsKey(sectionPos)) {
                toBeUpdated.add(sectionPos);
            }
        }
    }*/

    public static void computeCache(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.camera == null || minecraft.world == null) {
            return;
        }

        // update state machine state that's based on items etc
        SMACH.updateCompute(minecraft);

        // update player position
        playerPos = SubChunkPositionHelper.fromBlockVec3(new Vec3i((int) minecraft.camera.x, (int) minecraft.camera.y, (int) minecraft.camera.z));

        if (!SMACH.isEnabled()) {
            return;
        }

        queueNewChunksSlow(minecraft);

        // Get the currently active data providers and renderer
        List<OverlayDataProvider> dataProviders = DataProviderRegistry.getActiveProviders();
        OverlayRenderer renderer = RendererRegistry.getRenderer();

        // Remove any buffer that's outside the overlay distance
        for (var it = cachedBuffers.entrySet().iterator(); it.hasNext();) {
            var entry = it.next();
            if (outOfRange(entry.getKey())) {
                entry.getValue().close();
                it.remove();
            }
        }

        // Compute at maximum as many chunks as specified
        for (int ii = 8 * Config.CHUNKS_PER_TICK.getValue(); ii > 0;) {
            // get the next section pos
            SubChunkPositionHelper subChunkPos = toBeUpdated.pollFirst();
            if (subChunkPos == null) {
                // There is no more work to do, go home early! Feierabend :P
                break;
            }

            // as long as the section is in range...
            if (!outOfRange(subChunkPos)) {
                // ... and the section is already compiled...
                //if (!minecraft.levelRenderer.isSectionCompiled(subChunkPos.origin())) {
                //    // chunk data isn't ready yet, keep in queue
                //    keepInUpdate.add(subChunkPos);
                //    continue;
                //}
                // ... we compute the new buffers, reduce the counter!
                --ii;
                cachedBuffers.compute(
                        subChunkPos,
                        (pos, drawListHolder) -> buildChunk(
                                renderer,
                                dataProviders,
                                pos,
                                minecraft.world,
                                drawListHolder != null ? drawListHolder : new DrawListHolder()
                        )
                );
            }
        }
    }


    private Compute() {}

    private static void setComputationDistance(){
        computationDistance = Math.min(Config.OVERLAY_DISTANCE.getValue(), 16 >> MinecraftInstanceAccessor.getMinecraft().options.viewDistance);
    }

    private static boolean isSubchunkRendering(Minecraft minecraft, SubChunkPositionHelper pos) {
        RenderChunk[] renderChunks = ((WorldRendererCompiledChunksAccessor)minecraft.worldRenderer).getCompiledChunks();
        BlockPos originPosition = pos.getBlockPos();
        for(RenderChunk renderChunk : renderChunks)
            if(renderChunk.originX == originPosition.x && renderChunk.originY == originPosition.y && renderChunk.originZ == originPosition.z)
                return true;
        return false;
    }
}
