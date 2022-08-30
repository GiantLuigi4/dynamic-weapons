package tfc.dynamicweaponry.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class DynamicTool extends Item {
	public DynamicTool(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new ToolRenderProperties());
	}
}
