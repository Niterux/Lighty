package dev.schmarrn.lighty.mode;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.api.ModeManager;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class NumberMode extends LightyMode<Provider.Pos, NumberMode.Data> {

	private static final float TEXTURE_SIZE = 32;

	public static void init() {
		ModeManager.registerMode(Lighty.MOD_ID+".number_mode", new NumberMode());
		Config.MODE_SPECIFIC_OPTIONS.add(Config.SHOW_SKYLIGHT_LEVEL);
		Config.MODE_SPECIFIC_OPTIONS.add(Config.SHOW_ABOVE_HITBOX);
	}

	@Override
	public void compute(World world, int x, int y, int z) {
		if (Provider.isBlocked(x, y+1, z, world)) return;

		IntIntMutablePair light = Provider.compute(world, x, y, z);
		if (light == null) return;

		Color color = Provider.getColor(light);
		if (color == null) return;

		double offset = 0;

		int blockId = world.getBlock(x, y + 1, z);
		if (blockId != 0 && blockId == Block.SNOW_LAYER.id)
			offset += Block.BY_ID[blockId].getCollisionShape(world, x, y+1, z).maxY;

		cache.put(new Provider.Pos(x, y+1, z), new Data(light.leftInt(), light.rightInt(), offset, color));
	}

	@Override
	public void render(float tickDelta) {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		LivingEntity camera = minecraft.camera;
		if (camera == null) return;

		GL11.glPushMatrix();
		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glEnable(GL11.GL_BLEND);

		float brightness = minecraft.world.dimension.brightnessTable[Config.OVERLAY_BRIGHTNESS.getValue()];
		TextureManager textureManager = MinecraftInstanceAccessor.getMinecraft().textureManager;
		textureManager.bind(textureManager.load("/assets/lighty/textures/block/numbers.png"));
		cache.forEach((pos, data) -> {
			Vec3d cameraPosition = camera.lerpPosition(tickDelta);
			double x = pos.x + 0.5 - cameraPosition.x;
			double y = pos.y + 0.05 - cameraPosition.y;
			double z = pos.z + 0.5 - cameraPosition.z;

			if (Config.SHOW_ABOVE_HITBOX.getValue())
				y += data.offset;

			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glScalef(1f/32f, -1f/32f, 1f/32f);
			GL11.glRotated(90, 1, 0, 0);
			GL11.glRotated(camera.cameraPitch-180, 0, 0, 1);

			float offset = Config.SHOW_SKYLIGHT_LEVEL.getValue() ? 4.5f : 0f;

			renderNumber(data.blockLightLevel, data.color, -offset, brightness);
			if (Config.SHOW_SKYLIGHT_LEVEL.getValue())
				renderNumber(data.skyLightLevel, data.color, offset, brightness);

			GL11.glPopMatrix();
		});

		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private static void renderNumber(int light, Color color, float y_offset, float brightness) {
		renderNumber(light, color, y_offset, 0, brightness);
	}

	private static void renderNumber(int light, Color color, float y_offset, float x_offset, float brightness) {
		if (light > 15) light = 15;

		if (light >= 10) {
			int secondDigit = light % 10;
			light = light / 10;

			renderNumber(secondDigit, color, y_offset, x_offset + 3f, brightness);
			x_offset -= 3f;
		}

		int x = (light % 4) * 8;
		int y = (light / 4) * 8;

		drawTexture(-2.5f + x_offset, -3f + y_offset, 0, x, y, 8, 8, color, brightness);
	}

	private static void drawTexture(float x, float y, float z, float u, float v, float width, float height, Color color, float brightness) {
		BufferBuilder bufferBuilder = BufferBuilder.INSTANCE;

		bufferBuilder.start();

		bufferBuilder.color(
			color.getRed() * brightness,
			color.getGreen() * brightness,
			color.getBlue() * brightness,
			(int) (2.55f * Config.OVERLAY_TRANSPARENCY.getValue())
		);

		bufferBuilder.vertex(x, y + height, z, u / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE);
		bufferBuilder.vertex(x + width, y + height, z, (u + width) / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE);
		bufferBuilder.vertex(x + width, y, z, (u + width) / TEXTURE_SIZE, v / TEXTURE_SIZE);
		bufferBuilder.vertex(x, y, z, u / TEXTURE_SIZE, v / TEXTURE_SIZE);

		bufferBuilder.end();
	}

	static class Data {
		public int blockLightLevel;
		public int skyLightLevel;
		public double offset;
		public Color color;

		public Data(int blockLightLevel, int skyLightLevel, double offset, Color color) {
			this.blockLightLevel = blockLightLevel;
			this.skyLightLevel = skyLightLevel;
			this.offset = offset;
			this.color = color;
		}
	}
}
