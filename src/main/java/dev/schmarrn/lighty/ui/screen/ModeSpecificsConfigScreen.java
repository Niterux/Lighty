package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.config.ConfigType;

import java.util.List;

public class ModeSpecificsConfigScreen extends OptionsScreen {
	@Override
	public List<ConfigType<?>> getOptionList() {
		return Config.MODE_SPECIFIC_OPTIONS;
	}
}
