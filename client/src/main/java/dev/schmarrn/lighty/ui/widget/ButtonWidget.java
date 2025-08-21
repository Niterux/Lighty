package dev.schmarrn.lighty.ui.widget;

import net.minecraft.resource.language.I18n;

public class ButtonWidget extends InteractableWidget {
    private final ClickAction onClicked;
    private String message;
    private String tooltip;

    public ButtonWidget(WidgetSizes size, ClickAction onClicked, String message, String tooltip) {
        super(size);
        this.onClicked = onClicked;
        this.message = message;
        this.tooltip = tooltip;
    }

    @Override
    public void render() {
        minecraft.textureManager.bind(minecraft.textureManager.load("/gui/gui.png"));
        int halfWidth = size.pixelWidth / 2;
        int renderedTextY = getWidgetHeight() / 2 + yPos - 4;
        int buttonVCoordinate = mouseOver ? 86 : 66;
        drawTexture(xPos, yPos, 0, buttonVCoordinate, halfWidth, 20);
        drawTexture(xPos + halfWidth, yPos, 200 - halfWidth, buttonVCoordinate, halfWidth, 20);
        drawCenteredString(minecraft.textRenderer, I18n.translate(message), xPos + halfWidth, renderedTextY, 0xFFFFFFFF);
    }

    @Override
    public void onMouseDown() {
        super.onMouseDown();
        onClicked.onClicked();
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {
        super.onMouseMoved(mouseX, mouseY);
        renderTooltip(mouseX, mouseY, tooltip);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @FunctionalInterface
    public interface ClickAction {
        void onClicked();
    }
}
