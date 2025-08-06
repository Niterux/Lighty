package dev.schmarrn.lighty.ui.screen;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.SMACH;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.LanguageManager;

import java.util.List;

public class ModeSwitcherScreen extends Screen {
	private static final List<ObjectObjectImmutablePair<String, Runnable>> MODES = new ReferenceArrayList<>();

	private static final int START_HEIGHT = 15;
	private static final int DELTA_HEIGHT = 24;
	@Override
	public void init() {
		int height = START_HEIGHT + DELTA_HEIGHT;

		buttons.clear();
		buttons.add(new ButtonWidget(0, this.width/2 - 75, height, 150, 20, getStatus()));

		height += 6;
		int index = 0;
		for (ObjectObjectImmutablePair<String, Runnable> mode : MODES) {
			height += DELTA_HEIGHT;
			buttons.add(new ButtonWidget(index + 2, this.width / 2 - 75, height, 150, 20, Lighty.LANGUAGE_MANAGER.translate("gui.modeSwitcher." + mode.left())));
			index++;
		}

		height += DELTA_HEIGHT;
		buttons.add(new ButtonWidget(1, this.width/2 - 75, height + 6, 150, 20, Lighty.LANGUAGE_MANAGER.translate("gui.lighty.modeSwitcher.done")));
	}

	private String getStatus() {
		return Lighty.LANGUAGE_MANAGER.translate(
			"gui.lighty.modeSwitcher.toggle",
			(SMACH.isEnabled()) ? Lighty.LANGUAGE_MANAGER.translate("gui.lighty.modeSwitcher.on") : Lighty.LANGUAGE_MANAGER.translate("gui.lighty.modeSwitcher.off")
		);
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		renderBackground();
		drawCenteredString(MinecraftInstanceAccessor.getMinecraft().textRenderer, Lighty.LANGUAGE_MANAGER.translate("gui.lighty.modeSwitcher.title"), this.width/2, START_HEIGHT, 0xFFFFFF);
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
				MODES.get(button.id - 2).right().run();
				buttons.get(0).message = getStatus();
			}
		}
	}

	public static void addButton(String id, Runnable onPress) {
		MODES.add(new ObjectObjectImmutablePair<>(id, onPress));
	}
}
