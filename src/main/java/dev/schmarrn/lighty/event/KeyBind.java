package dev.schmarrn.lighty.event;

import dev.schmarrn.lighty.SMACH;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import dev.schmarrn.lighty.ui.ModeSwitcherScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.options.KeyBinding;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

public class KeyBind {
	public static KeyBinding enable;
	public static KeyBinding toggle;

	public static void callback(int keyCode) {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		@Nullable Screen currentScreen = minecraft.screen;

		if(keyCode == enable.keyCode && currentScreen == null)
			minecraft.openScreen(new ModeSwitcherScreen());
		if(keyCode == enable.keyCode && currentScreen == null)
			SMACH.toggle();

	}

	public static void init() {
		enable = new KeyBinding("key.lighty.enable", Keyboard.KEY_F6);
		toggle = new KeyBinding("key.lighty.toggle", Keyboard.KEY_F7);
	}

	public static void register() {
		GameSettings.keys.add(enable);
		GameSettings.keys.add(toggle);

		GameOptionss.CONTROLS
			.withComponent(new OptionsCategory("category.lighty")
				.withComponent(new KeyBindingComponent(enable))
				.withComponent(new KeyBindingComponent(toggle)));
	}
}
