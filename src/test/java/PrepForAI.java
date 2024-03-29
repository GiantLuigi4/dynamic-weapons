import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.loading.ClientMaterial;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.loading.Materials;
import tfc.dynamicweaponry.tool.stat_calc.ToolFlattener;
import tfc.dynamicweaponry.tool.ToolLayer;
import tfc.dynamicweaponry.util.ExpandedColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PrepForAI {
	public static void main(String[] args) throws IOException {
		File fl = new File("tests");
		for (File file : Objects.requireNonNull(fl.listFiles())) {
			int[][] ints = prep(file.getAbsolutePath());
			BufferedImage img = new BufferedImage(32, 2, BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < ints.length; y++) {
				int[] row = ints[y];
				for (int x = 0; x < row.length; x++) {
					int val = row[x];
					if (val == -1) val = 15;
					val = 15 - val;
					float v = val / 15f;
					val = (int) (v * 255);
					img.setRGB(x, y, new Color(val, val, val).getRGB());
				}
			}
//			for (int i = 0; i < ints.length / 2; i++) {
//				int val = ints[i];
//				if (val == -1) val = 15;
//				val = 15 - val;
//				float v = val / 15f;
//				val = (int) (v * 255);
//				img.setRGB(i, 0, new Color(val, val, val).getRGB());
//			}
//			for (int i = 0; i < ints.length / 2; i++) {
//				int val = ints[i + 32];
//				if (val == -1) val = 15;
//				val = 15 - val;
//				float v = val / 15f;
//				val = (int) (v * 255);
//				img.setRGB(i, 1, new Color(val, val, val).getRGB());
//			}
			Image img1 = img.getScaledInstance(img.getWidth() * 128, img.getHeight() * 128, BufferedImage.SCALE_FAST);
			img = new BufferedImage(img.getWidth() * 128, img.getHeight() * 128, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = img.getGraphics();
			g.drawImage(img1, 0, 0, null);
			ImageIO.write(img, "png", new File(file.getAbsolutePath() + "/ai_input.png"));
		}
	}
	
	public static int[][] prep(String dir) throws IOException {
		System.out.println(dir);
		ClientMaterial gray = new ClientMaterial(new Color(9794369).getRGB(), new Color(10388301).getRGB(), 0.15f, new ResourceLocation("a"));
		ClientMaterial gold = new ClientMaterial(new ExpandedColor(60, 59, 59).brighter(0.075f, 0).getRGB(), new Color(126, 126, 126).getRGB(), 0.1f, new ResourceLocation("b"));
		
		Materials materials = new Materials();
		materials.add(new Material(gray));
		materials.add(new Material(gold));
		
		ToolLayer layer0 = new ToolLayer(materials);
		load(ImageIO.read(new File(dir + "/layer0.png")), layer0, gray);
		ToolLayer layer1 = new ToolLayer(materials);
		load(ImageIO.read(new File(dir + "/layer1.png")), layer1, gold);
		ToolLayer[] layers = new ToolLayer[]{
				layer0, layer1
		};
		
		return ToolFlattener.prep2D(layers);
	}
	
	private static void load(BufferedImage from, ToolLayer to, ClientMaterial mat) {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(from, 0, 0, 16, 16, null);
		from = img;
		for (int x = 0; x < from.getWidth(); x++) {
			for (int y = 0; y < from.getHeight(); y++) {
				int pixel = from.getRGB(x, y);
				Color c = new Color(pixel, true);
				if (c.getAlpha() != 0)
					to.set(x, 15 - y, new Material(mat));
			}
		}
	}
}
