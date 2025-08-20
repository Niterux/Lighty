package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.BooleanConfig;

public class BooleanButtonWidget extends ButtonWidget {
    public BooleanButtonWidget(WidgetSizes size, BooleanConfig booleanConfig, String message, String tooltip) {
        super(size, () -> booleanConfig.setValue(!booleanConfig.getValue()), message, tooltip);
    }

    private String getOnOff() {
    }
}
