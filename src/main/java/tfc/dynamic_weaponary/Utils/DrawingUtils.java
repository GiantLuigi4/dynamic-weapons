package tfc.dynamic_weaponary.Utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class DrawingUtils {
	public static void drawTexturedRect(double x, double y, double u, double v, double texWidth, double texHeight, double width, double height, double red, double green, double blue, double alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos(x, (y + height), 0).tex((float) ((float) u * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos((x + width), (y + height), 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos((x + width), y, 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos(x, y, 0).tex((float) ((float) u * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		tessellator.draw();
	}
	
	public static class ColorHelper {
		public static ColorHelper BLUE = new ColorHelper(0, 0, 255);
		int value;
		
		public ColorHelper(int r, int g, int b) {
			this(r, g, b, 255);
		}
		
		public ColorHelper(int r, int g, int b, int a) {
			value = ((a & 0xFF) << 24) |
					((r & 0xFF) << 16) |
					((g & 0xFF) << 8) |
					((b & 0xFF));
//			testColorValueRange(r,g,b,a);
		}
		
		public ColorHelper(int rgb) {
			value = 0xff000000 | rgb;
		}
		
		public ColorHelper darker() {
			double FACTOR = 0.7;
			return new ColorHelper(Math.max((int) (getRed() * FACTOR), 0),
					Math.max((int) (getGreen() * FACTOR), 0),
					Math.max((int) (getBlue() * FACTOR), 0),
					getAlpha());
		}
		
		public ColorHelper darker(double FACTOR) {
			return new ColorHelper(Math.max((int) (getRed() * FACTOR), 0),
					Math.max((int) (getGreen() * FACTOR), 0),
					Math.max((int) (getBlue() * FACTOR), 0),
					getAlpha());
		}
		
		public int getRGB() {
			return value;
		}
		
		public int getRed() {
			return (getRGB() >> 16) & 0xFF;
		}
		
		public int getGreen() {
			return (getRGB() >> 8) & 0xFF;
		}
		
		public int getBlue() {
			return (getRGB() >> 0) & 0xFF;
		}
		
		public int getAlpha() {
			return (getRGB() >> 24) & 0xff;
		}
		
		public boolean equals(Object obj) {
			return obj instanceof ColorHelper && ((ColorHelper) obj).getRGB() == this.getRGB();
		}
		
		public String toString() {
			return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
		}
	}
}
