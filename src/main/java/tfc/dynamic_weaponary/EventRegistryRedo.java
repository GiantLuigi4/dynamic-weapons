package tfc.dynamic_weaponary;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import tfc.dynamic_weaponary.Utils.ReflectionHelper;

import java.util.HashMap;

public class EventRegistryRedo {
	private static HashMap<ResourceLocation, ReflectionHelper.MethodHolder> methods = new HashMap<>();
	
	public static void RegisterEvent(ResourceLocation location, String event) {
		DynamicWeapons.LOGGER.log(Level.INFO, location);
		DynamicWeapons.LOGGER.log(Level.INFO, event);
		if (!methods.containsKey(location)) {
			methods.put(location, new ReflectionHelper.MethodHolder(event));
		} else {
			methods.replace(location, new ReflectionHelper.MethodHolder(event));
		}
	}
	
	public static void execute(ResourceLocation location, Object arg1, Object arg2, Object arg3) {
		if (methods.containsKey(location)) {
			methods.get(location).execute(arg1, arg2, arg3);
		}
	}
}
