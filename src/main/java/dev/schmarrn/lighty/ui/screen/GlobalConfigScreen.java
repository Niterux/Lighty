package dev.schmarrn.lighty.ui.screen;


import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.config.ConfigType;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class GlobalConfigScreen extends OptionsScreen {

	public GlobalConfigScreen(Screen parent) {
		super(parent);
	}

	@Override
	public List<ConfigType<?>> getOptionList() {
		return Config.GLOBAL_OPTIONS;
	}
}
