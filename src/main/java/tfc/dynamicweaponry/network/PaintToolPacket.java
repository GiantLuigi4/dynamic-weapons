package tfc.dynamicweaponry.network;

import com.tfc.assortedutils.API.networking.SimplePacket;
import net.minecraft.network.PacketBuffer;

public class PaintToolPacket extends SimplePacket {
	public int x, y, type, x2, y2;
	
	public boolean upsideDown;
	public boolean backwards;
	
	public String resourceLocation, component;
	
	public PaintToolPacket(int x, int y, int x2, int y2, int type, String resourceLocation, String component) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.x2 = x2;
		this.y2 = y2;
		this.resourceLocation = resourceLocation;
		this.component = component;
	}
	
	public PaintToolPacket(PacketBuffer buffer) {
		super(buffer);
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		upsideDown = false;
		x = buf.readInt();
		y = buf.readInt();
		type = buf.readInt();
		x2 = buf.readInt();
		y2 = buf.readInt();
		resourceLocation = buf.readString(32767);
		if (resourceLocation.equals("null")) resourceLocation = null;
		component = buf.readString(32767);
		if (type == 1) upsideDown = buf.readBoolean();
		if (type == 1) backwards = buf.readBoolean();
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeInt(Math.min(x, x2));
		buf.writeInt(Math.min(y, y2));
		buf.writeInt(type);
		buf.writeInt(Math.max(x, x2));
		buf.writeInt(Math.max(y, y2));
		if (resourceLocation == null) buf.writeString("null");
		else buf.writeString(resourceLocation);
		buf.writeString(component);
		if (type == 1) buf.writeBoolean(y > y2);
		if (type == 1) buf.writeBoolean(x > x2);
	}
}
