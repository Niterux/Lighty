package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.config.ConfigType;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class ModeSpecificsConfigScreen extends OptionsScreen {
	public ModeSpecificsConfigScreen(Screen parent) {
		super(parent);
	}

	@Override
	public List<ConfigType<?>> getOptionList() {
		return Config.MODE_SPECIFIC_OPTIONS;
	}
}
