package com.tfc.dynamicweaponry.block;

import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.tool.Tool;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class ToolForgeTileEntity extends TileEntity {
	public final ToolForgeContainer container = new ToolForgeContainer(Registry.TOOL_FORGE_CONTAINER.get(), 0, pos, this.getWorld());
	public Tool tool;
	
	public ToolForgeTileEntity() {
		super(Registry.TOOL_FORGE_TE.get());
	}
	
	/**
	 * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
	 * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
	 */
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt.put("tool_info", nbt1);
		tool = new Tool(stack);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("tool", tool.serialize());
		return super.write(compound);
	}
}
