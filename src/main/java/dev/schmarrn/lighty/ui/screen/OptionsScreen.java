package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.ui.widget.ListedWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionsScreen extends Screen {
	private final int WIDGET_VERTICAL_MARGIN = 5;
	private final int WIDGET_RIGHT_MARGIN = 20;
	private final int LIST_MIDDLE_PADDING = 20;
	private final int LIST_START_HEIGHT = 20;
	private final int WIDGET_HEIGHT = 20;
	private final int DESCRIPTION_VERTICAL_ALIGNMENT = WIDGET_HEIGHT / 2 - 4;
	private final ArrayList<ListedWidget> widgetList = new ArrayList<>();
	private final Screen parent;

	public OptionsScreen(Screen parent) {
		this.parent = parent;
		for (ConfigType<?> configType : getOptionList()) {
			ListedWidget widget = configType.getOptionInstance();
			if (widget == null)
				continue;
			widgetList.add(widget);
		}
	}


	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		super.renderBackground();
		super.render(mouseX, mouseY, tickDelta);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for (ListedWidget listedWidget : widgetList) {
			listedWidget.setHovered(mouseX, mouseY);
			listedWidget.render(minecraft);
			String description = Lighty.LANGUAGE_MANAGER.translate(listedWidget.getDescription());
			drawString(textRenderer, description, (width - LIST_MIDDLE_PADDING) / 2 - textRenderer.getWidth(description), listedWidget.y + DESCRIPTION_VERTICAL_ALIGNMENT, 0xFFFFFFFF);
		}
	}

	@Override
	protected void keyPressed(char chr, int key) {
		super.keyPressed(chr, key);
		for (ListedWidget listedWidget : widgetList)
			if (listedWidget.focused)
				listedWidget.onFocusedKeypress(chr, key);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton != 0)
			return;
		for (ListedWidget listedWidget : widgetList) {
			listedWidget.focused = false;
			if (!listedWidget.hovered)
				continue;
			listedWidget.onClick();
			listedWidget.focused = true;
		}
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		super.buttonClicked(button);
		if(button.id == 0)
			minecraft.openScreen(this.parent);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		int currentButtonIndex = 0;
		for (ListedWidget listedWidget : widgetList) {
			listedWidget.x = (width + LIST_MIDDLE_PADDING) / 2;
			listedWidget.height = listedWidget.getPreferredHeight(WIDGET_HEIGHT);
			listedWidget.y = currentButtonIndex * (WIDGET_HEIGHT + WIDGET_VERTICAL_MARGIN) + LIST_START_HEIGHT;
			listedWidget.width = listedWidget.getPreferredWidth((width / 2) - WIDGET_RIGHT_MARGIN);
			currentButtonIndex++;
		}
		ButtonWidget backButton = new ButtonWidget(0, width - 220, height - 40, "Back");
		buttons.add(backButton);
	}

	public abstract List<ConfigType<?>> getOptionList();
}
