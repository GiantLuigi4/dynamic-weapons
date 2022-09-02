package tfc.dynamicweaponry.screens.tool;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import tfc.dynamicweaponry.block.ToolForgeBlockEntity;

public class ToolForgeContainer extends AbstractContainerMenu {
	ToolForgeBlockEntity blockEntity;
	ContainerLevelAccess access;
	
	public static final MenuType<ToolForgeContainer> MENU_TYPE;
	
	static {
		//noinspection unchecked
		MenuType<ToolForgeContainer>[] type = new MenuType[1];
		type[0] = new MenuType<>((a, inv)-> new ToolForgeContainer(type[0], a, inv));
		MENU_TYPE = type[0];
	}
	
	public ToolForgeContainer(@Nullable MenuType<ToolForgeContainer> pMenuType, int pContainerId, Inventory playerInventory) {
		super(pMenuType, pContainerId);
		
		int i;
		int yOff = 56;
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOff));
			}
		}
		
		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142 + yOff));
		}
	}
	
	public ToolForgeContainer(@Nullable MenuType<ToolForgeContainer> pMenuType, int pContainerId, ToolForgeBlockEntity blockEntity, Inventory playerInventory) {
		this(pMenuType, pContainerId, playerInventory);
		this.blockEntity = blockEntity;
		access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
	}
	
	@Override
	public boolean stillValid(Player pPlayer) {
		if (blockEntity == null) return true;
		return AbstractContainerMenu.stillValid(access, pPlayer, blockEntity.getBlockState().getBlock());
	}
	
	public void setTile(ToolForgeBlockEntity forge) {
		this.blockEntity = forge;
		access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
	}
	
	public ToolForgeBlockEntity getTile() {
		return blockEntity;
	}
}
