package dev.schmarrn.lighty.ui.screen;

import net.minecraft.client.gui.screen.Screen;

public abstract class OptionsScreen extends Screen {
	private int currentButtonId;
	public int getNextButtonId() {
		return currentButtonId++;
	}
	public int getCurrentButtonId() {
		return currentButtonId;
	}
	public int getWidgetX(int id) {
		return width / 2;
	}
	public int getWidgetY(int id) {
		return id * 20;
	}
	public int getWidgetWidth(int id) {
		return 200;
	}
	public int getWidgetHeight(int id) {
		return 20;
	}
	public int getWidgetX() {
		return getWidgetX(getCurrentButtonId());
	}
	public int getWidgetY() {
		return getWidgetY(getCurrentButtonId());
	}
	public int getWidgetWidth() {
		return getWidgetWidth(getCurrentButtonId());
	}
	public int getWidgetHeight() {
		return getWidgetHeight(getCurrentButtonId());
	}
}
