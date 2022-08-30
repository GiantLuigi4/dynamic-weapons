package tfc.dynamicweaponry;

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
	
	static {
		try {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			
			Material gold = new Material(new Color(15315218).getRGB(), new Color(16646006).getRGB(), 0.15f);
			Material gray = new Material(new Color(9794369).getRGB(), new Color(10388301).getRGB(), 0.15f);
			
			String dir = ".";
			
			File fl = new File(dir + "/layer0.png").getAbsoluteFile();
			layer0 = new ToolLayer();
			load(ImageIO.read(new File(dir + "/layer0.png")), layer0, gray);
			layer1 = new ToolLayer();
			load(ImageIO.read(new File(dir + "/layer1.png")), layer1, gold);
			ToolLayer[] layers = new ToolLayer[]{
					layer0, layer1
			};
			
			image = TextureGen.generate(layers, img.getTransparency(), true);
			image.write();
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
