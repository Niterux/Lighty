package dev.schmarrn.lighty.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.schmarrn.lighty.LightyWorldEventListener;
import dev.schmarrn.lighty.event.Compute;
import dev.schmarrn.lighty.event.KeyBind;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@ModifyExpressionValue(method = "tick()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 2, remap = false))
	private int lighty$keybindHandler(int keyCode) {
		KeyBind.callback(keyCode);
		return keyCode;
	}

	@Inject(method = "tick()V", at = @At("TAIL"))
	private void lighty$tick(CallbackInfo ci) {
		Compute.callback();
	}

	@Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/living/player/PlayerEntity;)V", at = @At("TAIL"))
	private void lighty$registerOnLightUpdateHandler(World world, String string, PlayerEntity playerEntity, CallbackInfo ci) {
		if (world != null)
			world.addEventListener(new LightyWorldEventListener());
	}
}
