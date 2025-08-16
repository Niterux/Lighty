package dev.schmarrn.lighty.api;

import dev.schmarrn.lighty.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightyHelper {

    public static float getOffset(BlockPos pos, World world) {
        BlockPos posUp = new BlockPos(pos.x, pos.y + 1, pos.z);
        int blockIdUp = world.getBlock(posUp.x, posUp.y, posUp.z);
        if (blockIdUp == 0)
            return 0f;
        Block blockUp = Block.BY_ID[world.getBlock(posUp.x, posUp.y, posUp.z)];
        // Returns the offset of blocks that aren't 16 pixels high
        return (float) blockUp.maxY;
    }

    public static boolean isBlocked(BlockPos pos, World world) {
        if (pos.y < 0 || pos.y > 126)
            return true;
        BlockPos posUp = new BlockPos(pos.x, pos.y + 1, pos.z);
        Block blockUp = Block.BY_ID[world.getBlock(posUp.x, posUp.y, posUp.z)];
        Material material;
        if (blockUp == null) {
            material = Material.AIR;
        } else {
            material = blockUp.material;
        }
        return (material.isSolid()) || !(world.isRedstoneConductor(pos.x, pos.y, pos.z) && !world.isRedstoneConductor(posUp.x, posUp.y, posUp.z) && !material.isLiquid());
    }

    public static boolean isSafe(int blockLightLevel) {
        return !(blockLightLevel <= Config.BLOCK_THRESHOLD.getValue());
    }
}
