package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.OptionWidget;

public abstract class ConfigType<T> {
	private final T DEFAULT_VALUE;
	private final String KEY;
	private T value;

	ConfigType(String key, T defaultValue) {
		this.KEY = key;
		this.DEFAULT_VALUE = defaultValue;
		this.value = defaultValue;

		Config.register(key, this);
	}

	public void onChange(T value) {
	}

	public T getValue() {
		return value;
	}

	public void setValue(T newValue) {
		value = newValue;
		onChange(newValue);
		Config.save();
	}

	public void resetToDefault() {
		value = DEFAULT_VALUE;
	}

	public T getDefault() {
		return DEFAULT_VALUE;
	}

	public String getKey() {
		return KEY;
	}

	public abstract OptionWidget<T> getOptionInstance();

	public String getTranslationKey() {
		return KEY.replaceFirst("\\.", ".options.");
	}

	public String getTranslationTooltipKey() {
		return getTranslationKey() + ".tooltip";
	}
	public abstract String serialize();
	public abstract T deserialize(String value) throws DeserializationException;
}
