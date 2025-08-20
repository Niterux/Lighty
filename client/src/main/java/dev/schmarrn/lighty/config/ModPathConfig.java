package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.api.ModPath;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;

public class ModPathConfig extends ConfigType<ModPath> {
    @Override
    public InteractableWidget createAssociatedWidget(InteractableWidget.WidgetSizes size) {
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
