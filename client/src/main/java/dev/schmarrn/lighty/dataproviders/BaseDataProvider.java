package dev.schmarrn.lighty.dataproviders;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.*;
import dev.schmarrn.lighty.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class BaseDataProvider implements OverlayDataProvider {
    public OverlayData compute(World world, BlockPos pos, Vec3i rPos) {
        BlockPos posUp = new BlockPos(pos.x, pos.y + 1, pos.z);

        if (LightyHelper.isBlocked(pos, world)) {
            return OverlayData.invalid();
        }

        int blockLightLevel = world.getLight(LightType.BLOCK, posUp.x, posUp.y, posUp.z);
        int skyLightLevel = world.getLight(LightType.SKY, posUp.x, posUp.y, posUp.z);

        if (LightyHelper.isSafe(blockLightLevel) && !Config.SHOW_SAFE.getValue()) {
            return OverlayData.invalid();
        }

        int color = LightyColors.getARGB(blockLightLevel, skyLightLevel);

        float offset = LightyHelper.getOffset(pos, world);
        if (offset == -1f) {
            return OverlayData.invalid();
        }

        return new OverlayData(true, color, skyLightLevel, blockLightLevel, pos, rPos, offset);
    }

    @Override
    public ModPath getModPath() {
        return new ModPath(Lighty.MOD_ID, "data_provider_base");
    }

    public static void init() {
        var dp = new BaseDataProvider();
        ModeManager.registerDataProvider(dp.getModPath(), dp);
    }
}
