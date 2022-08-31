package tfc.dynamicweaponry.loading;

import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.Material;

import java.awt.*;
import java.util.HashMap;

public class Materials {
	private static final HashMap<ResourceLocation, Material> materialHashMap = new HashMap<>();
	
	static {
		Material gray = new Material(new Color(15315218).getRGB(), new Color(16646006).getRGB(), 0.25f, new ResourceLocation("dynamic_weaponry:gold"));
		add(gray);
		Material gold = new Material(new Color(2535834).getRGB(), new Color(3271627).getRGB(), 0.3f, new ResourceLocation("dynamic_weaponry:diamond"));
		add(gold);
		Material oak = new Material(new Color(9794369).getRGB(), new Color(10388301).getRGB(), 0.15f, new ResourceLocation("dynamic_weaponry:oak_wood"));
		add(oak);
	}
	
	protected static void clear() {
		materialHashMap.clear();
	}
	
	protected static void add(Material matr) {
		materialHashMap.put(matr.regName, matr);
	}
	
	public static Material get(ResourceLocation regName) {
		return materialHashMap.get(regName);
	}
}
