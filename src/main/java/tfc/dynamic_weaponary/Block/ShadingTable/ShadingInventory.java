package tfc.dynamic_weaponary.Block.ShadingTable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import tfc.dynamic_weaponary.Deffered_Registry.Items;

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
		ItemStack shaderStack = new ItemStack(Items.SHADER_ORB.get());
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("image", tile.image.toString());
		shaderStack.setTag(nbt);
		return tile.hasItem ? shaderStack : ItemStack.EMPTY;
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
		ItemStack shaderStack = new ItemStack(Items.SHADER_ORB.get());
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("image", tile.image.toString());
		shaderStack.setTag(nbt);
		return tile.hasItem ? shaderStack : ItemStack.EMPTY;
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