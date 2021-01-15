package com.tfc.dynamicweaponry.client;

import com.tfc.dynamicweaponry.block.ToolForgeTileEntity;
import com.tfc.dynamicweaponry.network.ToolForgeDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ToolForgeDataPacketHandler {
	public static void handle(ToolForgeDataPacket packet) {
		World world = Minecraft.getInstance().player.world;
		TileEntity te = world.getTileEntity(packet.pos);
		if (te instanceof ToolForgeTileEntity) {
			ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
			tileEntity.tool = packet.tool;
			tileEntity.container.tool = packet.tool;
		}
	}
}
