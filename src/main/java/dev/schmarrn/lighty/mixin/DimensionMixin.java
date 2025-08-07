package dev.schmarrn.lighty.mixin;

import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.api.LightyMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Dimension.class)
public class DimensionMixin {
	@Inject(method = "init(Lnet/minecraft/world/World;)V", at = @At("TAIL"))
	private void thing(World world, CallbackInfo ci) {
		LightyMode<?, ?> mode = ModeLoader.getCurrentMode(true);
		if (mode == null)
			return;
		mode.initializeDrawLists();
	}
}
