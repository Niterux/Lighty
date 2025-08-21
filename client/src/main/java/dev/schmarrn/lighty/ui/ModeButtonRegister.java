package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.ui.widget.ButtonWidget;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;

import java.util.ArrayList;
import java.util.List;

public class ModeButtonRegister {
    static final List<ButtonWidget> BUTTONS = new ArrayList<>();

    public static void addButton(String message, String tooltip, ButtonWidget.ClickAction clickAction) {
        BUTTONS.add(new ButtonWidget(InteractableWidget.WidgetSizes.SMALL, clickAction, message, tooltip));
    }
}
