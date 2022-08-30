import tfc.dynamicweaponry.Material;
import tfc.dynamicweaponry.ToolLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PrepForAI {
	public static void main(String[] args) throws IOException {
		File fl = new File("tests");
		for (File file : fl.listFiles()) {
			int[] ints = prep(file.getAbsolutePath());
			BufferedImage img = new BufferedImage(ints.length, 1, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < ints.length; i++) {
				int val = ints[i];
				if (val == -1) val = 15;
				val = 15 - val;
				float v = val / 15f;
				val = (int) (v * 255);
				img.setRGB(i, 0, new Color(val, val, val).getRGB());
			}
			Image img1 = img.getScaledInstance(img.getWidth() * 128, img.getHeight() * 128, BufferedImage.SCALE_FAST);
			img = new BufferedImage(img.getWidth() * 128, img.getHeight() * 128, BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = img.getGraphics();
			g.drawImage(img1, 0, 0, null);
			ImageIO.write(img, "png", new File(file.getAbsolutePath() + "/ai_input.png"));
		}
	}
	
	public static int[] prep(String dir) throws IOException {
		System.out.println(dir);
		Material gray = new Material(new Color(106, 80, 31).getRGB(), new Color(134, 101, 38).getRGB(), 0.15f);
		Material gold = new Material(new Color(126, 126, 126).getRGB(), new Color(153, 153, 153).getRGB(), 0.15f);
		
		ToolLayer layer0 = new ToolLayer();
		load(ImageIO.read(new File(dir + "/layer0.png")), layer0, gray);
		ToolLayer layer1 = new ToolLayer();
		load(ImageIO.read(new File(dir + "/layer1.png")), layer1, gold);
		ToolLayer[] layers = new ToolLayer[]{
				layer0, layer1
		};
		int[] ints = new int[32];
		for (int i = 0; i < 32; i++)
			ints[i] = runRay(i - 15, layers);
		System.out.println(Arrays.toString(ints));
		return ints;
	}
	
	public static int runRay(int x, ToolLayer[] layers) {
		int y = 0;
		while (true) {
			y++;
			if (y > 15) {
				break;
			}
			
			if (x + y > 16) return -1;
			
			for (ToolLayer layer : layers) {
				if (x + y >= 0)
					if (layer.get(x + y, 15 - y) != null)
						return y;
			}
		}
		return -1;
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
