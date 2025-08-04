package dev.schmarrn.lighty.config;

public interface ConfigSerDe {
	String serialize();
	void deserialize(String value);
}
