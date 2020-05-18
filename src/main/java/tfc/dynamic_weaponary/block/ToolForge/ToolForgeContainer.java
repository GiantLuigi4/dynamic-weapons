package tfc.dynamic_weaponary.block.ToolForge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ToolForgeContainer extends Container {
	public static ContainerType<ToolForgeContainer> TYPE;
	public static ToolForge.ForgeTE tile;
	public IInventory player;
	
	public ToolForgeContainer(int id, IInventory playerInv) {
		super(TYPE, id);
		player = playerInv;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}
	
	public void setTile(ToolForge.ForgeTE tile1) {
		tile = tile1;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
}
