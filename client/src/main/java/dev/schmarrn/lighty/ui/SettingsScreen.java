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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resource.language.I18n;

public class SettingsScreen extends Screen {
    private final Screen parent;
    private final String title;
    public SettingsScreen(Screen parent) {
        // passing null to options is fine, because it is only used in the respective OptionsSubScreens,
        // and we don't use it here
        //noinspection DataFlowIssue
        this.parent = parent;
        this.title = "settings.lighty.title";
    }

    @Override
    protected void addOptions() {
        this.list.addBig(Config.OVERLAY_DISTANCE.createAssociatedWidget());
        this.list.addBig(Config.OVERLAY_BRIGHTNESS.createAssociatedWidget());
        this.list.addSmall(
                Config.BLOCK_THRESHOLD.createAssociatedWidget(),
                Config.SKY_THRESHOLD.createAssociatedWidget()
        );
        this.list.addBig(Config.FARM_GROWTH_THRESHOLD.createAssociatedWidget());
        this.list.addBig(Config.FARM_UPROOT_THRESHOLD.createAssociatedWidget());
        this.list.addSmall(
                Config.SHOW_SAFE.createAssociatedWidget(),
                Config.SHOW_SKYLIGHT_LEVEL.createAssociatedWidget()
        );
        this.list.addSmall(
                Config.SHOULD_AUTO_ON.createAssociatedWidget(),
                Config.CHUNKS_PER_TICK.createAssociatedWidget()
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
