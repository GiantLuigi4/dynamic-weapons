package tfc.dynamicweaponry.network;

import com.tfc.assortedutils.API.networking.SimplePacket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.registry.Registry;

public class ToolPacket extends SimplePacket {
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
