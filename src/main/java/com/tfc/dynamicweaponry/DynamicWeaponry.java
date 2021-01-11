package com.tfc.dynamicweaponry;

import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.registry.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("DynamicWeaponry")
public class DynamicWeaponry {
	
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	
	public DynamicWeaponry() {
		Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		MinecraftForge.EVENT_BUS.addListener(Loader::serverStartup);
	}
}
