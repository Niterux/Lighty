package dev.schmarrn.lighty;

import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.event.Compute;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import net.minecraft.client.entity.living.player.InputPlayerEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.WorldEventListener;

public class LightyWorldEventListener implements WorldEventListener {
	@Override
	public void onBlockChanged(int x, int y, int z) {
		InputPlayerEntity player = MinecraftInstanceAccessor.getMinecraft().player;
		if (player == null)
			return;

		x = Math.abs(x-(int)player.x);
		y = Math.abs(y-(int)player.y);
		z = Math.abs(z-(int)player.z);

		int computeDistance = Config.OVERLAY_DISTANCE.getValue() * 16;

		if (x <= computeDistance && y <= computeDistance && z <= computeDistance)
			Compute.markDirty();
	}

	// We do not need anything below this, but WorldEventListener REALLY wants us to take the rest home…
	@Override
	public void onRegionChanged(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {

	}

	@Override
	public void playSound(String name, double x, double y, double z, float pitch, float volume) {

	}

	@Override
	public void addParticle(String type, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {

	}

	@Override
	public void onEntityAdded(net.minecraft.entity.Entity entity) {

	}

	@Override
	public void onEntityRemoved(net.minecraft.entity.Entity entity) {

	}

	@Override
	public void onAmbientDarknessChanged() {

	}

	@Override
	public void onRecordRemoved(String record, int x, int y, int z) {

	}

	@Override
	public void onBlockEntityChanged(int x, int y, int z, BlockEntity blockEntity) {

	}

	@Override
	public void doEvent(PlayerEntity source, int type, int x, int y, int z, int data) {

	}
}
