package tfc.dynamicweaponry.loading;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Set;

public class Materials {
	private final HashMap<ResourceLocation, Material> materialHashMap = new HashMap<>();
	
	protected void clear() {
		materialHashMap.clear();
	}
	
	public void add(Material matr) {
		materialHashMap.put(matr.regName, matr);
	}
	
	public Material get(ResourceLocation regName) {
		return materialHashMap.get(regName);
	}
	
	public Set<ResourceLocation> keys() {
		return materialHashMap.keySet();
	}
}
