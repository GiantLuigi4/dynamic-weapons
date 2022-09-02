package tfc.dynamicweaponry.network.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.loading.MaterialLoader;
import tfc.dynamicweaponry.network.Packet;
import tfc.dynamicweaponry.screens.tool.ToolForgeContainer;
import tfc.dynamicweaponry.tool.Tool;

public class SetPixelsPacket extends Packet {
	int[] pixels;
	ResourceLocation regName;
	int layer;
	
	public SetPixelsPacket(int layer, int[] pixels, ResourceLocation regName) {
		this.layer = layer;
		this.pixels = pixels;
		this.regName = regName;
	}
	
	public SetPixelsPacket(FriendlyByteBuf buf) {
		super(buf);
		layer = buf.readInt();
		pixels = new int[buf.readInt()];
		boolean alternator = false;
		for (int i = 0; i < pixels.length; i++) pixels[i] = buf.readInt();
		if (buf.readBoolean()) regName = buf.readResourceLocation();
		else regName = null;
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		boolean alternator = false;
		buf.writeInt(layer);
		buf.writeInt(pixels.length);
		for (int pixel : pixels) buf.writeInt(pixel);
		buf.writeBoolean(regName != null);
		if (regName != null) buf.writeResourceLocation(regName);
	}
	
	@Override
	public void handle(NetworkEvent.Context ctx) {
		if (checkServer(ctx)) {
			AbstractContainerMenu menu = ctx.getSender().containerMenu;
			if (menu instanceof ToolForgeContainer) {
				Tool tool = ((ToolForgeContainer) menu).getTile().getTool(ctx.getSender().level);
				MaterialLoader loader = ((IHoldADataLoader) ctx.getSender().level).myLoader();
				Material m = loader.getMaterialHolder().get(regName);
				for (int i = 0; i < pixels.length; i++) {
					int x = pixels[i] / 16;
					int y = pixels[i] - (x * 16);
					tool.getLayers()[layer].set(x, 15 - y, m);
				}
				((ToolForgeContainer) menu).getTile().setChanged();
			}
		}
	}
}
