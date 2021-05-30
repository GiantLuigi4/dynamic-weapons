package tfc.dynamicweaponry.item.forge;

import net.minecraft.item.BlockItem;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfc.dynamicweaponry.client.ToolForgeItemRenderer;
import tfc.dynamicweaponry.registry.Registry;

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
