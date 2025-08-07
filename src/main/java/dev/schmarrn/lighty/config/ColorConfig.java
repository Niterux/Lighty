package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.widget.OptionWidget;
import dev.schmarrn.lighty.ui.widget.TextBoxWidget;
import org.lwjgl.util.Color;

public class ColorConfig extends ConfigType<Color> {
	ColorConfig(String key, Color defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public OptionWidget<Color> getOptionInstance() {
		return new TextBoxWidget<>(this);
	}


	@Override
	public String serialize() {
		Color color = getValue();
		return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public Color deserialize(String color) throws DeserializationException {
		try {
			if (!color.startsWith("0x") || color.length() != 8)
				throw new DeserializationException("Invalid format for a color!");
			color = color.replaceFirst("^0x", "");
			int colors = Integer.parseInt(color.toUpperCase(), 16);
			return new Color(colors >> 16, colors >> 8 & 0xFF, colors & 0xFF);
		} catch (NumberFormatException e) {
			throw new DeserializationException("Invalid format for a color!");
		}
	}
}
