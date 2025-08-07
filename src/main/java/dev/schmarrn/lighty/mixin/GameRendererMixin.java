package dev.schmarrn.lighty.mixin;

import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.event.Render;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Minecraft minecraft;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;turnOn()V"))
	private void lighty$overlayRenderer(float partialTicks, long updateRenderersUntil, CallbackInfo ci) {
		if (minecraft.world != null && minecraft.player != null && ModeLoader.getCurrentMode(false) != null)
			Render.renderOverlay(partialTicks);
	}
}
