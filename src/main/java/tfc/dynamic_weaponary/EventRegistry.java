package tfc.dynamic_weaponary;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;

public class EventRegistry {
	private static HashMap<ResourceLocation, TriConsumer> methods = new HashMap<>();
	
	public static void registerEvent(ResourceLocation material, String type, TriConsumer event) {
		methods.put(new ResourceLocation(material.toString() + "." + type), event);
	}
	
	public static TriConsumer getEvent(ResourceLocation material, String type) {
		try {
			return methods.get(new ResourceLocation(material.toString() + "." + type));
		} catch (Exception err) {
			return null;
		}
	}
}
