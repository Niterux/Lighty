package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.mixin.invokers.DrawInvokers;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import net.minecraft.client.Minecraft;

import java.text.MessageFormat;

public class TextBoxWidget<T> extends OptionWidget<T> {
	private boolean selected = false;
	public TextBoxWidget(OptionsScreen optionsScreen, ConfigType<T> option) {
		super(optionsScreen, option);
	}

	@Override
	public void onClick() {
		System.out.println("CLICKED!");
		this.selected = true;
	}

	@Override
	public void render(Minecraft minecraft, int mouseX, int mouseY) {
		fill(x, y, width + x, height + y, 0xFFFFFFFF);
		fill(x + 1, y + 1, width + x - 1, height + y - 1, 0xFF000000);
		if (selected)
			drawVerticalLine(x + 3, y + 3, y + height - 3, 0xFFFFFFFF);
	}
}
