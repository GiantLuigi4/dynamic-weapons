package tfc.dynamic_weaponary.Utils.Image;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.DrawingUtils;

import java.util.ArrayList;

public class MaterialBasedPixelStorage {
	public ArrayList<MaterialPixel> image = new ArrayList<>();
	int width;
	int height;
	
	public MaterialBasedPixelStorage(int width, int height) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.add(new MaterialPixel(x, y, ItemStack.EMPTY));
			}
		}
		this.width = width;
		this.height = height;
	}
	
	public static MaterialPixel pixelFromString(String string) {
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
		return new MaterialPixel(
				Integer.parseInt(string1),
				Integer.parseInt(string2),
				new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string3)))
		);
	}
	
	public static MaterialBasedPixelStorage fromString(String string) {
		ArrayList<MaterialPixel> pixels = new ArrayList<>();
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
		MaterialBasedPixelStorage storage = new MaterialBasedPixelStorage(maxX + 1, maxY + 1);
		for (MaterialPixel px : pixels) {
			try {
				storage.setPixel(px.x, px.y, px.stack);
			} catch (Exception err) {
			}
		}
		return storage;
	}
	
	public DrawingUtils.ColorHelper getPixel(int x, int y) {
		return image.get(x + (y * width)).color;
	}
	
	public void setPixel(int x, int y, ItemStack stack) {
		image.set(x + (y * width), new MaterialPixel(x, y, stack));
	}
	
	public String toString() {
		String str = "";
		for (MaterialPixel px : image) {
			str += px.toString() + ";";
		}
		return str;
	}
	
	public static class MaterialPixel {
		public int x;
		public int y;
		public DrawingUtils.ColorHelper color;
		public ItemStack stack;
		
		public MaterialPixel(int x, int y, ItemStack stack) {
			this.x = x;
			this.y = y;
			this.stack = stack;
			this.color = new DrawingUtils.ColorHelper(MaterialList.lookupMaterial(stack).color);
		}
		
		public String toString() {
			return "" + x + "," + y + "," + stack.getItem().getRegistryName();
		}
	}
}
