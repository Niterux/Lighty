package dev.schmarrn.lighty.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public interface OverlayDataProvider {
    OverlayData compute(World level, BlockPos pos, Vec3i rPos);

    ModPath getResourceLocation();
}
