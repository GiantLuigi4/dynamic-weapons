import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PatternGen {
	public static void main(String[] args) throws IOException {
		BufferedImage image = ImageIO.read(PatternGen.class.getClassLoader().getResourceAsStream("patterns/shulkered/shulkered_ingotB.png"));
		StringBuilder pattern = new StringBuilder("  \"pattern\": [\n");
		for (int x = 0; x < image.getWidth(); x++) {
			pattern.append("    [");
			for (int y = 0; y < image.getHeight(); y++) {
				pattern.append(image.getRGB(x, y)).append(", ");
			}
			pattern.append("],\n");
		}
		pattern = new StringBuilder((pattern + "  ]")
				.replace(", ]", "]")
				.replace(",\n  ]", "\n  ]"))
		;
		System.out.println(pattern.toString());
	}
}
