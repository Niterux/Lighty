package dev.schmarrn.lighty.core;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.ModPath;
import dev.schmarrn.lighty.api.OverlayRenderer;
import dev.schmarrn.lighty.config.Config;

import java.util.HashMap;

public class RendererRegistry {
    private static final HashMap<ModPath, OverlayRenderer> RENDERERS = new HashMap<>();

    private static OverlayRenderer renderer;

    public static void put(ModPath modPath, OverlayRenderer renderer) {
        RENDERERS.put(modPath, renderer);
    }

    public static OverlayRenderer getRenderer() {
        return renderer;
    }

    public static void loadRenderer(ModPath modPath) {
        OverlayRenderer renderer = RENDERERS.get(modPath);

        if (renderer == null) {
            Lighty.LOGGER.error("Could not find renderer with id {}! Not changing renderer.", modPath);
            return;
        }

        RendererRegistry.renderer = renderer;
        Config.LAST_USED_RENDERER.setValue(modPath);
        Compute.clear();
    }

    /**
     * Needs to be called AFTER registering all the different Lighty renderers.
     * If the requested renderer isn't loaded, default to the first registered mode.
     */
    public static void setLastUsedRenderer() {
        renderer = RENDERERS.getOrDefault(Config.LAST_USED_RENDERER.getValue(), RENDERERS.values().iterator().next());
    }
}
