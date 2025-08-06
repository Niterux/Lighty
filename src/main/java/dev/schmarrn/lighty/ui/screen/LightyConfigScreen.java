package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.event.Compute;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.LanguageManager;

import java.text.MessageFormat;

public class LightyConfigScreen extends Screen {
	private final Screen parent;

	public LightyConfigScreen(Screen parent) {
		super();
		this.parent = parent;
	}

	private static String translate(String pattern) {
		return Lighty.LANGUAGE_MANAGER.translate(MessageFormat.format(pattern, Lighty.MOD_ID));
	}

	@Override
	public void removed() {
		super.removed();
		Config.save();
		Compute.markDirty();
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		super.renderBackground();
		super.render(mouseX, mouseY, tickDelta);
		this.drawCenteredString(this.textRenderer, translate("gui.{0}.options.title"), this.width / 2, this.height / 4 - 40, 0xFFFFFF);
	}

	@Override
	public void mouseReleased(int mx, int my, int buttonNum) {
		super.mouseReleased(mx, my, buttonNum);
		Compute.markDirty();
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		super.buttonClicked(button);
		switch (button.id) {
			case 1:
				minecraft.openScreen(new GlobalConfigScreen(minecraft.screen));
				break;
			case 2:
				minecraft.openScreen(new ModeSpecificsConfigScreen(minecraft.screen));
				break;
			case 3:
				minecraft.openScreen(this.parent);
		}
	}

	@Override
	public void init() {
		super.init();
		buttons.add(new ButtonWidget(1, width / 2, height / 4, translate("gui.{0}.options.general.title")));
		buttons.add(new ButtonWidget(2, width / 2, height / 4 + 50, translate("gui.{0}.options.modes.title")));
		buttons.add(new ButtonWidget(3, width / 2, height / 4 + 100, "Back"));
	}
}
