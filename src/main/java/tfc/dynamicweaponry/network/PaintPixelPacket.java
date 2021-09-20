package tfc.dynamicweaponry.network;

import net.minecraft.network.PacketBuffer;
import tfc.assortedutils.API.networking.SimplePacket;

public class PaintPixelPacket extends SimplePacket {
	public int x, y;
	public String resourceLocation, component;
	
	public PaintPixelPacket(int x, int y, String resourceLocation, String component) {
		this.x = x;
		this.y = y;
		this.resourceLocation = resourceLocation;
		this.component = component;
	}
	
	public PaintPixelPacket(PacketBuffer buffer) {
		super(buffer);
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		x = buf.readInt();
		y = buf.readInt();
		resourceLocation = buf.readString(32767);
		if (resourceLocation.equals("null")) resourceLocation = null;
		component = buf.readString(32767);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		if (resourceLocation == null) buf.writeString("null");
		else buf.writeString(resourceLocation);
		buf.writeString(component);
	}
}
