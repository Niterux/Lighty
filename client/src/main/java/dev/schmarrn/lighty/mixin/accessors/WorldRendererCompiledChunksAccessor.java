package dev.schmarrn.lighty.mixin.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.world.RenderChunk;
import net.minecraft.client.render.world.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererCompiledChunksAccessor {
    @Accessor("compiledChunks")
    public RenderChunk[] getCompiledChunks();
}
