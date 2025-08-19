package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.Lighty;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resource.language.I18n;
import org.jetbrains.annotations.NotNull;

public class ModeSelectionScreen extends Screen {
    private final Screen parent;
    private final String title;
    protected ModeSelectionScreen(Screen parent) {
        this.title = "lighty.overlay.title";
        this.parent = parent;
    }

    @Override
    public void init() {
        GridLayout gridWidget = new GridLayout();
        gridWidget.defaultCellSetting().paddingBottom(4).alignHorizontallyCenter().alignVerticallyMiddle();
        GridLayout.RowHelper adder = gridWidget.createRowHelper(1);

        for (var btn : ModeButtonRegister.BUTTONS) {
            adder.addChild(btn.get());
        }

        adder.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).build(), adder.newCellSettings().paddingTop(6));
        gridWidget.arrangeElements();
        FrameLayout.alignInRectangle(gridWidget, 0, this.height/6 - 12, this.width, this.height, 0.5f, 0f);
        gridWidget.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        super.render(mouseX, mouseY, tickDelta);
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
