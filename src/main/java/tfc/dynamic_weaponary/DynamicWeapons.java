package tfc.dynamic_weaponary;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfc.dynamic_weaponary.Deffered_Registry.Blocks;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.Tool.IItemTeir;
import tfc.dynamic_weaponary.Tool.ModularItem;
import tfc.dynamic_weaponary.Tool.Properties;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.PixelStorage;
import tfc.dynamic_weaponary.block.GUI.Shading.STContainer;
import tfc.dynamic_weaponary.block.GUI.Shading.STScreen;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dynamic_weaponry")
public class DynamicWeapons {
	
	public static String ModID = "dynamic_weaponry";
	// Directly reference a log4j logger.
	public static Logger LOGGER = LogManager.getLogger();
	
	public DynamicWeapons() {
		PixelStorage.Pixel pixel = new PixelStorage.Pixel(0, 0, new DrawingUtils.ColorHelper(0, 255, 0));
		LOGGER.log(Level.INFO, "" + pixel.toString());
		LOGGER.log(Level.INFO, "" + PixelStorage.pixelFromString(pixel.toString()).toString());
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		
		Blocks.BLOCKS.register(bus);
		Items.ITEMS.register(bus);
		TileEntities.TILE_ENTITIES.register(bus);
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
//		LOGGER.info("HELLO FROM PREINIT");
//		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
//		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
	}
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
//		InterModComms.sendTo("dynamic_weaponary", "helloworld", () -> {
//			LOGGER.info("Hello world from the MDK");
//			return "Hello world";
//		});
	}
	
	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
//		LOGGER.info("Got IMC {}", event.getIMCStream().
//				map(m -> m.getMessageSupplier().get()).
//				collect(Collectors.toList()));
	}
	
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
//		LOGGER.info("HELLO from server starting");
	}
	
	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonRegistry {
		@SubscribeEvent
		public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegister) {
			itemRegister.getRegistry().register(new ModularItem(1, 1, new IItemTeir(), null, new Properties()).setRegistryName(ModID + ":tool"));
		}
		
		//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
		@SubscribeEvent
		public static void regContainerType(RegistryEvent.Register<ContainerType<?>> event) {
			event.getRegistry().register(
					(STContainer.TYPE = new ContainerType<>(STContainer::new)).setRegistryName("dynamic_weaponry", "stcontainer")
			);
		}
	}
	
	//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class ClientSetup {
		@SubscribeEvent
		public static void setup(FMLClientSetupEvent event) {
			ScreenManager.registerFactory(STContainer.TYPE, STScreen::new);
		}
	}
}
