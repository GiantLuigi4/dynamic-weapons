package com.tfc.dynamicweaponry.block;

import com.mojang.brigadier.StringReader;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.tool.Tool;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.ItemParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class ToolForgeTileEntity extends TileEntity {
	public final ToolForgeContainer container = new ToolForgeContainer(Registry.TOOL_FORGE_CONTAINER.get(), 0, pos, this.getWorld());
	public Tool tool;
	
	public static final ItemStack defaultTool = new ItemStack(Registry.DYNAMIC_TOOL.get());
	
	static {
		try {
			ItemParser parser = new ItemParser(new StringReader(
					"dynamic_weaponry:dynamic_tool{tool_info:{tool_type:\"dynamic_weaponry:single_head\",parts:{\"dynamic_weaponry:short_stick\":{name:\"dynamic_weaponry:short_stick\",points:[{material:\"minecraft:diamond\",x:0,y:2},{material:\"minecraft:diamond\",x:0,y:1},{material:\"minecraft:diamond\",x:0,y:0},{material:\"minecraft:diamond\",x:1,y:2},{material:\"minecraft:diamond\",x:1,y:1},{material:\"minecraft:diamond\",x:1,y:0},{material:\"minecraft:oak_planks\",x:2,y:3},{material:\"minecraft:oak_planks\",x:2,y:2},{material:\"minecraft:diamond\",x:2,y:1},{material:\"minecraft:diamond\",x:2,y:0},{material:\"minecraft:oak_planks\",x:3,y:4},{material:\"minecraft:oak_planks\",x:3,y:3},{material:\"minecraft:oak_planks\",x:3,y:2},{material:\"minecraft:oak_planks\",x:4,y:4},{material:\"minecraft:oak_planks\",x:4,y:3}]},\"dynamic_weaponry:sword_blade\":{name:\"dynamic_weaponry:sword_blade\",points:[{material:\"minecraft:diamond\",x:6,y:8},{material:\"minecraft:diamond\",x:6,y:7},{material:\"minecraft:diamond\",x:6,y:6},{material:\"minecraft:diamond\",x:7,y:9},{material:\"minecraft:diamond\",x:7,y:8},{material:\"minecraft:diamond\",x:7,y:7},{material:\"minecraft:diamond\",x:7,y:6},{material:\"minecraft:diamond\",x:8,y:10},{material:\"minecraft:diamond\",x:8,y:9},{material:\"minecraft:diamond\",x:8,y:8},{material:\"minecraft:diamond\",x:8,y:7},{material:\"minecraft:diamond\",x:8,y:6},{material:\"minecraft:diamond\",x:9,y:11},{material:\"minecraft:diamond\",x:9,y:10},{material:\"minecraft:diamond\",x:9,y:9},{material:\"minecraft:diamond\",x:9,y:8},{material:\"minecraft:diamond\",x:9,y:7},{material:\"minecraft:diamond\",x:10,y:12},{material:\"minecraft:diamond\",x:10,y:11},{material:\"minecraft:diamond\",x:10,y:10},{material:\"minecraft:diamond\",x:10,y:9},{material:\"minecraft:diamond\",x:10,y:8},{material:\"minecraft:diamond\",x:11,y:13},{material:\"minecraft:diamond\",x:11,y:12},{material:\"minecraft:diamond\",x:11,y:11},{material:\"minecraft:diamond\",x:11,y:10},{material:\"minecraft:diamond\",x:11,y:9},{material:\"minecraft:diamond\",x:12,y:14},{material:\"minecraft:diamond\",x:12,y:13},{material:\"minecraft:diamond\",x:12,y:12},{material:\"minecraft:diamond\",x:12,y:11},{material:\"minecraft:diamond\",x:12,y:10},{material:\"minecraft:diamond\",x:13,y:15},{material:\"minecraft:diamond\",x:13,y:14},{material:\"minecraft:diamond\",x:13,y:13},{material:\"minecraft:diamond\",x:13,y:12},{material:\"minecraft:diamond\",x:13,y:11},{material:\"minecraft:diamond\",x:14,y:15},{material:\"minecraft:diamond\",x:14,y:14},{material:\"minecraft:diamond\",x:14,y:13},{material:\"minecraft:diamond\",x:14,y:12},{material:\"minecraft:diamond\",x:15,y:15},{material:\"minecraft:diamond\",x:15,y:14},{material:\"minecraft:diamond\",x:15,y:13}]},\"dynamic_weaponry:sword_guard\":{name:\"dynamic_weaponry:sword_guard\",points:[{material:\"minecraft:gold_ingot\",x:2,y:9},{material:\"minecraft:gold_ingot\",x:2,y:8},{material:\"minecraft:gold_ingot\",x:3,y:9},{material:\"minecraft:gold_ingot\",x:3,y:8},{material:\"minecraft:gold_ingot\",x:3,y:7},{material:\"minecraft:gold_ingot\",x:3,y:6},{material:\"minecraft:gold_ingot\",x:4,y:8},{material:\"minecraft:gold_ingot\",x:4,y:7},{material:\"minecraft:gold_ingot\",x:4,y:6},{material:\"minecraft:gold_ingot\",x:4,y:5},{material:\"minecraft:gold_ingot\",x:5,y:7},{material:\"minecraft:gold_ingot\",x:5,y:6},{material:\"minecraft:gold_ingot\",x:5,y:5},{material:\"minecraft:gold_ingot\",x:5,y:4},{material:\"minecraft:gold_ingot\",x:6,y:5},{material:\"minecraft:gold_ingot\",x:6,y:4},{material:\"minecraft:gold_ingot\",x:6,y:3},{material:\"minecraft:gold_ingot\",x:7,y:5},{material:\"minecraft:gold_ingot\",x:7,y:4},{material:\"minecraft:gold_ingot\",x:7,y:3},{material:\"minecraft:gold_ingot\",x:8,y:4},{material:\"minecraft:gold_ingot\",x:8,y:3},{material:\"minecraft:gold_ingot\",x:8,y:2},{material:\"minecraft:gold_ingot\",x:9,y:3},{material:\"minecraft:gold_ingot\",x:9,y:2}]}}}}"
			), true);
			parser.readItem();
			parser.readNBT();
			defaultTool.getOrCreateTag().merge(parser.getNbt());
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
	
	public ToolForgeTileEntity() {
		super(Registry.TOOL_FORGE_TE.get());
		tool = new Tool(defaultTool);
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
//		ToolForgeTileEntity tileEntity = this;
//		ToolForgeDataPacket packet = new ToolForgeDataPacket(tileEntity.tool, tileEntity.getPos(), (Chunk) tileEntity.getWorld().getChunk(tileEntity.getPos()));
//		DynamicWeaponry.NETWORK_INSTANCE.send(
//				PacketDistributor.TRACKING_CHUNK.with(() -> packet.chunk),
//				packet
//		);
		return new SUpdateTileEntityPacket(pos, 0, serializeNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(this.getBlockState(), pkt.getNbtCompound());
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return getBlockState().getRenderShape(getWorld(), getPos()).getBoundingBox().offset(this.getPos());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return serializeNBT();
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt1.put("tool_info", nbt.getCompound("tool"));
		tool = new Tool(stack);
		container.tool = new Tool(stack);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("tool", container.tool.serialize());
		return compound;
	}
}
