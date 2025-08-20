package dev.schmarrn.lighty.core;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.ModPath;
import dev.schmarrn.lighty.api.OverlayDataProvider;
import dev.schmarrn.lighty.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataProviderRegistry {
    private static final HashMap<ModPath, OverlayDataProvider> DATA_PROVIDERS = new HashMap<>();

    private static final List<OverlayDataProvider> ACTIVE_PROVIDERS = new ArrayList<>();

    public static void put(ModPath rl, OverlayDataProvider dataProvider) {
        DATA_PROVIDERS.put(rl, dataProvider);
    }

    private static void updateConfig() {
        ArrayList<ModPath> activeRls = new ArrayList<>();
        DATA_PROVIDERS.forEach((key, value) -> {
            if (ACTIVE_PROVIDERS.contains(value)) {
                activeRls.add(key);
            }
        });
        Config.ACTIVE_DATA_PROVIDERS.setValue(activeRls);
    }

    public static void activate(ModPath rl) {
        if (DATA_PROVIDERS.containsKey(rl)) {
            ACTIVE_PROVIDERS.add(DATA_PROVIDERS.get(rl));
        } else {
            Lighty.LOGGER.error("There is no OverlayDataProvider registered for {}! Not changing active OverlayDataProviders.", rl);
        }
        updateConfig();
    }

    public static void deactivate(ModPath modPath) {
        if (DATA_PROVIDERS.containsKey(modPath)) {
            ACTIVE_PROVIDERS.remove(DATA_PROVIDERS.get(modPath));
        } else {
            Lighty.LOGGER.error("There is no OverlayDataProvider registered for {}! Cannot remove {}.", modPath, modPath);
        }
        updateConfig();
    }

    public static void setLastActiveProviders() {
        for (var modPath : Config.ACTIVE_DATA_PROVIDERS.getValue()) {
            activate(modPath);
        }
    }

    public static List<OverlayDataProvider> getActiveProviders() {
        return ACTIVE_PROVIDERS;
    }
}
