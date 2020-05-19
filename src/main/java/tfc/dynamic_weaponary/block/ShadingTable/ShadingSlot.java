package tfc.dynamic_weaponary.block.ShadingTable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.ShaderOrb.ShaderItem;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;

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
	
	@Override
	public void putStack(ItemStack stack) {
		STContainer.handler.tile.hasItem = !stack.isEmpty();
		try {
			if (
					stack.getItem() instanceof ShaderItem && stack.hasTag()
			) {
				DynamicWeapons.LOGGER.log(Level.INFO, stack.getOrCreateTag().getString("image"));
				STContainer.handler.tile.image = PixelStorage.fromString(stack.getTag().getString("image"));
				DynamicWeapons.LOGGER.log(Level.INFO, STContainer.handler.tile.image.toString());
			}
		} catch (Exception err) {
			DynamicWeapons.LOGGER.log(Level.WARN, "Exception:" + err.getMessage());
			for (StackTraceElement element : err.getStackTrace()) {
				DynamicWeapons.LOGGER.log(Level.WARN, element.toString());
			}
		}
		super.putStack(stack);
	}
	
	@Override
	public void onSlotChange(ItemStack oldStackIn, ItemStack newStackIn) {
		super.onSlotChange(oldStackIn, newStackIn);
	}
}
