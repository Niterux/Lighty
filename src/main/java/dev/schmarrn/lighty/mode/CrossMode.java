package dev.schmarrn.lighty.mode;

import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.api.ModeManager;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import it.unimi.dsi.fastutil.doubles.DoubleIntMutablePair;
import it.unimi.dsi.fastutil.doubles.DoubleObjectMutablePair;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class CrossMode extends LightyMode<Provider.Pos, DoubleObjectMutablePair<Color>> {

	public static void init() {
		ModeManager.registerMode(Lighty.MOD_ID+".cross_mode", new CrossMode());
		Config.MODE_SPECIFIC_OPTIONS.add(Config.OVERLAY_LINE_THICKNESS);
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

		cache.put(new Provider.Pos(x, y+1, z), new DoubleObjectMutablePair<Color>(offset, color));
	}

	@Override
	public void render(float tickDelta) {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		LivingEntity camera = minecraft.camera;
		if (camera == null) return;

		GL11.glPushMatrix();
		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		float brightness = minecraft.world.dimension.brightnessTable[Config.OVERLAY_BRIGHTNESS.getValue()];

		cache.forEach((pos, data) -> {
			Vec3d cameraPosition = camera.lerpPosition(tickDelta);
			double x = pos.x + 0.44 - cameraPosition.x;
			double y = pos.y + data.leftDouble() + 0.01 - cameraPosition.y;
			double z = pos.z + 0.562 - cameraPosition.z;

			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glRotated(90, 1, 0, 0);
			GL11.glRotated(45, 0, 0, 1);
			GL11.glScalef(2.85f/32f, -2.85f/32f, 2.85f/32f);

			drawCross(data.right(), brightness);

			GL11.glPopMatrix();
		});

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private static void drawCross(Color color, float brightness) {
		GL11.glLineWidth(Config.OVERLAY_LINE_THICKNESS.getValue());

		BufferBuilder bufferBuilder = BufferBuilder.INSTANCE;
		bufferBuilder.start(GL11.GL_LINES);
		bufferBuilder.color(
			(int) (color.getRed() * brightness),
			(int) (color.getGreen() * brightness),
			(int) (color.getBlue() * brightness),
			(int) (2.55f * Config.OVERLAY_TRANSPARENCY.getValue())
		);

		bufferBuilder.vertex(-8, 1, 0);
		bufferBuilder.vertex(+8, 1, 0);

		bufferBuilder.vertex(0, 1 - 8, 0);
		bufferBuilder.vertex(0, 1 + 8, 0);

		bufferBuilder.end();
	}
}
