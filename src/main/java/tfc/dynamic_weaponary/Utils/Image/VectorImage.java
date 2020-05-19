package tfc.dynamic_weaponary.Utils.Image;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.DrawingUtils;

import java.util.ArrayList;

public class VectorImage extends PixelStorage {
	PixelStorage shader;
	ArrayList<line> lines = new ArrayList<>();
	
	public VectorImage(int width, int height, PixelStorage shader) {
		super(1, 1);
		image = new ArrayList<>();
		for (int x = 0; x <= width; x++) {
			for (int y = 0; y <= height; y++) {
				image.add(new Pixel(x, y, new DrawingUtils.ColorHelper(0, 0, 0, 0)));
			}
		}
		this.width = width;
		this.height = height;
		this.shader = shader;
	}
	
	public static Pixel lerp(float Dist, Pixel start, Pixel end) {
		int newX = (int) MathHelper.lerp(Dist, start.x, end.x);
		int newY = (int) MathHelper.lerp(Dist, start.y, end.y);
		int newR = (int) MathHelper.lerp(Dist, start.color.getRed(), end.color.getRed());
		int newG = (int) MathHelper.lerp(Dist, start.color.getGreen(), end.color.getGreen());
		int newB = (int) MathHelper.lerp(Dist, start.color.getBlue(), end.color.getBlue());
		int newA = (int) MathHelper.lerp(Dist, start.color.getAlpha(), end.color.getAlpha());
		return new Pixel(newX, newY, new DrawingUtils.ColorHelper(newR, newG, newB, newA));
	}
	
	public void prep() {
		for (line ln : lines) {
			for (Pixel px : ln.getAllPixels(0.05f / ln.getLength())) {
				this.setPixel(px.x, px.y, px.color);
			}
		}

//		0,15,-85592954,12,-85592951,9,-109529892,11,-109529892,11,-109529894,13,-109529894,13,-109529896,13,-109529891,8,-109529891,8,-109529891,8,-109529891,9,-109529891,10,-109529891,8,-109529893,11,-756619613,1,-75661964,12,-756619613,2,-756619613,1,-756619613,1,-756619613,2,-756619613,2,-756619612,3,-756619615,0,-756619612,2,-756619615,0,-75661965,12,-361861615,1,-36186164,13,-147012422,10,-147012422,10,-147012422,10,-147012424,12,-147012424,12,-147012424,13,-147012424,12,-14701242

//		DynamicWeapons.LOGGER.log(Level.INFO,toString);
	}
	
	public void clear() {
		for (int i = 0; i < image.size(); i++) {
			image.set(i, new Pixel(0, 0, new DrawingUtils.ColorHelper(0, 0, 0, 0)));
		}
		lines.clear();
	}
	
	public void addLine(Pixel start, Pixel end) {
		lines.add(new line(start, end));
	}
	
	public static class line {
		public Pixel px1;
		public Pixel px2;
		public Item itemOne;
		public Item itemTwo;
		
		public line(Pixel px1, Pixel px2) {
			this.px1 = px1;
			this.px2 = px2;
		}
		
		public line(Pixel px1, Pixel px2, Item itemOne, Item itemTwo) {
			this.px1 = px1;
			this.px2 = px2;
			this.itemOne = itemOne;
			this.itemTwo = itemTwo;
			px1.color = new DrawingUtils.ColorHelper(MaterialList.lookupMaterial(new ItemStack(itemOne)).color);
			px2.color = new DrawingUtils.ColorHelper(MaterialList.lookupMaterial(new ItemStack(itemTwo)).color);
		}
		
		public float getLength() {
			float xDist = Math.abs(px1.x - px2.x);
			float yDist = Math.abs(px1.y - px2.y);
			return xDist + yDist;
		}
		
		public ArrayList<Pixel> getAllPixels(float precision) {
			ArrayList<Pixel> pxls = new ArrayList<>();
			for (float i = precision; i <= getLength(); i += 1) {
				pxls.add(lerp(i / getLength(), px1, px2));
			}
			return pxls;
		}
	}
}
