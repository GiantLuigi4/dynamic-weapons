package tfc.dynamicweaponry.client.renderer.texture;

import net.minecraft.client.renderer.texture.NativeImage;
import tfc.assortedutils.utils.Color;
import tfc.dynamicweaponry.utils.Point;

public class ToolTexture {
	private final NativeImage image = new NativeImage(16, 16, true);
	public int timeSinceLastRender = 10000;
	
	public void setPixel(Point position, Color c) {
		image.setPixelRGBA(position.x, position.y, new Color(c.getBlue(), c.getGreen(), c.getRed()).getRGB());
	}
	
	public void fill(NativeImage image) {
		image.copyImageData(this.image);
	}
	
	public boolean tick() {
		timeSinceLastRender -= 1;
		return timeSinceLastRender <= 0;
	}
	
	public void close() {
		image.close();
	}
}
