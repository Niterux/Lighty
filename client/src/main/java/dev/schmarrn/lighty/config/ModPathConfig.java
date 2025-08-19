package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.api.ModPath;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.ResourceLocation;

public class ModPathConfig extends ConfigType<ModPath> {
    @Override
    public OptionInstance<ModPath> getOptionInstance() {
        return null;
    }

    public ModPathConfig(String key, ModPath defaultValue) {
        super(key, defaultValue);
    }

    @Override
    String serialize() {
        return getValue().toString();
    }

    @Override
    void deserialize(String value) {
        setValue(ModPath.parse(value));
    }
}
