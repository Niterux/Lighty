package dev.schmarrn.lighty;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.schmarrn.lighty.ui.LightyConfigScreen;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return LightyConfigScreen::new;
	}
}
