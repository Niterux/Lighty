package dev.schmarrn.lighty.ui.widget;


import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class ColorWidget extends OptionWidget<Color> {
	public ColorWidget(OptionsScreen optionsScreen, ConfigType<Color> option) {
		super(optionsScreen, option);
	}

	@Override
	public void onClick() {

	}

	@Override
	public void render(Minecraft minecraft, int mouseX, int mouseY) {
		Color value = option.getValue();
		GL11.glColor3b(value.getRedByte(), value.getGreenByte(), value.getBlueByte());
		super.render(minecraft, mouseX, mouseY);
	}
}
