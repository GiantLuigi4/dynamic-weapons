package tfc.dynamic_weaponary;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;

public class EventRegistry {
	private static HashMap<String, TriConsumer> methods = new HashMap<>();
	
	public static void registerEvent(ResourceLocation material, String type, TriConsumer event) {
		methods.put(material.toString() + "." + type, event);
	}
	
	public static TriConsumer getEvent(ResourceLocation material, String type) {
		try {
			if (methods.containsKey(material.toString() + "." + type)) {
				return methods.get(material.toString() + "." + type);
			} else {
				try {
					return (TriConsumer) (ForgeRegistries.ITEMS.getValue(material).getClass().getField(type).get(ForgeRegistries.ITEMS.getValue(material)));
				} catch (Exception err) {
					return null;
				}
			}
		} catch (Exception err) {
			return null;
		}
	}
}
