package tfc.dynamicweaponry.network.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import tfc.dynamicweaponry.block.ToolForgeBlockEntity;
import tfc.dynamicweaponry.network.DynamicWeaponryNetworkRegistry;
import tfc.dynamicweaponry.network.Packet;
import tfc.dynamicweaponry.screens.tool.ToolForgeContainer;
import tfc.dynamicweaponry.screens.tool.ToolForgeScreen;

public class SendBlockPosPacket extends Packet {
	BlockPos position;
	boolean request;
	
	public SendBlockPosPacket(boolean request, BlockPos pos) {
		this.request = request;
		this.position = pos;
	}
	
	public SendBlockPosPacket(FriendlyByteBuf buf) {
		super(buf);
		if (!(request = buf.readBoolean()))
			position = new BlockPos(buf.readIntLE(), buf.readInt(), buf.readIntLE());
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		super.write(buf);
		buf.writeBoolean(request);
		if (position != null) {
			buf.writeIntLE(position.getX());
			buf.writeInt(position.getY());
			buf.writeIntLE(position.getZ());
		}
	}
	
	@Override
	public void handle(NetworkEvent.Context ctx) {
		if (checkServer(ctx)) {
			ctx.enqueueWork(() -> {
				if (ctx.getSender().containerMenu instanceof ToolForgeContainer container) {
					DynamicWeaponryNetworkRegistry.NETWORK_INSTANCE.send(
							PacketDistributor.PLAYER.with(ctx::getSender),
							new SendBlockPosPacket(false, container.getTile().getBlockPos())
					);
				}
			});
		} else if (checkClient(ctx)) {
			Screen screen = Minecraft.getInstance().screen;
			if (screen != null) {
				if (screen instanceof ToolForgeScreen) {
					BlockEntity be = Minecraft.getInstance().level.getBlockEntity(position);
					if (be instanceof ToolForgeBlockEntity forge)
						((ToolForgeScreen) screen).getMenu().setTile(forge);
				}
			}
		}
		ctx.setPacketHandled(true);
	}
}
