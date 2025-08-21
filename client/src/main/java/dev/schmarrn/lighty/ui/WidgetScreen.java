package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.ui.widget.InteractableWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;

public abstract class WidgetScreen extends Screen {
    private final String title;
    private final Screen parent;
    protected List<InteractableWidget> widgetList = new ArrayList<>();
    private int lastMouseX = 0;
    private int lastMouseY = 0;

    public WidgetScreen(String title, Screen parent) {
        this.title = I18n.translate(title);
        this.parent = parent;
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        super.render(mouseX, mouseY, tickDelta);
        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        setMousePositionOnWidgets(mouseX, mouseY);
        renderWidgets();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        setMousePositionOnWidgets(mouseX, mouseY);
        if (mouseButton == 0)
            handleMouseDownOnWidgets();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0)
            handleMouseUpOnWidgets();
    }

    @Override
    public void init() {
        super.init();
        setWidgetsPositions();
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.openScreen(parent);
    }

    private void setWidgetsPositions() {
        for (int i = 0; i < widgetList.size(); i++) {
            widgetList.get(i).setWidgetPosition(applyWidgetXPosition(i), applyWidgetYPosition(i));
            setMousePositionOnWidgets(lastMouseX, lastMouseY);
        }
    }

    protected abstract int applyWidgetXPosition(int widgetIndex);

    protected abstract int applyWidgetYPosition(int widgetIndex);

    private void handleMouseUpOnWidgets() {
        for (InteractableWidget widget : widgetList)
            widget.onMouseUp();
    }

    private void handleMouseDownOnWidgets() {
        for (InteractableWidget widget : widgetList)
            widget.onMouseDown();
    }

    private void setMousePositionOnWidgets(int mouseX, int mouseY) {
        if (mouseX != lastMouseX || mouseY != lastMouseY)
            for (InteractableWidget widget : widgetList) {
                widget.setMouseOver(isHoveringWidget(widget, mouseX, mouseY));
                widget.onMouseMoved(mouseX, mouseY);
            }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    private void renderWidgets() {
        for (InteractableWidget widget : widgetList)
            widget.render();
    }

    protected boolean isHoveringWidget(InteractableWidget widget, int mouseX, int mouseY) {
        int x = widget.getxPos();
        int y = widget.getyPos();
        //noinspection RedundantIfStatement
        if (x <= mouseX && x + widget.getSize().pixelWidth >= mouseX && y <= mouseY && y + widget.getWidgetHeight() >= mouseY)
            return true;
        return false;
    }
}
