package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.ui.widget.OptionWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		super.renderBackground();
		super.render(mouseX, mouseY, tickDelta);
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		super.buttonClicked(button);
		if(button instanceof OptionWidget)
			((OptionWidget<?>)button).onClick();
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		currentButtonId = 0;
		for(ConfigType<?> configType : getOptionList()) {
			OptionWidget thing = configType.getOptionInstance(this);
			if (thing != null)
				buttons.add(thing);
		}
	}
	public abstract List<ConfigType<?>> getOptionList();
}
