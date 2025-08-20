package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.widget.BooleanButtonWidget;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;

public class BooleanConfig extends ConfigType<Boolean> {
    @Override
    public BooleanButtonWidget createAssociatedWidget(InteractableWidget.WidgetSizes size) {
        return new BooleanButtonWidget(size, this, this.getTranslationKey(), this.getTranslationTooltipKey());
    }

    public BooleanConfig(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    String serialize() {
        return Boolean.toString(getValue());
    }

    @Override
    void deserialize(String value) {
        setValue(Boolean.valueOf(value));
    }
}
