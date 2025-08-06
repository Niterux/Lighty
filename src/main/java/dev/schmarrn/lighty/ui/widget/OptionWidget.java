package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;

public abstract class OptionWidget<T> extends ListedWidget {
	public final ConfigType<T> option;

	public OptionWidget(ConfigType<T> option) {
		super(option.getTranslationKey());
		this.option = option;
	}
}
