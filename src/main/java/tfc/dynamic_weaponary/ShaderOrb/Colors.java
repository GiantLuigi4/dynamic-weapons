package tfc.dynamic_weaponary.ShaderOrb;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;

import java.util.HashMap;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class Colors implements IItemColor {
	HashMap<String, DrawingUtils.ColorHelper> memory = new HashMap<>();
	
	@Override
	public int getColor(ItemStack stack, int index) {
		int colorR = 128;
		int colorG = 128;
		int colorB = 128;
		int start = 0;
		if (stack.hasTag()) {
			String imageData = stack.getTag().getString("image");
			PixelStorage image = PixelStorage.fromString(imageData);
			if (!memory.containsKey(imageData) || (new Random().nextDouble() <= 0.01d)) {
				if (index == 0) {
					start = 0;
				} else if (index == 1 || index == 2) {
					start = 4;
				} else if (index == 3) {
					start = 8;
				} else if (index == 4) {
					start = 12;
				}
				for (int x = start; x < start + 4; x++) {
					for (int y = 0; y < 16; y++) {
						try {
							colorR += new DrawingUtils.ColorHelper(image.getPixel(x, y).getRGB()).getRed();
							colorR /= 2;
							colorG += new DrawingUtils.ColorHelper(image.getPixel(x, y).getRGB()).getGreen();
							colorG /= 2;
							colorB += new DrawingUtils.ColorHelper(image.getPixel(x, y).getRGB()).getBlue();
							colorB /= 2;
						} catch (Exception err) {
						}
					}
				}
				if (memory.containsKey(imageData)) {
				
				} else {
					memory.replace(imageData, new DrawingUtils.ColorHelper(colorR, colorG, colorB));
				}
			} else {
				return memory.get(imageData).getRGB();
			}
		}
		if (new Random().nextDouble() <= 0.01) {
			memory.clear();
		}
		return new DrawingUtils.ColorHelper(colorR, colorG, colorB).getRGB();
	}
}
