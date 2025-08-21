package dev.schmarrn.lighty.core;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.schmarrn.lighty.api.OverlayRenderer;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import dev.schmarrn.lighty.overlaystate.SMACH;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.renderer.DynamicUniforms;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class LightyRenderer {
    private static final Vector4f UNIT_COLOR_MODULATOR = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
    private static final Matrix4f DEFAULT_TEXTURE_MATRIX = new Matrix4f();

    public static void render(Frustum frustum) {
        if (!SMACH.isEnabled())
            return;

        // Get required data
        Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
        VertexFormat.IndexType baseIndexType = data.maxIndicesRequired == 0 ? null : asib.type();

        renderEachSection(minecraft, camera, camPos, frustum, drawList, transforms);
    }

    private static Data prepareData(Minecraft minecraft, Frustum frustum) {
        // Get some basic stuff
        LivingEntity camera = minecraft.camera;

        // Create required rendering lists
        List<RenderPass.Draw<GpuBufferSlice[]>> drawList = new ArrayList<>();
        List<DynamicUniforms.Transform> transforms = new ArrayList<>();

        // tracking the biggest *vertex* buffer, in case our data didn't return an *index* buffer as well
        // See LevelRenderer#renderSectionLayer (1.21.5) for the place of inspiration
        int biggestBufferSize = renderEachSection(minecraft, camera, frustum, drawList, transforms);

        GpuBufferSlice[] dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransforms(transforms.toArray(new DynamicUniforms.Transform[0]));
        return new Data(drawList, biggestBufferSize, dynamicTransforms);
    }

/*    private static int goThroughEachBuffer(Minecraft minecraft, Camera camera, Vec3 camPos, Frustum frustum, List<RenderPass.Draw<GpuBufferSlice[]>> drawList, List<DynamicUniforms.Transform> transforms) {
        int biggestBufferSize = 0;
        for (var entry : Compute.cachedBuffers.entrySet()) {
            SectionPos chunkSection = entry.getKey();
            DrawListHolder cachedBuffer = entry.getValue();

            for (var bufferEntry : cachedBuffer.getLists().entrySet()) {
                String key = bufferEntry.getKey();
                if (!cachedBuffer.isValid(key)) {
                    continue;
                }
                if (!frustum.isVisible(
                        AABB.encapsulatingFullBlocks(chunkSection.origin().offset(-1, -1, -1), chunkSection.origin().offset(16, 16, 16))
                )) {
                    continue;
                }
                // Only continue if the buffer is valid
                biggestBufferSize = addData(chunkSection, bufferEntry.getValue(), camPos, biggestBufferSize, drawList, transforms);
            }
        }
        return biggestBufferSize;
    }*/

    private static int renderEachSection(Minecraft minecraft, LivingEntity camera, Frustum frustum, List<RenderPass.Draw<GpuBufferSlice[]>> drawList, List<DynamicUniforms.Transform> transforms) {
        ChunkPos cameraChunkPos = new ChunkPos(camera.chunkX, camera.chunkZ);
        int biggestBufferSize = 0;

        for (int xx = -Compute.computationDistance + 1; xx < Compute.computationDistance; ++xx) {
            for (int zz = -Compute.computationDistance + 1; zz < Compute.computationDistance; ++zz) {
                ChunkPos chunkPos = new ChunkPos(cameraChunkPos.x + xx, cameraChunkPos.z + zz);

                for (int ii = 0; ii < 8; ++ii) {
                    SectionPos chunkSection = SectionPos.of(chunkPos, ii + minecraft.level.getMinSectionY());
                    if (!minecraft.levelRenderer.isSectionCompiled(chunkSection.origin())) {
                        // Don't bother doing anything if the chunk isn't rendered yet
                        continue;
                    }

                    if (Compute.cachedBuffers.containsKey(chunkSection)) {
                        DrawListHolder cachedBuffer = Compute.cachedBuffers.get(chunkSection);
                        for (var entry : cachedBuffer.getLists().entrySet()) {
                            String key = entry.getKey();
                            if (!cachedBuffer.isValid(key)) {
                                continue;
                            }
                            if (!frustum.isVisible(
                                    AABB.encapsulatingFullBlocks(chunkSection.origin().offset(-1, -1, -1), chunkSection.origin().offset(16, 16, 16))
                            )) {
                                continue;
                            }
                            // Only continue if the buffer is valid
                            biggestBufferSize = addData(chunkSection, entry.getValue(), camPos, biggestBufferSize, drawList, transforms);
                        }
                    }
                }
            }
        }
        return biggestBufferSize;
    }

/*    private static int goThroughVisibleSections(Minecraft minecraft, Camera camera, Vec3 camPos, Frustum frustum, List<RenderPass.Draw<GpuBufferSlice[]>> drawList, List<DynamicUniforms.Transform> transforms) {
        int biggestBufferSize = 0;
        for (var sections : minecraft.levelRenderer.getVisibleSections()) {
            var chunkSection = SectionPos.of(sections.getRenderOrigin());
            if (Compute.cachedBuffers.containsKey(chunkSection)) {
                DrawListHolder cachedBuffer = Compute.cachedBuffers.get(chunkSection);
                for (var entry : cachedBuffer.getLists().entrySet()) {
                    String key = entry.getKey();
                    if (!cachedBuffer.isValid(key)) {
                        continue;
                    }
                    // Only continue if the buffer is valid
                    biggestBufferSize = addData(chunkSection, entry.getValue(), camPos, biggestBufferSize, drawList, transforms);
                }
            }
        }
        return biggestBufferSize;
    }*/

    private static int addData(SubChunkPositionHelper chunkSection) {
        // Calculate the translation required to place the section at its right place
        Vec3 origin = new Vec3(chunkSection.origin());
        Vec3 dPos = origin.subtract(camPos);

        // Prepare the render data
        // If there is no index buffer available...
        if (gpuBuffer.getIndexBuffer() == null) {
            // ... try to reserve enough space to fit the vertex buffers
            if (gpuBuffer.getIndexCount() > biggestBufferSize) {
                biggestBufferSize = gpuBuffer.getIndexCount();
            }
        }

        // Get index of the current transform,
        // which is the size of the list *before* adding the transform to the list
        int currentTransformationIndex = transforms.size();
        Matrix4f modelViewMatrix = new Matrix4f(RenderSystem.getModelViewMatrix());
        modelViewMatrix.translate((float) dPos.x(), (float) dPos.y(), (float) dPos.z());
        transforms.add(
                new DynamicUniforms.Transform(
                        modelViewMatrix,
                        UNIT_COLOR_MODULATOR,
                        new Vector3f(),
                        DEFAULT_TEXTURE_MATRIX,
                        1.0F // Line Width
                )
        );

        drawList.add(new RenderPass.Draw<>(
                0, // slot (whatever a slot is in this context)
                gpuBuffer.getVertexBuffer(),
                gpuBuffer.getIndexBuffer(),
                gpuBuffer.getIndexType(),
                0, // first index
                gpuBuffer.getIndexCount(),
                (bufferSlice, uniformUploader) -> uniformUploader.upload("DynamicTransforms", bufferSlice[currentTransformationIndex])
        ));
        return biggestBufferSize;
    }

    /// See ChunkSectionsToRender
    // TODO? Maybe integrate Lighty render code more tightly into Minecraft render code, but wait if there are major changes in next versions until I do so
    private record Data(List<RenderPass.Draw<GpuBufferSlice[]>> drawList,
                        int maxIndicesRequired,
                        GpuBufferSlice[] dynamicTransforms) {
    }
}
