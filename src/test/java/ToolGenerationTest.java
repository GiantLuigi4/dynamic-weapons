import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ToolGenerationTest {
	public static final HashMap<Color, ResourceLocation> colorToMaterialMap = new HashMap<>();
	public static final HashMap<String, ResourceLocation> imageToPartMap = new HashMap<>();
	
	static {
		colorToMaterialMap.put(
				new Color(255, 255, 255), new ResourceLocation("minecraft:oak_planks")
		);
		colorToMaterialMap.put(
				new Color(89, 89, 89), new ResourceLocation("minecraft:gold_ingot")
		);
		colorToMaterialMap.put(
				new Color(179, 179, 179), new ResourceLocation("minecraft:diamond")
		);
		
		imageToPartMap.put(
				"test_stick.png", new ResourceLocation("dynamic_weaponry:short_stick")
		);
		imageToPartMap.put(
				"test_blade.png", new ResourceLocation("dynamic_weaponry:sword_blade")
		);
	}
	
	public static void main(String[] args) {
		CompoundNBT tool = new CompoundNBT();
		
		CompoundNBT nbt = new CompoundNBT();
		imageToPartMap.forEach((image, part) -> {
			try {
				BufferedImage image1 = ImageIO.read(ToolGenerationTest.class.getClassLoader().getResourceAsStream(image));
				
				CompoundNBT partNBT = new CompoundNBT();
				
				partNBT.putString("name", part.toString());
				
				ListNBT points = new ListNBT();
				
				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < 16; y++) {
						Color c = new Color(image1.getRGB(x, y));
						if (colorToMaterialMap.containsKey(c)) {
							CompoundNBT pointNBT = new CompoundNBT();
							pointNBT.putInt("x", x);
							pointNBT.putInt("y", 15 - y);
							pointNBT.putString("material", colorToMaterialMap.get(c).toString());
							points.add(pointNBT);
						}
					}
				}
				
				partNBT.put("points", points);
				
				nbt.put(part.toString(), partNBT);
			} catch (Throwable ignored) {
			}
		});
		
		tool.put("parts", nbt);
		
		System.out.println("dynamic_weaponry:dynamic_tool" + tool);
	}
}
