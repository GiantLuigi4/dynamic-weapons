package com.tfc.dynamicweaponry.block;

import com.tfc.assortedutils.API.gui.container.SimpleContainer;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.tool.Tool;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ToolForgeContainer extends SimpleContainer {
	public BlockPos pos;
	public World world;
	public Tool tool;
	
	public ToolForgeContainer(int id, IInventory playerInv) {
		super(Registry.TOOL_FORGE_CONTAINER.get(), id);
	}
	
	public ToolForgeContainer(@Nullable ContainerType<?> type, int id, BlockPos pos, World world) {
		super(type, id);
		this.pos = pos;
		this.world = world;
	}
}
