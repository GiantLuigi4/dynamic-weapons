package tfc.dynamic_weaponary;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tfc.dynamic_weaponary.Utils.Tool.Material;

import java.util.HashMap;

public class MaterialList {
	public static HashMap<String, Material> materialHashMap = new HashMap<>();
	
	public static void RegisterMaterial(Item stack, Material mat) {
		materialHashMap.put(stack.getRegistryName().toString(), mat);
	}
	
	public static Material lookupMaterial(ItemStack stack) {
		Class t = stack.getItem().getClass();
		if (materialHashMap.containsKey(stack.getItem().getRegistryName().toString())) {
			return materialHashMap.get(stack.getItem().getRegistryName().toString());
		}
		try {
			int d = t.getField("MatDurability").getInt(stack.getItem());
			int s = t.getField("MatStrength").getInt(stack.getItem());
			float w = t.getField("MatWeight").getFloat(stack.getItem());
			int c = t.getField("MatColor").getInt(stack.getItem());
			return new Material(d, s, w, c);
		} catch (Exception err) {
			return new Material(0, 0, 0, 0);
		}
	}
}
