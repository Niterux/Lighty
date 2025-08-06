package dev.schmarrn.lighty.config;

public abstract class GetSetStringConfig<T> extends ConfigType<T> {
	GetSetStringConfig(String key, T defaultValue) {
		super(key, defaultValue);
	}

	public abstract String getString();
	public abstract boolean setString(String newString);
}
