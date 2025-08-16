package dev.schmarrn.lighty.mixin;

import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.api.LightyMode;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "Lnet/minecraft/entity/living/player/PlayerEntity;postSpawn()V", at = @At("HEAD"))
	private void thing(CallbackInfo ci){
		LightyMode<?, ?> mode = ModeLoader.getCurrentMode(true);
		if (mode == null)
			return;
		mode.initializeDrawLists();
	}
}
