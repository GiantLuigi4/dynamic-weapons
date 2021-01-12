package com.tfc.dynamicweaponry;

import com.tfc.dynamicweaponry.client.ToolCreationScreen;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.network.DataPacket;
import com.tfc.dynamicweaponry.registry.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	
	public static final SimpleChannel NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation("dynamic_weaponry", "main"),
			() -> "1",
			"1"::equals,
			"1"::equals
	);
	
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final KeyBinding key = new KeyBinding("test", GLFW.GLFW_KEY_X, "dynamic_weaponry") {
		@Override
		public void setPressed(boolean valueIn) {
			super.setPressed(valueIn);
			ToolCreationScreen screen = new ToolCreationScreen(StringTextComponent.EMPTY, Minecraft.getInstance());
			if (valueIn) Minecraft.getInstance().displayGuiScreen(screen);
		}
	};
	
	public DynamicWeaponry() {
		Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		MinecraftForge.EVENT_BUS.addListener(Loader::serverStartup);
		MinecraftForge.EVENT_BUS.addListener(Loader::onPlayerJoin);
		MinecraftForge.EVENT_BUS.addListener(Loader::tick);
		
		NETWORK_INSTANCE.registerMessage(1, DataPacket.class, DataPacket::writePacketData, DataPacket::new, (packet, context) -> {
			context.get().setPacketHandled(true);
		});
		
		if (FMLEnvironment.dist.isClient()) {
			ClientRegistry.registerKeyBinding(key);
		}
	}
}
