package tfc.dynamic_weaponary.block.GUI.Shading;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tfc.dynamic_weaponary.block.ShadingTable;

/*
https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorContainer.java
and
https://github.com/Commoble/tubesreloaded/blob/1.15.2-1.3.0c/src/main/java/com/github/commoble/tubesreloaded/common/blocks/filter/FilterContainer.java
*/
public class STContainer extends Container {
	public static ContainerType<STContainer> TYPE;
	static ShadingInventory handler = new ShadingInventory();
	public NonNullList items = NonNullList.withSize(1, ItemStack.EMPTY);
	
	public STContainer(int id, IInventory playerInv, ShadingTable.ShadingTE tile) {
		super(TYPE, id);
		handler = new ShadingInventory();
		setTile(tile);
		this.addSlot(new ShadingSlot(handler, 0, 128, 26));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}
	
	public STContainer(int id, IInventory playerInv) {
		super(TYPE, id);
		this.addSlot(new ShadingSlot(handler, 0, 128, 26));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack copiedStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stackFromSlot = slot.getStack();
			copiedStack = stackFromSlot.copy();
			if (index == 0) {
				if (!this.mergeItemStack(stackFromSlot, 1, 37, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stackFromSlot, 0, 1, false)) {
				return ItemStack.EMPTY;
			}
			
			if (stackFromSlot.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if (stackFromSlot.getCount() == copiedStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, stackFromSlot);
		}
		
		return copiedStack;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	public void setTile(ShadingTable.ShadingTE tile) {
		handler.setTile(tile);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	@Override
	public Slot getSlot(int slotId) {
		return super.getSlot(slotId);
	}
	
	@Override
	public NonNullList<ItemStack> getInventory() {
		items.set(0, handler.getStackInSlot(0));
		return items;
	}
	
	public static final class Provider implements INamedContainerProvider {
		
		static final ITextComponent TITLE = new TranslationTextComponent("gui.shaderscreen.name");
		
		private final ShadingTable.ShadingTE tile;
		
		public Provider(ShadingTable.ShadingTE tile) {
			this.tile = tile;
		}
		
		@Override
		public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
			STContainer container = new STContainer(id, playerInv, tile);
			container.setTile(tile);
			return container;
		}
		
		@Override
		public ITextComponent getDisplayName() {
			return TITLE;
		}
		
	}
}
