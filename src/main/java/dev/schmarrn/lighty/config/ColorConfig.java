package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.widget.OptionWidget;
import dev.schmarrn.lighty.ui.widget.TextBoxWidget;
import org.lwjgl.util.Color;

public class ColorConfig extends GetSetStringConfig<Color> {
	ColorConfig(String key, Color defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String getString() {
		Color color = getValue();
		return String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public boolean setString(String newString) {
		int colors;
		try {
			newString = newString.replaceFirst("^0[xX]", "");
			colors = Integer.parseInt(newString.toUpperCase(), 16);
		} catch (Exception e) {
			return false;
		}
		setValue(new Color(colors >> 16, colors >> 8 & 0xFF, colors & 0xFF));
		return true;
	}

	@Override
	public OptionWidget<Color> getOptionInstance() {
		return new TextBoxWidget<Color>(this);
	}


	@Override
	public String serialize() {
		return getString();
	}

	@Override
	public void deserialize(String color) {
		setString(color);
	}
}
