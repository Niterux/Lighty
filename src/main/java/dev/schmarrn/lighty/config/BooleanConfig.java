package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.widget.BooleanWidget;
import dev.schmarrn.lighty.ui.widget.OptionWidget;

public class BooleanConfig extends ConfigType<Boolean> {
	@Override
	public OptionWidget<Boolean> getOptionInstance() {
		return new BooleanWidget(this);
	}

	public BooleanConfig(String key, Boolean defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String serialize() {
		return Boolean.toString(getValue());
	}

	@Override
	public Boolean deserialize(String value) {
		return Boolean.valueOf(value);
	}
}
