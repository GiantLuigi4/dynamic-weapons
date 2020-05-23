package tfc.dynamic_weaponary;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.TriConsumer;
import tfc.dynamic_weaponary.Utils.ReflectionHelper;

import java.util.HashMap;

public class EventRegistryRedo {
	private static HashMap<String, ReflectionHelper.MethodHolder> methods = new HashMap<>();
	
	public static void RegisterEvent(String location, String event) {
		DynamicWeapons.LOGGER.log(Level.INFO, location);
		DynamicWeapons.LOGGER.log(Level.INFO, event);
		if (!methods.containsKey(location)) {
			methods.put(location, new ReflectionHelper.MethodHolder(event));
		} else {
			methods.replace(location, new ReflectionHelper.MethodHolder(event));
		}
	}
	
	public static void execute(ResourceLocation location, String type, Object arg1, Object arg2, Object arg3) {
		if (methods.containsKey(location + "." + type)) {
			methods.get(location + "." + type).execute(arg1, arg2, arg3);
		}
		Object obj = EventRegistry.getEvent(location, type);
		if (obj != null) {
			((TriConsumer) obj).accept(arg1, arg2, arg3);
		}
	}
}
