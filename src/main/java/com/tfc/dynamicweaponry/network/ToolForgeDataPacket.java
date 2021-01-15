package com.tfc.dynamicweaponry.network;

import com.tfc.assortedutils.API.networking.SimplePacket;
import com.tfc.dynamicweaponry.client.ToolForgeDataPacketHandler;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.tool.Tool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ToolForgeDataPacket extends SimplePacket {
	public Tool tool;
	public BlockPos pos;
	public Chunk chunk;
	
	public ToolForgeDataPacket(PacketBuffer buffer) {
		readPacketData(buffer);
	}
	
	public ToolForgeDataPacket(Tool tool, BlockPos pos, Chunk chunk) {
		this.tool = tool;
		this.pos = pos;
		this.chunk = chunk;
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.put("tool_info", buf.readCompoundTag());
		tool = new Tool(stack);
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		ToolForgeDataPacketHandler.handle(this);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeCompoundTag(tool.serialize());
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	}
}
