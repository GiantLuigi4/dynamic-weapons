package tfc.dynamic_weaponary.API;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;
import tfc.dynamic_weaponary.EventRegistry;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.Tool.Material;

public class RegistryHelper {
	public static void RegisterMaterial(Material mat, ItemStack item) {
		MaterialList.RegisterMaterial(item.getItem(), mat);
	}
	
	public static void RegisterMaterial(Material mat, Item item) {
		MaterialList.RegisterMaterial(item, mat);
	}
	
	public static void RegisterEvent(TriConsumer event, eventTypes type, Item item) {
		EventRegistry.registerEvent(item.getRegistryName(), type.name, event);
	}
	
	public static enum eventTypes {
		HIT_ENTITY("event_hitentity"),
		INV_TICK("InvTick");
		
		String name;
		
		eventTypes(String name) {
			this.name = name;
		}
		
		
		@Override
		public String toString() {
			return name;
		}
	}
}
