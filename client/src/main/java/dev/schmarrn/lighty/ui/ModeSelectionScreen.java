package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.ui.widget.ButtonWidget;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;
import net.minecraft.client.gui.screen.Screen;

public class ModeSelectionScreen extends WidgetScreen {
    private final ButtonWidget doneButton;

    protected ModeSelectionScreen(Screen parent) {
        super("lighty.overlay.title", parent);
        doneButton = new ButtonWidget(InteractableWidget.WidgetSizes.SMALL, this::removed, "Done", null);
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        super.render(mouseX, mouseY, tickDelta);
    }

    @Override
    public void init() {
        for (var btn : ModeButtonRegister.BUTTONS) {
            btn.init(width, height);
        }
    }

    @Override
    protected int applyWidgetXPosition(int widgetIndex) {
        return 0;
    }

    @Override
    protected int applyWidgetYPosition(int widgetIndex) {
        return 0;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
