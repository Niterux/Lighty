package dev.schmarrn.lighty.ui.widget;

import dev.schmarrn.lighty.config.ConfigType;
import dev.schmarrn.lighty.ui.screen.OptionsScreen;

public class BooleanWidget extends OptionWidget<Boolean> {

	public BooleanWidget(OptionsScreen optionsScreen, ConfigType<Boolean> option) {
		super(optionsScreen, option);
		setMessage();
	}

	@Override
	public void onClick() {
		option.setValue(!option.getValue());
		setMessage();
	}

	private void setMessage() {
		this.message = option.getValue() ? languageManager.translate("options.on") : languageManager.translate("options.off");
	}
}
