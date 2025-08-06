package dev.schmarrn.lighty;

import dev.schmarrn.lighty.api.LightyModesRegistration;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.event.Compute;
import dev.schmarrn.lighty.event.KeyBind;
import dev.schmarrn.lighty.event.Render;
//import dev.schmarrn.lighty.mode.CarpetMode;
import dev.schmarrn.lighty.mode.CrossMode;
import dev.schmarrn.lighty.mode.NumberMode;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.LanguageManager;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Lighty implements ClientModInitializer {
	public static final LanguageManager LANGUAGE_MANAGER = LanguageManager.getInstance();
    public static final String MOD_ID = "lighty";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void initClient() {
		LOGGER.info("Let there be {}", MOD_ID);

		Config.init();

		KeyBind.init();
		Compute.init();
		Render.init();

		//CarpetMode.init();
		CrossMode.init();
		NumberMode.init();
		FabricLoader.getInstance().getEntrypoints("lightyModesRegistration", LightyModesRegistration.class).forEach(LightyModesRegistration::registerLightyModes);

		ModeLoader.setLastUsedMode();
		MinecraftEvents.READY.register(this::afterClientStart);
	}

	public void afterClientStart(Minecraft minecraft) {
		KeyBind.register();
	}
}
