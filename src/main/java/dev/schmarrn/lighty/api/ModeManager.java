package dev.schmarrn.lighty.api;

import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.ui.screen.LightyConfigScreen;
import dev.schmarrn.lighty.ui.screen.ModeSwitcherScreen;

import java.util.function.Supplier;

/**
 * Used for registering your LightyModes.
 */
public class ModeManager {
    /**
     * Registers a Lighty Mode and adds a Button to enable the mode to the ModeSwitcherScreen.
     * For the Button, you need to specify `modeSwitcher.{id.getNamespace}.{id.getPath}` for the
     * Button Name, and `modeSwitcher.{id.getNamespace}.{id.getPath}.tooltip` for the Tooltip of
     * the Button.
     *
     * @param id Used to generate the translatable text resource locations
     * @param mode Your LightyMode to be registered
     */
    public static void registerMode(String id, LightyMode<?, ?> mode) {
        ModeLoader.put(id, mode);

        ModeSwitcherScreen.addButton(
			id, () -> ModeLoader.loadMode(id)
        );
    }

/*    *//**
     * Adds options to the lighty config screens modes section.
     * This must be called before the {@link net.ornithemc.osl.lifecycle.api.MinecraftEvents#READY} event.
     *//*
    @SafeVarargs
    public static void addOptions(Supplier<OptionsComponent>... components) {
        LightyConfigScreen.addModeOption(components);
    }*/

    private ModeManager() {}
}
