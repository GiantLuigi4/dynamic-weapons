package tfc.dynamic_weaponary.Utils.Image;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class MaterialVectorImage extends MaterialBasedPixelStorage {
	ArrayList<MaterialLine> lines = new ArrayList<>();
	
	public MaterialVectorImage(int width, int height) {
		super(width, height);
	}
	
	public static MaterialPixel lerp(float Dist, MaterialPixel start, MaterialPixel end) {
		int newX = (int) MathHelper.lerp(Dist, start.x, end.x);
		int newY = (int) MathHelper.lerp(Dist, start.y, end.y);
		return new MaterialPixel(newX, newY, start.stack);
	}
	
	public void prep() {
		for (MaterialLine ln : lines) {
			for (MaterialPixel px : ln.getAllPixels(0.05f / ln.getLength())) {
				if (px.x < width && px.y < height) {
					this.setPixel(px.x, px.y, px.stack);
				}
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < image.size(); i++) {
			image.set(i, new MaterialPixel(0, 0, ItemStack.EMPTY));
		}
		lines.clear();
	}
	
	public void addLine(MaterialPixel start, MaterialPixel end) {
		lines.add(new MaterialLine(start, end));
	}
	
	public static class MaterialLine {
		public MaterialPixel px1;
		public MaterialPixel px2;
		
		public MaterialLine(MaterialPixel px1, MaterialPixel px2) {
			this.px1 = px1;
			this.px2 = px2;
		}
		
		public float getLength() {
			float xDist = Math.abs(px1.x - px2.x);
			float yDist = Math.abs(px1.y - px2.y);
			return xDist + yDist;
		}
		
		public ArrayList<MaterialPixel> getAllPixels(float precision) {
			ArrayList<MaterialPixel> pxls = new ArrayList<>();
			for (float i = precision; i <= getLength(); i += 1) {
				pxls.add(lerp(i / getLength(), px1, px2));
			}
			return pxls;
		}
	}
}
