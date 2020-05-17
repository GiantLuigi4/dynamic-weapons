package tfc.dynamic_weaponary.block.GUI.Shading;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import tfc.dynamic_weaponary.ShaderOrb.ShaderItem;

public class ShadingSlot extends Slot {
	public ShadingSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ShaderItem;
	}
}
