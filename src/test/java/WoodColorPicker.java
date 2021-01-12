import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class WoodColorPicker {
	public static void main(String[] args) {
		InputStream stream = WoodColorPicker.class.getClassLoader().getResourceAsStream(
				"assets/minecraft/textures/block/warped_planks.png"
		);
		
		BufferedImage image;
		try {
			image = ImageIO.read(stream);
			stream.close();
		} catch (Throwable ignored) {
			Runtime.getRuntime().exit(-1);
			return;
		}
		
		Color color = new Color(image.getRGB(9, 9));
		float r = color.getRed();
		float g = color.getGreen();
		float b = color.getBlue();
		System.out.println(
				new Color((int) r, (int) g, (int) b).getRGB()
		);
	}
}
