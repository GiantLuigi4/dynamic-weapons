import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.Material;
import tfc.dynamicweaponry.tool.ToolLayer;
import tfc.dynamicweaponry.util.TextureGen;
import tfc.dynamicweaponry.util.ToolImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AwtTesting {
	public static void main(String[] args) throws IOException {
		File fl = new File("tests");
		for (File file : Objects.requireNonNull(fl.listFiles())) {
			run(file.getAbsolutePath());
		}
	}
	
	private static void run(String dir) throws IOException {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		Material gold = new Material(new Color(15315218).getRGB(), new Color(16646006).getRGB(), 0.15f, new ResourceLocation("a"));
		Material gray = new Material(new Color(9794369).getRGB(), new Color(10388301).getRGB(), 0.15f, new ResourceLocation("a"));
		
		ToolLayer layer0 = new ToolLayer();
		load(ImageIO.read(new File(dir + "/layer0.png")), layer0, gray);
		ToolLayer layer1 = new ToolLayer();
		load(ImageIO.read(new File(dir + "/layer1.png")), layer1, gold);
		ToolLayer[] layers = new ToolLayer[]{
				layer0, layer1
		};
		
		ToolImage image = TextureGen.generate(layers, 0, false);
		for (int x = 0; x < 16; x++)
			for (int y = 0; y < 16; y++)
				img.setRGB(x, y, image.getRGB(x, y));
		
		ImageIO.write(img, "png", new File(dir + "/output.png"));
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
