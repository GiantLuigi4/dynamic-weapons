package com.tfc.assortedutils.packets.container;

import com.tfc.assortedutils.API.gui.container.SimpleContainer;
import com.tfc.assortedutils.API.gui.screen.SimpleContainerScreen;
import com.tfc.assortedutils.API.gui.screen.SimpleContainerScreenFactory;
import com.tfc.assortedutils.custom_registries.simple_container_screens.SimpleContainerScreenRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerPacket implements IPacket {
	String regName;
	CompoundNBT containerNBT;
	
	public ContainerPacket(SimpleContainer container, ContainerType<?> type) {
		regName = type.getRegistryName().toString();
		containerNBT = container.serialize();
	}
	
	public ContainerPacket(PacketBuffer buf) {
		readPacketData(buf);
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) {
		read(buf);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void read(PacketBuffer buf) {
		if (FMLEnvironment.dist.isClient()) {
			regName = buf.readString();
			containerNBT = buf.readCompoundTag();
			ContainerType<?> type = ForgeRegistries.CONTAINERS.getValue(new ResourceLocation(regName));
			SimpleContainerScreenFactory factory = SimpleContainerScreenRegistry.get(new ResourceLocation(regName));
			SimpleContainerScreen<?> screen;
			if (factory == null)
				screen = new SimpleContainerScreen<>(StringTextComponent.EMPTY, Minecraft.getInstance(), type);
				//TODO: make the server send the text component
			else screen = factory.create(StringTextComponent.EMPTY, Minecraft.getInstance(), type);
			screen.deseralize(containerNBT);
			
			Screen old = Minecraft.getInstance().currentScreen;
			net.minecraftforge.client.event.GuiOpenEvent event = new net.minecraftforge.client.event.GuiOpenEvent(screen);
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
			
			Screen guiScreenIn = event.getGui();
			if (old != null && guiScreenIn != old)
				old.onClose();
			
			guiScreenIn.init(Minecraft.getInstance(), Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight());
			
			Minecraft.getInstance().mouseHelper.ungrabMouse();
			KeyBinding.unPressAllKeys();
			
			Minecraft.getInstance().currentScreen = guiScreenIn;
		}
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) {
		buf.writeString(regName);
		buf.writeCompoundTag(containerNBT);
	}
	
	@Override
	public void processPacket(INetHandler handler) {
	}
}
