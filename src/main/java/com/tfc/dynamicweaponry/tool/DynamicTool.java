package com.tfc.dynamicweaponry.tool;

import com.tfc.dynamicweaponry.client.ToolRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class DynamicTool extends Item {
	public DynamicTool() {
		super(getProperties());
	}
	
	public static Properties getProperties() {
		Properties properties = new Properties().maxStackSize(1);
		
		if (FMLEnvironment.dist.isClient()) {
			properties.setISTER(() -> ToolRenderer::new);
		}
		
		return properties;
	}
}
