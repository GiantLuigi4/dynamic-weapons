package com.tfc.dynamicweaponry.network;

import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.tool.Tool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class ToolPacket implements IPacket {
	public Tool tool;
	
	public ToolPacket(Tool tool) {
		this.tool = tool;
	}
	
	public ToolPacket(PacketBuffer buffer) {
		readPacketData(buffer);
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.put("tool_info", buf.readCompoundTag());
		tool = new Tool(stack);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeCompoundTag(tool.serialize());
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	}
}
