package dev.schmarrn.lighty.ui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModeButtonRegister {
    static final List<ButtonHolder> BUTTONS = new ArrayList<>();

    public static void addButton(String message, String tooltip, Button.OnPress onPress) {
        BUTTONS.add(new ButtonHolder(Button.builder(message, onPress).tooltip(Tooltip.create(tooltip))));
    }

    static class ButtonHolder implements Supplier<Button> {
        private final Button.Builder builder;
        private Button button = null;
        ButtonHolder(Button.Builder builder) {
            this.builder = builder;
        }

        @Override
        public Button get() {
            if (button == null) {
                button = builder.build();
            }
            return button;
        }
    }
}
