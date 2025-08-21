package dev.schmarrn.lighty.renderers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.ModPath;
import dev.schmarrn.lighty.api.ModeManager;
import dev.schmarrn.lighty.api.OverlayData;
import dev.schmarrn.lighty.api.OverlayRenderer;
import dev.schmarrn.lighty.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CrossRenderer implements OverlayRenderer {
    public void build(World world, BlockPos pos, OverlayData data, BufferBuilder builder, int lightmap) {
        float x1 = data.rPos().x;
        float x2 = data.rPos().x + 1f;
        float y  = data.rPos().y + 1.005f + data.yOffset();
        float z1 = data.rPos().z;
        float z2 = data.rPos().z + 1f;
        builder.color(data.color());
        builder.normal(1f, 0f, 1f);
        builder.vertex(x1, y, z1);
        builder.vertex(x2, y, z2);
        builder.normal(1f, 0f, -1f);
        builder.vertex(x1, y, z2);
        builder.vertex(x2, y, z1);
    }

    @Override
    public int getDrawMode() {
        return GL11.GL_LINES;
    }

    @Override
    public ModPath getTextureLocation() {
        return Config.CROSS_TEXTURE.getValue();
    }

    @Override
    public ModPath getResourceLocation() {
        return new ModPath(Lighty.MOD_ID, "renderer_cross");
    }

    public static void init() {
        var dp = new CrossRenderer();
        ModeManager.registerRenderer(dp.getResourceLocation(), dp);
    }
}
