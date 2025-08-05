package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.mixin.invokers.DrawInvokers;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.LanguageManager;

public abstract class OptionWidget<T> extends ButtonWidget {
	public static final LanguageManager languageManager = LanguageManager.getInstance();
	public final ConfigType<T> option;

	public OptionWidget(OptionsScreen optionsScreen, ConfigType<T> option) {
		super(optionsScreen.getNextButtonId(), optionsScreen.getWidgetX(), optionsScreen.getWidgetY(), optionsScreen.getWidgetWidth(), optionsScreen.getWidgetHeight(), "");
		this.option = option;
	}

	public abstract void onClick();
}
