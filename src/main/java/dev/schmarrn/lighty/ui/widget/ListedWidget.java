package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;

public abstract class ListedWidget extends GuiElement {
	public int x, y, width, height;
	public boolean hovered = false;
	public boolean focused = false;
	String description = "";

	public ListedWidget(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setHovered(int x, int y) {
		hovered = false;
		if (x > this.x + width)
			return;
		if (x < this.x)
			return;
		if (y > this.y + height)
			return;
		if (y < this.y)
			return;
		hovered = true;
	}

	public abstract void onClick();

	public abstract void render(Minecraft minecraft);

	public int getPreferredWidth(int listWidth) {
		return listWidth;
	}

	public int getPreferredHeight(int defaultWidgetHeight) {
		return defaultWidgetHeight;
	}

	public void playClickingSound() {
		MinecraftInstanceAccessor.getMinecraft().soundSystem.play("random.click", 1.0F, 1.0F);
	}

	public void onFocusedKeypress(char chr, int key) {}
}
