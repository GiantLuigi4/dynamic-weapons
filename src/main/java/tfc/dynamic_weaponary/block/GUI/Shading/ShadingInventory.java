package tfc.dynamic_weaponary.block.GUI.Shading;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.block.ShadingTable;

import javax.annotation.Nonnull;

public class ShadingInventory implements IInventory {
	
	ShadingTable.ShadingTE tile;
	
	public ShadingInventory() {
	}
	
	public void setTile(ShadingTable.ShadingTE te) {
		this.tile = te;
	}
	
	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return tile.hasItem ? new ItemStack(Items.SHADER_ORB.get()) : ItemStack.EMPTY;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return tile.hasItem;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return removeStackFromSlot(index);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return tile.hasItem ? new ItemStack(Items.SHADER_ORB.get()) : ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		tile.hasItem = !stack.isEmpty();
		markDirty();
	}
	
	@Override
	public void markDirty() {
		this.tile.markDirty();
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void clear() {
		tile.hasItem = false;
	}
}