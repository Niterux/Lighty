package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.TextRenderer;

public abstract class InteractableWidget extends GuiElement {
    private static int TOOLTIP_PADDING = 3;
    protected final WidgetSizes size;
    protected final Minecraft minecraft;
    protected final TextRenderer textRenderer;
    protected int screenWidth;
    protected int screenHeight;
    protected boolean mouseOver = false;
    protected boolean focused = false;

    protected InteractableWidget(WidgetSizes size) {
        this.size = size;
        this.minecraft = MinecraftInstanceAccessor.getMinecraft();
        this.textRenderer = minecraft.textRenderer;
    }

    public void init(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    ;

    abstract public void render(int x, int y);

    public void onMouseDown() {
    }

    public void onMouseUp() {
    }

    public void onMouseMoved(int mouseX, int mouseY) {
    }

    public void setMouseOver(boolean highlighted) {
        this.mouseOver = highlighted;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getWidgetHeight() {
        return 20;
    }

    protected void renderTooltip(int mouseX, int mouseY, String tooltip) {
        int tooltipWidth = 0, tooltipPositionX = mouseX + 9, tooltipPositionY;
        String[] tooltipLines = tooltip.split("<br>");
        tooltipPositionY = tooltipLines.length * -8 + mouseY - 7;
        for (String tooltipLine : tooltipLines)
            tooltipWidth = Math.max(textRenderer.getWidth(tooltipLine), tooltipWidth);
        tooltipWidth += TOOLTIP_PADDING * 2;
        tooltipPositionX = Math.min(screenWidth - tooltipWidth, tooltipPositionX);

        //noinspection SuspiciousNameCombination
        fill(
                tooltipPositionX,
                tooltipPositionY,
                tooltipPositionX + tooltipWidth,
                tooltipLines.length * 8 + tooltipPositionY + (TOOLTIP_PADDING * 2),
                0xC0000000);
        for (int i = 0; i < tooltipLines.length; i++) {
            textRenderer.drawWithShadow(tooltipLines[i],
                    tooltipPositionX + TOOLTIP_PADDING,
                    i * 8 + tooltipPositionY + TOOLTIP_PADDING,
                    0xFFFFFFFF);
        }
    }

    public WidgetSizes getSize() {
        return this.size;
    }

    public enum WidgetSizes {
        BIG(310),
        SMALL(150);
        public final int pixelWidth;

        WidgetSizes(int pixelWidth) {
            this.pixelWidth = pixelWidth;
        }
    }
}

