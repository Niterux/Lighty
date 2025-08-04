package dev.schmarrn.lighty.config;

import net.minecraft.client.gui.widget.ButtonWidget;

public class BooleanConfig extends ConfigType<Boolean> {
	@Override
	public ButtonWidget getOptionInstance() {
		return null;
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
