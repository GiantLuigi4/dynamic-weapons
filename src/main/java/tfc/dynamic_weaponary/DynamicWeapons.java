package tfc.dynamic_weaponary;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.TriConsumer;
import tfc.dynamic_weaponary.Deffered_Registry.Blocks;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.Packet.ImagePacket;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;
import tfc.dynamic_weaponary.Utils.Tool.Material;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.BlazeQuartzEvents;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.ElvenQuartzEvents;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.SmokeyQuartzEvents;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.SunnyQuartzEvents;
import tfc.dynamic_weaponary.block.ShadingTable.STContainer;
import tfc.dynamic_weaponary.block.ShadingTable.STScreen;
import tfc.dynamic_weaponary.block.ToolForge.ToolForge;
import tfc.dynamic_weaponary.block.ToolForge.ToolForgeContainer;
import tfc.dynamic_weaponary.block.ToolForge.ToolForgeScreen;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dynamic_weaponry")
public class DynamicWeapons {
	
	public static String ModID = "dynamic_weaponry";
	public static Logger LOGGER = LogManager.getLogger();
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ModID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	
	public DynamicWeapons() {
		PixelStorage.Pixel pixel = new PixelStorage.Pixel(0, 0, new DrawingUtils.ColorHelper(0, 255, 0));
		LOGGER.log(Level.INFO, "source_test" + pixel.toString());
		LOGGER.log(Level.INFO, "result_test" + PixelStorage.pixelFromString(pixel.toString()).toString());
		
		INSTANCE.registerMessage(
				0,
				ImagePacket.class,
				(imagePacket, buffer) -> imagePacket.writePacketData(buffer),
				buffer -> new ImagePacket(buffer),
				new BiConsumer<ImagePacket, Supplier<NetworkEvent.Context>>() {
					@Override
					public void accept(ImagePacket imagePacket, Supplier<NetworkEvent.Context> contextSupplier) {
						ToolForge.ForgeTE te = ((ToolForgeContainer) contextSupplier.get().getSender().openContainer).tile;
						te.image = imagePacket.info;
						te.markDirty();
						te.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);
					}
				}
		);
		
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
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static void tryRegisterModdedMaterial(ResourceLocation material, Material mat) {
		if (ForgeRegistries.ITEMS.containsKey(material)) {
			MaterialList.RegisterMaterial(ForgeRegistries.ITEMS.getValue(material), mat);
		}
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		tfc.dynamic_weaponary.Events.ClientSetup.run(event);
	}
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
	}
	
	private void processIMC(final InterModProcessEvent event) {
	}
	
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}
	
	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonRegistry {
		@SubscribeEvent
		public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegister) {
		}
		
		//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
		@SubscribeEvent
		public static void regContainerType(RegistryEvent.Register<ContainerType<?>> event) {
			event.getRegistry().register(
					(STContainer.TYPE = new ContainerType<>(STContainer::new)).setRegistryName("dynamic_weaponry", "stcontainer")
			);
			event.getRegistry().register(
					(ToolForgeContainer.TYPE = new ContainerType<>(ToolForgeContainer::new)).setRegistryName("dynamic_weaponry", "tfcontainer")
			);
		}
	}
	
	//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class ClientSetup {
		@SubscribeEvent
		public static void setup(FMLClientSetupEvent event) {
			ScreenManager.registerFactory(STContainer.TYPE, STScreen::new);
			ScreenManager.registerFactory(ToolForgeContainer.TYPE, ToolForgeScreen::new);
		}
	}
	
	public static void tryRegisterModdedEvent(ResourceLocation material, String type, TriConsumer event) {
		if (ForgeRegistries.ITEMS.containsKey(material)) {
			EventRegistry.registerEvent(material, type, event);
		}
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		//GOLD
		MaterialList.RegisterMaterial(net.minecraft.item.Items.GOLD_INGOT, new Material(32, 4, 5 - 3.6f, new DrawingUtils.ColorHelper(245, 244, 135)));
		//WOODS
		MaterialList.RegisterMaterial(net.minecraft.item.Items.OAK_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(125, 101, 65)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.SPRUCE_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(118, 88, 53)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.BIRCH_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(168, 153, 107)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.JUNGLE_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(145, 103, 67)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.ACACIA_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(169, 90, 50)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.DARK_OAK_PLANKS, new Material(58, 4, 5 - 1.0f, new DrawingUtils.ColorHelper(72, 46, 22)));
		//STONE
		MaterialList.RegisterMaterial(net.minecraft.item.Items.COBBLESTONE, new Material(131, 5, 5 - 0.6f, new DrawingUtils.ColorHelper(90, 90, 90)));
		MaterialList.RegisterMaterial(net.minecraft.item.Items.STONE, new Material(131, 5, 5 - 0.6f, new DrawingUtils.ColorHelper(90, 90, 90)));
		//IRON
		MaterialList.RegisterMaterial(net.minecraft.item.Items.IRON_INGOT, new Material(250, 6, 5 - 1.6f, new DrawingUtils.ColorHelper(140, 140, 140)));
		//EMERALD
		MaterialList.RegisterMaterial(net.minecraft.item.Items.EMERALD, new Material(687, 6, 5 - 1.8f, new DrawingUtils.ColorHelper(31, 173, 70)));
		//DIAMOND
		MaterialList.RegisterMaterial(net.minecraft.item.Items.DIAMOND, new Material(1561, 7, 5 - 2.3f, new DrawingUtils.ColorHelper(88, 222, 227)));
		//QUARTZ
		MaterialList.RegisterMaterial(net.minecraft.item.Items.QUARTZ, new Material(15, 10, 5 - 1.6f, new DrawingUtils.ColorHelper(200, 200, 200)));
		
		//BOTANIA
		tryRegisterModdedMaterial(new ResourceLocation("botania:quartz_blaze"), new Material(16, 8, 5 - 1.6f, new DrawingUtils.ColorHelper(248, 145, 74)));
		tryRegisterModdedEvent(new ResourceLocation("botania:quartz_blaze"), "hit_entity", BlazeQuartzEvents.hitEvent);
		tryRegisterModdedMaterial(new ResourceLocation("botania:quartz_dark"), new Material(16, 7, 5 - 1.7f, new DrawingUtils.ColorHelper(28, 27, 26)));
		tryRegisterModdedEvent(new ResourceLocation("botania:quartz_dark"), "hit_entity", SmokeyQuartzEvents.hitEvent);
		tryRegisterModdedMaterial(new ResourceLocation("botania:quartz_elven"), new Material(16, 6, 5 - 1.5f, new DrawingUtils.ColorHelper(186, 240, 188)));
		tryRegisterModdedEvent(new ResourceLocation("botania:quartz_elven"), "hit_entity", ElvenQuartzEvents.hitEvent);
		tryRegisterModdedMaterial(new ResourceLocation("botania:quartz_sunny"), new Material(16, 7, 5 - 1.5f, new DrawingUtils.ColorHelper(223, 229, 125)));
		tryRegisterModdedEvent(new ResourceLocation("botania:quartz_sunny"), "hit_entity", SunnyQuartzEvents.hitEvent);
	}
}
