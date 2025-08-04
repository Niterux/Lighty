package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.BooleanWidget;
import dev.schmarrn.lighty.ui.widget.OptionWidget;

public class BooleanConfig extends ConfigType<Boolean> {
	@Override
	public OptionWidget<Boolean> getOptionInstance(OptionsScreen optionsScreen) {
		return new BooleanWidget(optionsScreen, this);
	}

	public BooleanConfig(String key, Boolean defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String serialize() {
		return Boolean.toString(getValue());
	}

	@Override
	public void deserialize(String value) {
		setValue(Boolean.valueOf(value));
	}
}
