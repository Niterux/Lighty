package dev.schmarrn.lighty.config;

import dev.schmarrn.lighty.ui.widget.InteractableWidget;

abstract class ConfigType<T> extends ConfigSerDe {
    private final T DEFAULT_VALUE;
    private final String KEY;
    private T value;

    ConfigType(String key, T defaultValue) {
        this.KEY = key;
        this.DEFAULT_VALUE = defaultValue;
        this.value = defaultValue;

        Config.register(key, this);
    }

    public void onChange(T value) {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        value = newValue;
        onChange(newValue);
        Config.save();
    }

    public void resetToDefault() {
        value = DEFAULT_VALUE;
    }

    public String getKey() {
        return KEY;
    }

    public abstract InteractableWidget createAssociatedWidget(InteractableWidget.WidgetSizes size);

    protected String getTranslationKey() {
        return KEY.replaceFirst("\\.", ".options.");
    }

    protected String getTranslationTooltipKey() {
        return getTranslationKey() + ".tooltip";
    }
}
