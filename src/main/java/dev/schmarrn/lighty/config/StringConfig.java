package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.OptionWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

public class StringConfig extends ConfigType<String> {
	StringConfig(String key, String defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public OptionWidget<String> getOptionInstance() {
		return null;
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
