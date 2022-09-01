package tfc.dynamicweaponry.loading;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class Materials {
	private final HashMap<ResourceLocation, Material> materialHashMap = new HashMap<>();
	
	protected void clear() {
		materialHashMap.clear();
	}
	
	protected void add(Material matr) {
		materialHashMap.put(matr.regName, matr);
	}
	
	public Material get(ResourceLocation regName) {
		return materialHashMap.get(regName);
	}
}
