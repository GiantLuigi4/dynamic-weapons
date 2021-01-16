package com.tfc.dynamicweaponry.block;

import com.tfc.assortedutils.API.gui.container.SimpleContainer;
import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ToolForgeContainer extends SimpleContainer {
	public BlockPos pos;
	public World world;
	public Tool tool = new Tool(ToolForgeTileEntity.defaultTool);
	
	public ToolForgeContainer(int id, IInventory playerInv) {
		super(Registry.TOOL_FORGE_CONTAINER.get(), id);
	}
	
	public ToolForgeContainer(@Nullable ContainerType<?> type, int id, BlockPos pos, World world) {
		super(type, id);
		this.pos = pos;
		this.world = world;
	}
	
	@Override
	public CompoundNBT serialize() {
		CompoundNBT nbt = super.serialize();
		nbt.put("tool", tool.serialize());
		return nbt;
	}
}
