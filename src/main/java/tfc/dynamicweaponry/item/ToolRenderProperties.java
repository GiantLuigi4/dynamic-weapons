package tfc.dynamicweaponry.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

public class ToolRenderProperties implements IItemRenderProperties {
	private static final ToolRenderer renderer = new ToolRenderer();
	
	@Override
	public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
		return renderer;
	}
}

