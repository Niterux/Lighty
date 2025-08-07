package dev.schmarrn.lighty.mode;

import dev.schmarrn.lighty.config.Config;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.Color;

public enum ColorEnum {
	SAFE,
	DAYLIGHT_SAFE,
	UNSAFE,
	NONE;

	public @Nullable Color getColor() {
		switch (this) {
			case SAFE:
				return Config.OVERLAY_GREEN.getValue();
			case DAYLIGHT_SAFE:
				return Config.OVERLAY_ORANGE.getValue();
			case UNSAFE:
				return Config.OVERLAY_RED.getValue();
			default:
				return null;
		}
	}
}
