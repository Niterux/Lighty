package dev.schmarrn.lighty.event;

import dev.schmarrn.lighty.SMACH;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import dev.schmarrn.lighty.ui.screen.ModeSwitcherScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyBind {
	public static KeyBinding enable;
	public static KeyBinding toggle;

	public static void callback(int keyCode) {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		if (keyCode == enable.keyCode)
			minecraft.openScreen(new ModeSwitcherScreen());
		if (keyCode == toggle.keyCode)
			SMACH.toggle();
	}

	public static void init() {
		enable = new KeyBinding("key.lighty.enable", Keyboard.KEY_F6);
		toggle = new KeyBinding("key.lighty.toggle", Keyboard.KEY_F7);
	}

	public static void register() {
/*		GameSettings.keys.add(enable);
		GameSettings.keys.add(toggle);

		GameOptionss.CONTROLS
			.withComponent(new OptionsCategory("category.lighty")
				.withComponent(new KeyBindingComponent(enable))
				.withComponent(new KeyBindingComponent(toggle)));*/
	}
}
