package dev.schmarrn.lighty.mixin;

import dev.schmarrn.lighty.ui.LightyConfigScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.options.ScreenOptions;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.GameOptionsRegistry;
import net.minecraft.client.render.Font;
import net.minecraft.core.lang.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = ScreenOptions.class, remap = false)
public abstract class ScreenOptionsMixin extends Screen {

	@Unique
	private boolean isLightyScreen() {
		return (Object) this instanceof LightyConfigScreen;
	}

	@Unique
	private List<GameOptions> redirect(GameOptionsRegistry instance) {
		if (isLightyScreen()) {
			return LightyConfigScreen.getPages();
		} else {
			return instance.getPages();
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/options/ScreenOptions;drawStringCentered(Lnet/minecraft/client/render/Font;Ljava/lang/String;III)V"))
	public void lighty$changeTitle$render(ScreenOptions instance, Font font, String s, int i, int j, int k) {
		I18n i18n = I18n.getInstance();

		drawStringCentered(this.font, i18n.translateKey(isLightyScreen() ? "gui.lighty.options.title" : s), i, j, k);
	}

	@Redirect(
		method = "drawPagesListItems",
		at = @At(
			value = "INVOKE",
			target = "Lnet.minecraft.client.options.GameOptionsRegistry;getPages()Ljava/util/List;"
		)
	)
	private List<GameOptions> lighty$redirectPages$drawPagesListItems(GameOptionsRegistry instance) {
		return redirect(instance);
	}

	@Redirect(
		method = "getTotalPagesListHeight",
		at = @At(
			value = "INVOKE",
			target = "Lnet.minecraft.client.options.GameOptionsRegistry;getPages()Ljava/util/List;"
		)
	)
	private List<GameOptions> lighty$redirectPages$getTotalPagesListHeight(GameOptionsRegistry instance) {
		return redirect(instance);
	}

	@Redirect(
		method = "mouseClicked",
		at = @At(
			value = "INVOKE",
			target = "Lnet.minecraft.client.options.GameOptionsRegistry;getPages()Ljava/util/List;"
		)
	)
	private List<GameOptions> lighty$redirectPages$mouseClicked(GameOptionsRegistry instance) {
		return redirect(instance);
	}
}
