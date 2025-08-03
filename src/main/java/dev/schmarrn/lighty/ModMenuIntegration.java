package dev.schmarrn.lighty;

import dev.schmarrn.lighty.ui.LightyConfigScreen;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public String getModId() {
		return Lighty.MOD_ID;
	}

	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return LightyConfigScreen::new;
	}
}
