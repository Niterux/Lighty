package dev.schmarrn.lighty.ui.screen;


import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.config.ConfigType;

import java.util.List;

public class GlobalConfigScreen extends OptionsScreen {

	@Override
	public List<ConfigType<?>> getOptionList() {
		return Config.GLOBAL_OPTIONS;
	}
}
