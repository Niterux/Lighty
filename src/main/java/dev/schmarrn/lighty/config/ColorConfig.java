package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.ColorWidget;
import dev.schmarrn.lighty.ui.widget.OptionWidget;

public class ColorConfig extends ConfigType<Color> {
	@Override
	public OptionWidget<Color> getOptionInstance(OptionsScreen optionsScreen) {
		return new ColorWidget(optionsScreen, this);
	}

	public ColorConfig(String key, Color defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String serialize() {
		Color color = getValue();
		return "0x" + Integer.toHexString(color.toInt());
	}

	@Override
	public void deserialize(String color) {
		setValue(Color.fromHex(color));
	}
}
