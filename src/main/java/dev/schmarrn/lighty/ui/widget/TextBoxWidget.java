package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.config.DeserializationException;
import net.minecraft.client.Minecraft;

import java.time.Instant;
import java.time.InstantSource;

public class TextBoxWidget<T> extends OptionWidget<T> {
	private String contents;
	private boolean validContents = true;
	private long latestInteractionTime;

	public TextBoxWidget(ConfigType<T> option) {
		super(option);
		contents = option.serialize();
	}

	@Override
	public void onClick() {
		latestInteractionTime = Instant.now().toEpochMilli();
	}

	@Override
	public void render(Minecraft minecraft) {
		fill(x, y, width + x, height + y, 0xFFFFFFFF);
		fill(x + 1, y + 1, width + x - 1, height + y - 1, 0xFF000000);
		int textColor = validContents ? 0xFFFFFFFF : 0xFFFF0000;
		drawString(minecraft.textRenderer, contents, x + 3, height / 2 + y - 4, textColor);
		if (!focused) {
			return;
		}
		boolean showing = ((Instant.now().toEpochMilli() - latestInteractionTime) % 1500 < 750);
		if (!showing) {
			return;
		}
		int contentsWidth = minecraft.textRenderer.getWidth(contents);
		drawVerticalLine(x + 3 + contentsWidth, y + 3, y + height - 3, textColor);
	}

	@Override
	public void onFocusedKeypress(char chr, int key) {
		latestInteractionTime = Instant.now().toEpochMilli();
		if (Character.getNumericValue(chr) != -1)
			contents = contents + chr;
		if (key == 14 && !contents.isEmpty())
			contents = contents.substring(0, contents.length() - 1);
		validContents = true;
		try {
			option.setValue(option.deserialize(contents));
		} catch (DeserializationException e) {
			validContents = false;
		}
	}
}
