package com.tfc.dynamicweaponry.item.forge;

import com.tfc.dynamicweaponry.client.ToolForgeItemRenderer;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.item.BlockItem;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ToolForgeItem extends BlockItem {
	public ToolForgeItem() {
		super(Registry.TOOL_FORGE.get(), getProperties());
	}
	
	public static Properties getProperties() {
		Properties properties = new Properties().maxStackSize(64);
		
		if (FMLEnvironment.dist.isClient()) {
			properties.setISTER(() -> ToolForgeItemRenderer::new);
		}
		
		return properties;
	}
}
