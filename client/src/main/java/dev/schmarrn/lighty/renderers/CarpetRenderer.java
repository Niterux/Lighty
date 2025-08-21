package dev.schmarrn.lighty.renderers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.ModPath;
import dev.schmarrn.lighty.api.ModeManager;
import dev.schmarrn.lighty.api.OverlayData;
import dev.schmarrn.lighty.api.OverlayRenderer;
import dev.schmarrn.lighty.config.Config;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CarpetRenderer implements OverlayRenderer {
    public static void init() {
        var dp = new CarpetRenderer();
        ModeManager.registerRenderer(dp.getResourceLocation(), dp);
    }

    public void build(World level, BlockPos pos, OverlayData data, BufferBuilder builder, int lightmap) {
        float x = data.rPos().x;
        float y = data.rPos().y + 1 + data.yOffset();
        float z = data.rPos().z;

        try {
            builder.color(ColorMixer.multiplyColorAndLight(data.color(), lightmap));
            builder.normal(0, 1f, 0f);
            builder.vertex(x, y + 1 / 16f, z, 0, 0);
            builder.vertex(x, y + 1 / 16f, z + 1, 0, 1);
            builder.vertex(x + 1, y + 1 / 16f, z + 1, 1, 1);
            builder.vertex(x + 1, y + 1 / 16f, z, 1, 0);
            if (data.yOffset() > 0.001f) {
                //if it renders above it should check if the block above culls the faces
                pos = new BlockPos(pos.x, pos.y + 1, pos.z);
            }
            //NORTH
            if (Block.shouldRenderFace(Blocks.STONE.defaultBlockState(), level.getBlockState(pos.relative(Direction.SOUTH)), Direction.SOUTH)) {
                builder.normal(0f, 0f, -1f);
                builder.vertex(x, y + 1 / 16f, z + 1);
                builder.vertex(x, y, z + 1);
                builder.vertex(x + 1, y, z + 1);
                builder.vertex(x + 1, y + 1 / 16f, z + 1);
            }
            //EAST
            if (Block.shouldRenderFace(Blocks.STONE.defaultBlockState(), level.getBlockState(pos.relative(Direction.WEST)), Direction.WEST)) {
                builder.normal(-1f, 0f, 0f);
                builder.vertex(x, y + 1 / 16f, z);
                builder.vertex(x, y, z);
                builder.vertex(x, y, z + 1);
                builder.vertex(x, y + 1 / 16f, z + 1);
            }
            //SOUTH
            if (Block.shouldRenderFace(Blocks.STONE.defaultBlockState(), level.getBlockState(pos.relative(Direction.NORTH)), Direction.NORTH)) {
                builder.normal(0f, 0f, 1f);
                builder.vertex(x + 1, y + 1 / 16f, z);
                builder.vertex(x + 1, y, z);
                builder.vertex(x, y, z);
                builder.vertex(x, y + 1 / 16f, z);
            }
            //WEST
            if (Block.shouldRenderFace(Blocks.STONE.defaultBlockState(), level.getBlockState(pos.relative(Direction.EAST)), Direction.EAST)) {
                builder.normal(1f, 0f, 0f);
                builder.vertex(x + 1, y + 1 / 16f, z + 1);
                builder.vertex(x + 1, y, z + 1);
                builder.vertex(x + 1, y, z);
                builder.vertex(x + 1, y + 1 / 16f, z);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getDrawMode() {
        return GL11.GL_QUADS;
    }

    @Override
    public ModPath getTextureLocation() {
        return Config.CARPET_TEXTURE.getValue();
    }

    @Override
    public ModPath getResourceLocation() {
        return new ModPath(Lighty.MOD_ID, "renderer_carpet");
    }
}
