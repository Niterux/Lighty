package dev.schmarrn.lighty.ui;

import net.minecraft.client.gui.screen.Screen;

public class ControlsScreen extends WidgetScreen{
    public ControlsScreen(String title, Screen parent) {
        super(title, parent);
    }

    @Override
    protected int applyWidgetXPosition(int widgetIndex) {
        return width / 2 + 50;
    }

    @Override
    protected int applyWidgetYPosition(int widgetIndex) {
        return widgetIndex * 25 + 100;
    }
}
