package dev.schmarrn.lighty.ui.widget;


import dev.schmarrn.lighty.config.ConfigType;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class ColorWidget extends OptionWidget<Color> {
	TextBoxWidget<Color> textBoxWidget;
	public ColorWidget(ConfigType<Color> option) {
		super(option);
		textBoxWidget = new TextBoxWidget<>(option);
	}

	@Override
	public void onClick() {

	}

	@Override
	public void render(Minecraft minecraft) {
		Color value = option.getValue();
		GL11.glColor3b(value.getRedByte(), value.getGreenByte(), value.getBlueByte());
	}
}
