package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.LanguageManager;

public abstract class OptionWidget<T> extends ButtonWidget {
	public final ConfigType<T> option;
	public static final LanguageManager languageManager = LanguageManager.getInstance();
	public OptionWidget(OptionsScreen optionsScreen, ConfigType<T> option) {
		super(optionsScreen.getNextButtonId(), optionsScreen.getWidgetX(), optionsScreen.getWidgetY(), optionsScreen.getWidgetWidth(), optionsScreen.getWidgetHeight(), "");
		this.option = option;
	}

	abstract void onClick();
}
