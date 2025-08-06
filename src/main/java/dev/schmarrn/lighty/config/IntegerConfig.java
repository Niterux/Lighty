package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.OptionWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

public class IntegerConfig extends ConfigType<Integer> {
	private final int min, max;

	public IntegerConfig(String key, Integer defaultValue, int min, int max) {
		super(key, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	public OptionWidget<Integer> getOptionInstance() {
		return null;
	}



	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	@Override
	public void setValue(Integer newValue) {
		if (newValue <= max) {
			super.setValue(Math.max(min, newValue));
		} else {
			Lighty.LOGGER.error("Lighty Config: {}, new value {} out of bounds: [{}, {}]. Ignoring new value.", getKey(), newValue, min, max);
		}
	}

	@Override
	public String serialize() {
		return Integer.toString(getValue());
	}

	@Override
	public void deserialize(String value) {
		setValue(Integer.valueOf(value));
	}
}
