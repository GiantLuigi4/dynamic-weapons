package com.tfc.dynamicweaponry.network;

import com.tfc.assortedutils.API.networking.SimplePacket;
import com.tfc.dynamicweaponry.data.Loader;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;

public class DataPacket extends SimplePacket {
	public String[] materials;
	public String[] parts;
	public String tools;
	
	public DataPacket(PacketBuffer buffer) {
		readPacketData(buffer);
		if (FMLEnvironment.dist.isClient()) Loader.deserializePacket(this);
	}
	
	public DataPacket(String[] materials, String[] parts, String tools) {
		this.materials = materials;
		this.parts = parts;
		this.tools = tools;
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		materials = readArray(buf);
		parts = readArray(buf);
		tools = buf.readString();
	}
	
	private String[] readArray(PacketBuffer buf) {
		int len = buf.readInt();
		ArrayList<String> array = new ArrayList<>();
		for (int i = 0; i < len; i++)
			array.add(buf.readString());
		return array.toArray(new String[0]);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		writeArray(buf, materials);
		writeArray(buf, parts);
		buf.writeString(tools);
	}
	
	private void writeArray(PacketBuffer buf, String[] array) {
		buf.writeInt(array.length);
		for (String s : array) buf.writeString(s);
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	
	}
}
