package tfc.dynamic_weaponary.Packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ImagePacketToClient extends ImagePacket {
	BlockPos pos;
	
	public ImagePacketToClient(String info, BlockPos pos) {
		super(info);
		this.pos = pos;
	}
	
	public ImagePacketToClient(PacketBuffer buffer) {
		super(buffer);
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		super.readPacketData(buf);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeString("" + pos.toLong() + "|");
		super.writePacketData(buf);
	}
}
