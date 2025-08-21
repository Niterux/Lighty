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

import static dev.schmarrn.lighty.renderers.ColorMixer.multiplyColorAndLight;

public class NumberRenderer implements OverlayRenderer {
    private static final float PXL = 1 / 16f;
    private static final float dx = 0.25f;
    private static final float dz = 0.25f;

    public static void init() {
        var dp = new NumberRenderer();
        ModeManager.registerRenderer(dp.getResourceLocation(), dp);
    }

    public void build(World level, BlockPos pos, OverlayData data, BufferBuilder builder, int lightmap) {
        float x1 = data.rPos().x + PXL * 5.25f;
        float y = data.rPos().y + 1f + 0.005f + data.yOffset();
        float z1 = data.rPos().z + PXL * 4f;

        if (Config.SHOW_SKYLIGHT_LEVEL.getValue()) {
            renderNumber(builder, data.blockNumber(), x1, y, z1, data.color(), lightmap);
            renderNumber(builder, data.skyNumber(), x1, y, z1 + PXL * 6f, data.color(), lightmap);
        } else {
            renderNumber(builder, data.blockNumber(), x1, y, z1 + PXL * 2f, data.color(), lightmap);
        }
    }

    private static void renderNumber(BufferBuilder builder, int number, float x, float y, float z, int color, int lightmap) {
        int oneDigit = number % 10;
        int tenDigit = number / 10;

        if (tenDigit > 0) {
            renderDigit(builder, tenDigit, x, y, z, color, lightmap);
            renderDigit(builder, oneDigit, x + dx - PXL, y, z, color, lightmap);
        } else {
            renderDigit(builder, oneDigit, x + (dx - PXL) / 2f, y, z, color, lightmap);
        }
    }

    private static void renderDigit(BufferBuilder builder, int digit, float x, float y, float z, int color, int lightmap) {
        float startU = (0b11 & digit) / 4f;
        float startV = ((digit >> 2) & 0b11) / 4f;
        float width = 0.25f;
        builder.color(multiplyColorAndLight(color, lightmap));
        builder.normal(0f, 1f, 0f);
        builder.vertex(x, y, z, startU, startV);
        builder.vertex(x, y, z + dz, startU, startV + width);
        builder.vertex(x + dx, y, z + dz, startU + width, startV + width);
        builder.vertex(x + dx, y, z, startU + width, startV);
    }

    @Override
    public int getDrawMode() {
        return GL11.GL_QUADS;
    }

    @Override
    public ModPath getTextureLocation() {
        return new ModPath(Lighty.MOD_ID, "textures/block/numbers.png");
    }

    @Override
    public ModPath getResourceLocation() {
        return new ModPath(Lighty.MOD_ID, "renderer_number");
    }
}
