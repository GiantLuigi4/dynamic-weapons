import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MakeBAndW {
	public static File toConvert=new File(System.getProperty("user.dir") + File.separatorChar + "image.png");
	public static void main(String[] args) {
		if (!toConvert.exists()) {
			try {
				toConvert.createNewFile();
			} catch (Exception err) {}
		}
		try {
			BufferedImage bimig=ImageIO.read(toConvert);
			File fi=new File(System.getProperty("user.dir")+"\\out.png");
			fi.createNewFile();
			for (int x=0;x<bimig.getWidth();x++) {
				for (int y=0;y<bimig.getHeight();y++) {
					bimig.setRGB(x,y,new Color(
							(new Color(bimig.getRGB(x,y)).getRed()+new Color(bimig.getRGB(x,y)).getGreen()+new Color(bimig.getRGB(x,y)).getBlue())/3,
							(new Color(bimig.getRGB(x,y)).getRed()+new Color(bimig.getRGB(x,y)).getGreen()+new Color(bimig.getRGB(x,y)).getBlue())/3,
							(new Color(bimig.getRGB(x,y)).getRed()+new Color(bimig.getRGB(x,y)).getGreen()+new Color(bimig.getRGB(x,y)).getBlue())/3
					).getRGB());
				}
			}
			ImageIO.write(bimig, "png", fi);
		} catch (Exception ignored) {
		}
	}
}
