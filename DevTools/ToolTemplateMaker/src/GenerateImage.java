import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class GenerateImage {
	public static File options=new File(System.getProperty("user.dir") + File.separatorChar + "options.txt");
	public static void main(String[] args) {
		if (!options.exists()) {
			try {
				options.createNewFile();
			} catch (Exception err) {}
		} else {
			try {
				Scanner sc=new Scanner(options);
				int width=Integer.parseInt(sc.nextLine());
				int height=Integer.parseInt(sc.nextLine());
				File fi=new File(System.getProperty("user.dir")+"\\out.png");
				BufferedImage bimig=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
				fi.createNewFile();
				for (int x=0;x<width;x++) {
					for (int y=0;y<height;y++) {
						bimig.setRGB(x,y,
								new Color(
										new Random(x*y*new Date().getTime()/new Date().getYear()).nextInt(255),
										new Random(x*y*new Date().getTime()/new Date().getSeconds()).nextInt(255),
										new Random(x*y*new Date().getTime()/new Date().getMonth()).nextInt(255)
								).getRGB()
							);
					}
				}
				System.out.println("h");
				ImageIO.write(bimig, "png", fi);
			} catch (Exception ignored) {
			}
		}
	}
}
