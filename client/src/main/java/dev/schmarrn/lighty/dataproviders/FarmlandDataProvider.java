package dev.schmarrn.lighty.dataproviders;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.*;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class FarmlandDataProvider implements OverlayDataProvider {
    public OverlayData compute(World world, BlockPos pos, Vec3i rPos) {
        BlockPos posUp = new BlockPos(pos.x, pos.y + 1, pos.z);

        if (!(world.getBlock(pos.x, pos.y, pos.z) == Block.FARMLAND.id)) {
            return OverlayData.invalid();
        }

        int blockLightLevel = world.getLight(LightType.BLOCK, posUp.x, posUp.y, posUp.z);
        int skyLightLevel = world.getLight(LightType.SKY, posUp.x, posUp.y, posUp.z);

        int color = LightyColors.getGrowthARGB(blockLightLevel, skyLightLevel);

        float offset = -1f/15f;

        return new OverlayData(true, color, skyLightLevel, blockLightLevel, pos, rPos, offset);
    }

    @Override
    public ModPath getModPath() {
        return new ModPath(Lighty.MOD_ID, "data_provider_farmland");
    }

    public static void init() {
        var dp = new FarmlandDataProvider();
        ModeManager.registerDataProvider(dp.getModPath(), dp);
    }
}
