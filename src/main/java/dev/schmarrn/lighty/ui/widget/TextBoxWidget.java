package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.config.GetSetStringConfig;
import net.minecraft.client.Minecraft;

public class TextBoxWidget<T> extends OptionWidget<T> {
	public TextBoxWidget(GetSetStringConfig<T> option) {
		super(option);
	}

	@Override
	public void onClick() {
		System.out.println("CLICKED!");
	}

	@Override
	public void render(Minecraft minecraft) {
		fill(x, y, width + x, height + y, 0xFFFFFFFF);
		fill(x + 1, y + 1, width + x - 1, height + y - 1, 0xFF000000);
		String content = ((GetSetStringConfig<T>)this.option).getString();
		drawString(minecraft.textRenderer, content, x + 3, height / 2 + y - 4, 0xFFFFFFFF);
		if (!focused) {
			return;
		}
		boolean showing = (System.currentTimeMillis() % 2000 > 1000);
		if (!showing) {
			return;
		}
		int contentsWidth = minecraft.textRenderer.getWidth(content);
		drawVerticalLine(x + 3 + contentsWidth, y + 3, y + height - 3, 0xFFFFFFFF);
	}

	@Override
	public void onFocusedKeypress(char chr, int key) {
		GetSetStringConfig<T> option = (GetSetStringConfig<T>)this.option;
		option.setString(option.getString() + chr);
	}
}
