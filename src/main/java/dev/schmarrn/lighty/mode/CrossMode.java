package dev.schmarrn.lighty.mode;

import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.schmarrn.lighty.Lighty;
import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.api.ModeManager;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.mixin.accessors.MinecraftInstanceAccessor;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class CrossMode extends LightyMode<Provider.Pos, IntIntMutablePair> {
	private static int LIST_START = -1;

	private static int GREEN_LIST;
	private static int ORANGE_LIST;
	private static int RED_LIST;

	public static void init() {
		ModeManager.registerMode(Lighty.MOD_ID + ".cross_mode", new CrossMode());
		Config.MODE_SPECIFIC_OPTIONS.add(Config.OVERLAY_LINE_THICKNESS);
	}

	private static void drawCross(Color color, float brightness) {
		BufferBuilder bufferBuilder = BufferBuilder.INSTANCE;
		bufferBuilder.start(GL11.GL_LINES);
		bufferBuilder.color(
			(int) (color.getRed() * brightness),
			(int) (color.getGreen() * brightness),
			(int) (color.getBlue() * brightness),
			(int) (2.55f * Config.OVERLAY_TRANSPARENCY.getValue())
		);

		bufferBuilder.vertex(0, 0, 0);
		bufferBuilder.vertex(1, 0, 1);

		bufferBuilder.vertex(1, 0, 0);
		bufferBuilder.vertex(0, 0, 1);

		bufferBuilder.end();
	}

	@Override
	public void compute(World world, int x, int y, int z) {
		if (Provider.isBlocked(x, y + 1, z, world)) return;

		IntIntMutablePair light = Provider.compute(world, x, y, z);
		if (light == null) return;

		ColorEnum color = Provider.getColor(light);
		if (color == null) return;

		double offset = 0;

		int blockId = world.getBlock(x, y + 1, z);
		if (blockId != 0)
			offset += Block.BY_ID[blockId].maxY;

		cache.put(new Provider.Pos(x, y + 1 + offset, z), light);
	}

	@Override
	public void initializeDrawLists() {
		super.initializeDrawLists();
		if (LIST_START == -1) {
			LIST_START = MemoryTracker.getLists(3);
			GREEN_LIST = LIST_START;
			ORANGE_LIST = LIST_START + 1;
			RED_LIST = LIST_START + 2;
		}
		World world = MinecraftInstanceAccessor.getMinecraft().world;
		if (world == null)
			return;
		float brightness = world.dimension.brightnessTable[Config.OVERLAY_BRIGHTNESS.getValue()];
		GL11.glNewList(GREEN_LIST, GL11.GL_COMPILE);
		drawCross(ColorEnum.SAFE.getColor(), brightness);
		GL11.glEndList();
		GL11.glNewList(ORANGE_LIST, GL11.GL_COMPILE);
		drawCross(ColorEnum.DAYLIGHT_SAFE.getColor(), brightness);
		GL11.glEndList();
		GL11.glNewList(RED_LIST, GL11.GL_COMPILE);
		drawCross(ColorEnum.UNSAFE.getColor(), brightness);
		GL11.glEndList();
	}

	@Override
	public void render(float tickDelta) {
		Minecraft minecraft = MinecraftInstanceAccessor.getMinecraft();
		LivingEntity camera = minecraft.camera;
		if (camera == null) return;

		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glLineWidth(Config.OVERLAY_LINE_THICKNESS.getValue());
		cache.forEach((pos, data) -> {
			Vec3d cameraPosition = camera.lerpPosition(tickDelta);
			double x = pos.x - cameraPosition.x;
			double y = pos.y + 0.01 - cameraPosition.y;
			double z = pos.z - cameraPosition.z;
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			switch (Provider.getColor(data)) {
				case SAFE:
					GL11.glCallList(GREEN_LIST);
					break;
				case DAYLIGHT_SAFE:
					GL11.glCallList(ORANGE_LIST);
					break;
				case UNSAFE:
					GL11.glCallList(RED_LIST);
			}
			GL11.glPopMatrix();
		});
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (Config.OVERLAY_TRANSPARENCY.getValue() < 100)
			GL11.glDisable(GL11.GL_BLEND);
	}
}
