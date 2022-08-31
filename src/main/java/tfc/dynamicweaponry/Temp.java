package tfc.dynamicweaponry;

import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.loading.Materials;
import tfc.dynamicweaponry.tool.ToolLayer;
import tfc.dynamicweaponry.util.TextureGen;
import tfc.dynamicweaponry.util.ToolImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Temp {
	private static ToolLayer layer0;
	private static ToolLayer layer1;
	
	public static ToolImage image;
	public static ToolLayer[] layers;
	
	static {
		try {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			
			Material gray = Materials.get(new ResourceLocation("dynamic_weaponry:oak_wood"));
			Material gold = Materials.get(new ResourceLocation("dynamic_weaponry:gold"));
			
			String dir = ".";
			
			layer0 = new ToolLayer();
			load(ImageIO.read(new File(dir + "/layer0.png")), layer0, gray);
			layer1 = new ToolLayer();
			load(ImageIO.read(new File(dir + "/layer1.png")), layer1, gold);
			layers = new ToolLayer[]{
					layer0, layer1
			};
		} catch (Throwable err) {
			throw new RuntimeException();
		}
	}
	
	private static void load(BufferedImage from, ToolLayer to, Material mat) {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(from, 0, 0, 16, 16, null);
		from = img;
		for (int x = 0; x < from.getWidth(); x++) {
			for (int y = 0; y < from.getHeight(); y++) {
				int pixel = from.getRGB(x, y);
				Color c = new Color(pixel, true);
				if (c.getAlpha() != 0)
					to.set(x, 15 - y, mat);
			}
		}
	}
}
