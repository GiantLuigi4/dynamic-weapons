package com.tfc.dynamicweaponry.registry;

import com.tfc.dynamicweaponry.tool.DynamicTool;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,"dynamic_weaponry");
	
	public static final RegistryObject<Item> DYNAMIC_TOOL = ITEMS.register("dynamic_tool", DynamicTool::new);
}
