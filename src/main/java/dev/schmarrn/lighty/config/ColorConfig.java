package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.ColorWidget;
import dev.schmarrn.lighty.ui.widget.OptionWidget;
import dev.schmarrn.lighty.ui.widget.TextBoxWidget;
import org.lwjgl.util.Color;

public class ColorConfig extends ConfigType<Color> {
	@Override
	public OptionWidget<Color> getOptionInstance(OptionsScreen optionsScreen) {
		return new TextBoxWidget<Color>(optionsScreen, this);
	}

	public ColorConfig(String key, Color defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String serialize() {
		Color color = getValue();
		return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void deserialize(String color) {
		color = color.replaceFirst("^0[xX]", "");
		int colors = Integer.parseInt(color.toUpperCase(), 16);
		setValue(new Color(colors >> 16, colors >> 8 & 0xFF, colors & 0xFF));
	}
}
