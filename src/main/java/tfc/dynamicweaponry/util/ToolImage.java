package tfc.dynamicweaponry.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class ToolImage {
	private final int[] colors = new int[16 * 16];
	
	private final NativeImage image;
	private final DynamicTexture texture;
	public final ResourceLocation location;
	
	public ToolImage(NativeImage image, DynamicTexture texture, ResourceLocation location) {
		this.image = image;
		this.texture = texture;
		this.location = location;
	}
	
	public void setRGB(int x, int y, int rgb) {
		colors[x * 16 + y] = rgb;
	}
	
	public int getRGB(int x, int y) {
		return colors[x * 16 + y];
	}
	
	public void write() {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				ExpandedColor color = new ExpandedColor(colors[x * 16 + y], true);
				color = new ExpandedColor(color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha());
				image.setPixelRGBA(x, y, color.getRGB());
			}
		}
		texture.upload();
	}
	
	private boolean wasFinalized = false;
	
	@SuppressWarnings("UnnecessaryLocalVariable")
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (wasFinalized) return;
		wasFinalized = true;
		try {
			final DynamicTexture texture1 = texture;
			final ResourceLocation location1 = location;
			Minecraft.getInstance().executeIfPossible(() -> {
				Minecraft.getInstance().textureManager.release(location1);
				texture1.close();
			});
		} catch (Throwable ignored) {
		}
	}
}
