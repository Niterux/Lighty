package dev.schmarrn.lighty.event;

import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.SMACH;
import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.InputPlayerEntity;
import net.minecraft.world.World;

public class Compute {
	private static boolean dirty;

	private static int oldPlayerX = 0;
	private static int oldPlayerY = 0;
	private static int oldPlayerZ = 0;

	/**
	 * <p>Will be called at the end of every tick cycle, if a {@link LightyMode} is active.</p>
	 * <p>Both the current world and the player will be guaranteed not `null` during this method call.</p>
	 */
	public static void callback() {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		World world = minecraft.world;
		InputPlayerEntity player = minecraft.player;

		if (player == null || world == null) return;

		SMACH.updateCompute(player);

		if (oldPlayerX != (int) player.x || oldPlayerY != (int) player.y || oldPlayerZ != (int) player.z) {
			oldPlayerX = (int) player.x;
			oldPlayerY = (int) player.y;
			oldPlayerZ = (int) player.z;
			markDirty();
		}

		if (!SMACH.isEnabled()) return;

		LightyMode<?, ?> mode = ModeLoader.getCurrentMode(false);
		if (mode == null) return;

		if (!dirty)
			return;
		dirty = false;
		mode.beforeCompute();

		int computeDistance = Config.OVERLAY_DISTANCE.getValue() * 16;

		for (int x = -computeDistance; x <= computeDistance; ++x)
			for (int y = -computeDistance; y <= computeDistance; ++y)
				for (int z = -computeDistance; z <= computeDistance; ++z)
					mode.compute(world, (int) player.x + x, (int) player.y + y, (int) player.z + z);

		mode.afterCompute();
		mode.swap();
	}

	public static void init() {
		dirty = false;
	}

	public static void markDirty() {
		dirty = true;
	}
}
