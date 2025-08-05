package dev.schmarrn.lighty.mode;

import dev.schmarrn.lighty.config.Config;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.block.Block;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.Color;

public class Provider {
	public static boolean isBlocked(int x, int y, int z, World world) {
		Block block = Block.BY_ID[world.getBlock(x, y, z)];

		if (y < 0 || y > 127)
			return true;

		return (block != null && (block.material.isSolid()))
			|| !(world.isRedstoneConductor(x, y-1, z)
			&& !world.isRedstoneConductor(x, y, z)
			&& !world.getMaterial(x, y, z).isLiquid());
	}

	public static @Nullable IntIntMutablePair compute(World world, int x, int y, int z) {
		if (world == null) return null;
		WorldChunk chunk = world.getChunk(x, z);

		int blockLightLevel = chunk.getLightAt(LightType.BLOCK, x & 0xF, y+1, z & 0xF);
		int skyLightLevel = chunk.getLightAt(LightType.SKY, x & 0xF, y+1, z & 0xF);

		return new IntIntMutablePair(blockLightLevel, skyLightLevel);
	}

	public static @Nullable Color getColor(IntIntMutablePair light) {
		return getColor(light.leftInt(), light.rightInt());
	}

	public static @Nullable Color getColor(int blockLightLevel, int skyLightLevel) {
		Color color =  Config.OVERLAY_GREEN.getValue();

		if (blockLightLevel <= Config.BLOCK_THRESHOLD.getValue()) {
			if (skyLightLevel <= Config.SKY_THRESHOLD.getValue()) {
				color =  Config.OVERLAY_RED.getValue();
			} else {
				color =  Config.OVERLAY_ORANGE.getValue();
			}
		} else if (!Config.SHOW_SAFE.getValue()) {
			return null;
		}

		return color;
	}

	static class Pos {
		public int x;
		public int y;
		public int z;

		public Pos(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
