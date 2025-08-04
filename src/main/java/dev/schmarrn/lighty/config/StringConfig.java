package dev.schmarrn.lighty.config;

import net.minecraft.client.gui.options.components.OptionsComponent;

public class StringConfig extends ConfigType<String> {
	@Override
	public OptionsComponent getOptionInstance() {
		return null;
	}

	public StringConfig(String key, String defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public String serialize() {
		return getValue();
	}

	@Override
	public void deserialize(String value) {
		setValue(value);
	}
}
