package tfc.dynamic_weaponary;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.function.Function;

public class CalculationRegistry {
	private static HashMap<String, Function> methods = new HashMap<>();
	
	public static void registerEvent(ResourceLocation material, String type, Function event) {
		methods.put(material.toString() + "." + type, event);
	}
	
	public static Function getEvent(ResourceLocation material, String type) {
		try {
			if (methods.containsKey(material.toString() + "." + type)) {
				return methods.get(material.toString() + "." + type);
			} else {
				try {
					return (Function) (ForgeRegistries.ITEMS.getValue(material).getClass().getField(type).get(ForgeRegistries.ITEMS.getValue(material)));
				} catch (Exception err2) {
					return null;
				}
			}
		} catch (Exception err) {
			return null;
		}
	}
}
