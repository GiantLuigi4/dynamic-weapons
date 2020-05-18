package tfc.dynamic_weaponary.Packet;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class ImagePacket implements IPacket {
	String info;
	
	public ImagePacket(String info, PacketBuffer buffer) {
		this.info = info;
		try {
			writePacketData(buffer);
		} catch (Exception ignored) {
		}
	}
	
	public ImagePacket(PacketBuffer buffer) {
		try {
			readPacketData(buffer);
		} catch (Exception ignored) {
		}
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		info = buf.readString();
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeString(info);
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	}
}
