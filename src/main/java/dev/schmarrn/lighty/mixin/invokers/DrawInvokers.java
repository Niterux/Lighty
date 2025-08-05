package dev.schmarrn.lighty.mixin.invokers;

import net.minecraft.client.gui.GuiElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiElement.class)
public interface DrawInvokers {
	@Invoker("fill")
	void lighty$fill(int x1, int y1, int x2, int y2, int color);
	@Invoker("drawHorizontalLine")
	void lighty$DrawHorizontalLine(int x1, int x2, int y, int color);
	@Invoker("drawVerticalLine")
	void lighty$DrawVerticalLine(int x, int y1, int y2, int color);
}
