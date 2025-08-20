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

package dev.schmarrn.lighty.api;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.core.DataProviderRegistry;
import dev.schmarrn.lighty.core.RendererRegistry;
import dev.schmarrn.lighty.ui.ModeButtonRegister;
import net.minecraft.resource.language.I18n;

import java.text.MessageFormat;

/**
 * Used for registering your LightyModes.
 */
public class ModeManager {
    private ModeManager() {
    }

    /**
     * Registers a Lighty OverlayDataProvider.
     * <p>
     * Use the LightyModesRegistration EntryPoint
     *
     * @param modPath      Used to generate the translatable text resource locations
     * @param dataProvider Your OverlayDataProvider to be registered
     */
    public static void registerDataProvider(ModPath modPath, OverlayDataProvider dataProvider) {
        DataProviderRegistry.put(modPath, dataProvider);
    }

    /**
     * Registers a Lighty OverlayRenderer and adds a Button to enable the renderer to the ModeSwitcherScreen.
     * For the Button, you need to specify `modeSwitcher.{id.getNamespace}.{id.getPath}` for the
     * Button Name, and `modeSwitcher.{id.getNamespace}.{id.getPath}.tooltip` for the Tooltip of
     * the Button.
     * <p>
     * Use the LightyModesRegistration EntryPoint
     *
     * @param modPath  Used to generate the translatable text resource locations
     * @param renderer Your OverlayRenderer to be registered
     */
    public static void registerRenderer(ModPath modPath, OverlayRenderer renderer) {
        RendererRegistry.put(modPath, renderer);

        ModeButtonRegister.addButton(
                I18n.translate(MessageFormat.format("modeSwitcher.{0}.{1}", modPath.getModId(), modPath.getPath())),
                I18n.translate(MessageFormat.format("modeSwitcher.{0}.{1}.tooltip", modPath.getModId(), modPath.getPath())), () -> RendererRegistry.loadRenderer(modPath)
        );
    }
}
