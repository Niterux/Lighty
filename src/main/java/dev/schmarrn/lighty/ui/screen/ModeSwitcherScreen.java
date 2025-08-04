package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.SMACH;
import dev.schmarrn.lighty.config.Mode;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.LanguageManager;

import java.util.ArrayList;
import java.util.List;

public class ModeSwitcherScreen extends Screen {
	private static final List<Mode> MODES = new ArrayList<>();

	private static final int START_HEIGHT = 15;
	private static final int DELTA_HEIGHT = 24;
	private static final LanguageManager languageManager = LanguageManager.getInstance();

	@Override
	public void init() {
		int height = START_HEIGHT + DELTA_HEIGHT;

		buttons.clear();
		buttons.add(new ButtonWidget(0, this.width/2 - 75, height, 150, 20, getStatus()));

		height += 6;
		int index = 0;
		for (Mode mode : MODES) {
			height += DELTA_HEIGHT;
			buttons.add(new ButtonWidget(index + 2, this.width / 2 - 75, height, 150, 20, languageManager.translate(mode.title())));
			index++;
		}

		height += DELTA_HEIGHT;
		buttons.add(new ButtonWidget(1, this.width/2 - 75, height + 6, 150, 20, languageManager.translate("gui.lighty.modeSwitcher.done")));
	}

	private String getStatus() {
		return languageManager.translate(
			"gui.lighty.modeSwitcher.toggle",
			(SMACH.isEnabled()) ? languageManager.translate("gui.lighty.modeSwitcher.on") : languageManager.translate("gui.lighty.modeSwitcher.off")
		);
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		renderBackground();
		drawCenteredString(MinecraftInstanceAccessor.getMinecraft().textRenderer, languageManager.translate("gui.lighty.modeSwitcher.title"), this.width/2, START_HEIGHT, 0xFFFFFF);
		super.render(mx, my, partialTick);
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		if (button.active) {
			if (button.id == 0) {
				SMACH.toggle();
				buttons.get(0).message = getStatus();
			} else if (button.id == 1) {
				minecraft.openScreen(null);
			} else {
				Mode mode = MODES.get(button.id - 2);
				mode.onPress().run();
				buttons.get(0).message = getStatus();
			}
		}
	}


	public static void addButton(String title, Runnable onPress) {
		MODES.add(new Mode(title, onPress));
	}
}
