// Copyright 2022-2023 The Lighty contributors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.core.Compute;
import dev.schmarrn.lighty.ui.widget.InteractableWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.language.I18n;

public class SettingsScreen extends Screen {
    private final Screen parent;
    private final String title;
    public SettingsScreen(Screen parent) {
        this.parent = parent;
        this.title = "settings.lighty.title";
    }

    @Override
    protected void addOptions() {
        this.list.addBig(Config.OVERLAY_DISTANCE.createAssociatedWidget(InteractableWidget.WidgetSizes.BIG));
        this.list.addBig(Config.OVERLAY_BRIGHTNESS.createAssociatedWidget(InteractableWidget.WidgetSizes.BIG));
        this.list.addSmall(
                Config.BLOCK_THRESHOLD.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL),
                Config.SKY_THRESHOLD.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL)
        );
        this.list.addBig(Config.FARM_GROWTH_THRESHOLD.createAssociatedWidget(InteractableWidget.WidgetSizes.BIG));
        this.list.addBig(Config.FARM_UPROOT_THRESHOLD.createAssociatedWidget(InteractableWidget.WidgetSizes.BIG));
        this.list.addSmall(
                Config.SHOW_SAFE.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL),
                Config.SHOW_SKYLIGHT_LEVEL.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL)
        );
        this.list.addSmall(
                Config.SHOULD_AUTO_ON.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL),
                Config.CHUNKS_PER_TICK.createAssociatedWidget(InteractableWidget.WidgetSizes.SMALL)
        );
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        super.render(mouseX, mouseY, tickDelta);
        this.drawCenteredString(this.textRenderer, I18n.translate(this.title), this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public void removed() {
        Compute.clear();
        assert this.minecraft != null;
        this.minecraft.openScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
