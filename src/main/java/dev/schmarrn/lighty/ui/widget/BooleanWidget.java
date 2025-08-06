package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.config.ConfigType;
import net.minecraft.client.Minecraft;

public class BooleanWidget extends OptionWidget<Boolean> {
	private static int KNOB_WIDTH = 24;
	private static int TOGGLE_HEIGHT = 20;
	public BooleanWidget(ConfigType<Boolean> option) {
		super(option);
	}

	@Override
	public void onClick() {
		option.setValue(!option.getValue());
		playClickingSound();
	}

	@Override
	public void render(Minecraft minecraft) {
		minecraft.textureManager.bind(minecraft.textureManager.load("/gui/gui.png"));
		int knobX = option.getValue() ? width - KNOB_WIDTH + x : x;
		int renderedTextY = height / 2 + y - 4;
		int buttonVCoordinate = hovered ? 86 : 66;
		String message = Lighty.LANGUAGE_MANAGER.translate(option.getValue() ? "options.on" : "options.off");
		drawTexture(x, y, 0, 46, width / 2, TOGGLE_HEIGHT);
		drawTexture(x + (width / 2), y, 200 - (width / 2), 46, width / 2, TOGGLE_HEIGHT);
		drawTexture(knobX, y, 0, buttonVCoordinate, KNOB_WIDTH / 2, TOGGLE_HEIGHT);
		drawTexture(knobX + (KNOB_WIDTH / 2), y, 200 - (KNOB_WIDTH / 2), buttonVCoordinate, KNOB_WIDTH / 2, TOGGLE_HEIGHT);
		drawCenteredString(minecraft.textRenderer, message, knobX + (KNOB_WIDTH / 2), renderedTextY, 0xFFFFFFFF);
	}

	@Override
	public int getPreferredWidth(int listWidth) {
		return 40;
	}
}
