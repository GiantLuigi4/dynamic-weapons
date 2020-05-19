package tfc.dynamic_weaponary.Packet;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class ImagePacket implements IPacket {
	public String info;
	
	public ImagePacket(String info) {
		this.info = info;
	}
	
	public ImagePacket(PacketBuffer buffer) {
		try {
			readPacketData(buffer);
		} catch (Exception ignored) {
		}
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		info = buf.readString();
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeString(info);
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	}
}
