package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import dev.schmarrn.lighty.ui.widget.OptionWidget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringListConfig extends ConfigType<List<String>> {
	public StringListConfig(String key, List<String> defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public OptionWidget<List<String>> getOptionInstance(OptionsScreen optionsScreen) {
		return null;
	}

	@Override
	public String serialize() {
		StringBuilder builder = new StringBuilder();
		for (int ii = 0; ii < this.getValue().size() - 1; ii++) {
			builder.append(this.getValue().get(ii).toString());
			builder.append(" ");
		}
		builder.append(this.getValue().get(this.getValue().size()-1));
		return builder.toString();
	}

	@Override
	public void deserialize(String value) {
		setValue(Arrays.stream(value.split(" ")).collect(Collectors.toList()));
	}
}
