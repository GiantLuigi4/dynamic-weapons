package tfc.dynamicweaponry.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import tfc.dynamicweaponry.Temp;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.access.IMayHoldATool;
import tfc.dynamicweaponry.tool.Tool;

import java.util.function.Consumer;

public class DynamicTool extends Item {
	public DynamicTool(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new ToolRenderProperties());
	}
	
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		if (!pStack.getOrCreateTag().contains("tool")) {
			CompoundTag tag = pStack.getOrCreateTag();
			Tool tool = ((IMayHoldATool) (Object) pStack).myTool();
			if (tool == null) tool = new Tool(Temp.layers);
			tag.put("tool", tool.toTag());
		} else {
			if (((IMayHoldATool) (Object) pStack).myTool() == null)
				((IMayHoldATool) (Object) pStack).setTool(Tool.fromTag(
						((IHoldADataLoader) pLevel).myLoader(),
						pStack.getOrCreateTag().get("tool")
				));
		}
	}
}
