package dev.schmarrn.lighty.mode;

import dev.schmarrn.lighty.config.Config;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.Color;

public class Provider {
	public static boolean isBlocked(int x, int y, int z, World world) {
		Block block = Block.BY_ID[world.getBlock(x, y, z)];
		Material material;
		if (block == null) {
			material = Material.AIR;
		} else {
			material = block.material;
		}
		if (y < 0 || y > 127)
			return true;

		return (material.isSolid()) || !(world.isRedstoneConductor(x, y-1, z) && !world.isRedstoneConductor(x, y, z) && !material.isLiquid());
	}

	public static @Nullable IntIntMutablePair compute(World world, int x, int y, int z) {
		if (world == null) return null;
		WorldChunk chunk = world.getChunk(x, z);

		int blockLightLevel = chunk.getLightAt(LightType.BLOCK, x & 0xF, y+1, z & 0xF);
		int skyLightLevel = chunk.getLightAt(LightType.SKY, x & 0xF, y+1, z & 0xF);

		return new IntIntMutablePair(blockLightLevel, skyLightLevel);
	}

	public static @Nullable ColorEnum getColor(IntIntMutablePair light) {
		return getColor(light.leftInt(), light.rightInt());
	}

	public static @Nullable ColorEnum getColor(int blockLightLevel, int skyLightLevel) {
		if (blockLightLevel <= Config.BLOCK_THRESHOLD.getValue()) {
			if (skyLightLevel <= Config.SKY_THRESHOLD.getValue()) {
				return ColorEnum.UNSAFE;
			} else {
				return ColorEnum.DAYLIGHT_SAFE;
			}
		}
		if (!Config.SHOW_SAFE.getValue())
			return null;

		return ColorEnum.SAFE;
	}

	static class Pos {
		public double x;
		public double y;
		public double z;

		public Pos(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
