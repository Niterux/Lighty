package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.ui.widget.ButtonWidget;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.language.I18n;

public class ModeSelectionScreen extends Screen {
    private final Screen parent;
    private final String title;
    private final ButtonWidget doneButton;
    protected ModeSelectionScreen(Screen parent) {
        this.title = "lighty.overlay.title";
        this.parent = parent;
        doneButton = new ButtonWidget(InteractableWidget.WidgetSizes.SMALL, this::removed, "Done", null);
    }

    @Override
    public void init() {
        for (var btn : ModeButtonRegister.BUTTONS) {
            btn.init(width, height);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        super.render(mouseX, mouseY, tickDelta);
        for (var btn : ModeButtonRegister.BUTTONS) {
            //test
            btn.render(0, 0);
        }
        this.drawCenteredString(this.textRenderer, I18n.translate(this.title), this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.openScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
