package tfc.dynamic_weaponary.Utils;

import java.util.ArrayList;

public class PixelStorage {
	public ArrayList<Pixel> image = new ArrayList<>();
	int width;
	int height;
	
	public PixelStorage(int width, int height) {
		for (int x = 0; x <= width; x++) {
			for (int y = 0; y <= height; y++) {
				image.add(new Pixel(x, y, new DrawingUtils.ColorHelper(x * 15, x * 15, x * 15)));
			}
		}
		this.width = width;
		this.height = height;
	}
	
	public static Pixel pixelFromString(String string) {
		String string1 = "";
		String string2 = "";
		String string3 = "";
		int part = 0;
		for (char c : string.toCharArray()) {
			try {
				if (c == ',') {
					part++;
				} else if (part == 0) {
					string1 += "" + c;
				} else if (part == 1) {
					string2 += "" + c;
				} else {
					string3 += "" + c;
				}
			} catch (Exception err) {
			}
		}
		return new Pixel(
				Integer.parseInt(string1),
				Integer.parseInt(string2),
				new DrawingUtils.ColorHelper(Integer.parseInt(string3))
		);
	}
	
	public DrawingUtils.ColorHelper getPixel(int x, int y) {
		return image.get(x + (y * width)).color;
	}
	
	public void setPixel(int x, int y, DrawingUtils.ColorHelper color) {
		image.set(x + (y * width), new Pixel(x, y, color));
	}
	
	public static class Pixel {
		public int x;
		public int y;
		public DrawingUtils.ColorHelper color;
		
		public Pixel(int x, int y, DrawingUtils.ColorHelper color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
		
		public String toString() {
			return "" + x + "," + y + "," + color.getRGB();
		}
	}
}
