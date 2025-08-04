package dev.schmarrn.lighty.ui;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.event.Compute;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.OptionsComponent;
import net.minecraft.client.gui.options.components.ShortcutComponent;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.helper.FileOpener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LightyConfigScreen extends Screen {

	private static final List<GameOptions> OPTION_PAGES = new ArrayList<>();
	private static final List<Supplier<OptionsComponent>> MODE_OPTIONS = new ArrayList<>();

	public LightyConfigScreen(Screen parent) {
		super();
	}

	@Override
	public void removed() {
		for (GameOptions optionPage : OPTION_PAGES)
			for (OptionsComponent component : optionPage.getComponents())
				component.onClose();

		super.removed();
		Config.save();
		Compute.markDirty();
	}

	@Override
	public void mouseReleased(int mx, int my, int buttonNum) {
		super.mouseReleased(mx, my, buttonNum);
		Compute.markDirty();
	}

	public static GameOptions getDefaultPage() {
		return OPTION_PAGES.get(0);
	}

	public static List<GameOptions> getPages() {
		return OPTION_PAGES;
	}

	public static void register() {
		OPTION_PAGES.add(new GameOptions("gui."+Lighty.MOD_ID+".options.general.title", new ItemStack(Block.TORCH))
			.withComponent(new OptionsCategory("gui."+ Lighty.MOD_ID+".options.toggle")
				.withComponent(Config.SHOW_SAFE.getOptionInstance())
				.withComponent(Config.SHOULD_AUTO_ON.getOptionInstance())
			)
			.withComponent(new OptionsCategory("gui."+ Lighty.MOD_ID+".options.visual")
				.withComponent(Config.OVERLAY_DISTANCE.getOptionInstance())
				.withComponent(Config.OVERLAY_BRIGHTNESS.getOptionInstance())
				.withComponent(Config.OVERLAY_TRANSPARENCY.getOptionInstance())
			)
			.withComponent(new OptionsCategory("gui."+ Lighty.MOD_ID+".options.advanced")
				.withComponent(Config.SKY_THRESHOLD.getOptionInstance())
				.withComponent(Config.BLOCK_THRESHOLD.getOptionInstance())
				.withComponent(new ShortcutComponent("gui.lighty.options.page.advanced.button.open_folder", () -> FileOpener.open(new File(FabricLoader.getInstance().getConfigDir().toString(), Lighty.MOD_ID))))
			)
		);

		GameOptions page = new GameOptions("gui."+Lighty.MOD_ID+".options.modes.title", new ItemStack(Block.BOOKSHELF));
		for (Supplier<OptionsComponent> component : MODE_OPTIONS) {
			page.withComponent(component.get());
		}
		MODE_OPTIONS.clear();
		OPTION_PAGES.add(page);
	}

	@SafeVarargs
	public static void addModeOption(Supplier<OptionsComponent>... components) {
		Collections.addAll(MODE_OPTIONS, components);
	}
}
