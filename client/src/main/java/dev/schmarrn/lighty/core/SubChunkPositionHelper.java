package dev.schmarrn.lighty.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class SubChunkPositionHelper extends Vec3i {
    public SubChunkPositionHelper(int x, int y, int z) {
        super(x, y, z);
    }

    public static SubChunkPositionHelper fromBlockVec3(Vec3i position) {
        return new SubChunkPositionHelper(position.x >> 4, position.y >> 4, position.z >> 4);
    }
    public static SubChunkPositionHelper fromBlockPos(BlockPos position) {
        return new SubChunkPositionHelper(position.x >> 4, position.y >> 4, position.z >> 4);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(this.x << 4, this.y << 4, this.z << 4);
    }

    public int getTaxicabDistance(SubChunkPositionHelper other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z);
    }

    public long packIntoLong() {
        return ((long)this.x << 36) | ((long)this.y << 28) | ((long)this.z & 0xFFFFFFF);
    }
}
