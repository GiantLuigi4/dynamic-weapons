package tfc.dynamic_weaponary.Utils;

import java.util.ArrayList;

public class PixelStorage {
	public ArrayList<Pixel> image = new ArrayList<>();
	int width;
	int height;
	
	public PixelStorage(int width, int height, boolean gradient) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (gradient) {
					image.add(new Pixel(x, y, new DrawingUtils.ColorHelper(255 - ((x + 1) * 15), 255 - ((x + 1) * 15), 255 - ((x + 1) * 15))));
				} else {
					image.add(new Pixel(x, y, new DrawingUtils.ColorHelper(0, 0, 0, 0)));
				}
			}
		}
		this.width = width;
		this.height = height;
	}
	
	public PixelStorage(int width, int height) {
		this(width, height, true);
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
	
	public static PixelStorage fromString(String string) {
		ArrayList<Pixel> pixels = new ArrayList<>();
		String string2 = "";
		int maxX = 0;
		int maxY = 0;
		for (char c : string.toCharArray()) {
			if (c != ';') {
				string2 += c;
			} else {
				pixels.add(pixelFromString(string2));
				if (pixels.get(pixels.size() - 1).x >= maxX) {
					maxX = pixels.get(pixels.size() - 1).x;
				}
				if (pixels.get(pixels.size() - 1).y >= maxY) {
					maxY = pixels.get(pixels.size() - 1).y;
				}
				string2 = "";
			}
		}
		PixelStorage storage = new PixelStorage(maxX + 1, maxY + 1, false);
		for (Pixel px : pixels) {
			try {
				storage.setPixel(px.x, px.y, px.color);
			} catch (Exception err) {
			}
		}
		return storage;
	}
	
	public String toString() {
		String str = "";
		for (Pixel px : image) {
			str += px.toString() + ";";
		}
		return str;
	}
}
