package dev.schmarrn.lighty.api;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface OverlayRenderer {
    void build(World level, BlockPos pos, OverlayData data, BufferBuilder builder, int lightmap);

    int getDrawMode();

    ModPath getTextureLocation();

    ModPath getResourceLocation();
}
