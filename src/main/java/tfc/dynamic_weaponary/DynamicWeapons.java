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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
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
import tfc.dynamic_weaponary.Block.ShadingTable.STContainer;
import tfc.dynamic_weaponary.Block.ShadingTable.STScreen;
import tfc.dynamic_weaponary.Block.ToolForge.ToolForge;
import tfc.dynamic_weaponary.Block.ToolForge.ToolForgeContainer;
import tfc.dynamic_weaponary.Block.ToolForge.ToolForgeScreen;
import tfc.dynamic_weaponary.Deffered_Registry.Blocks;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.Other.Config;
import tfc.dynamic_weaponary.Packet.ImagePacket;
import tfc.dynamic_weaponary.Utils.CFGHelper;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;
import tfc.dynamic_weaponary.Utils.Tool.Material;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania.*;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.Bumble_Zone.HoneyCrystalEvents;
import tfc.dynamic_weaponary.Utils.ToolEventHandler.PSI.PSIMetalEvents;

import java.io.File;
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
	
	static File path;
	
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
	
	static void handleConfig() {
		Config.createAndWrite(path, "minecraft", "" +
				new CFGHelper.matString(net.minecraft.item.Items.QUARTZ, 15, 10, 5 - 1.6, new DrawingUtils.ColorHelper(200, 200, 200).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.DIAMOND, 1561, 7, 2.7, new DrawingUtils.ColorHelper(88, 222, 227).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.EMERALD, 687, 6, 5 - 1.8, new DrawingUtils.ColorHelper(31, 173, 70).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.IRON_INGOT, 250, 6, 5 - 1.6, new DrawingUtils.ColorHelper(140, 140, 140).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.STONE, 131, 5, 5 - 0.6, new DrawingUtils.ColorHelper(90, 90, 90).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.COBBLESTONE, 131, 5, 5 - 0.6, new DrawingUtils.ColorHelper(90, 90, 90).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.DARK_OAK_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(72, 46, 22).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.ACACIA_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(169, 90, 50).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.JUNGLE_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(145, 103, 67).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.BIRCH_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(168, 153, 107).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.SPRUCE_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(118, 88, 53).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.OAK_PLANKS, 58, 4, 5 - 1.0, new DrawingUtils.ColorHelper(125, 101, 65).getRGB()).toString() +
				new CFGHelper.matString(net.minecraft.item.Items.GOLD_INGOT, 32, 4, 5 - 3.6, new DrawingUtils.ColorHelper(245, 244, 135).getRGB()).toString()
		);
		Config.createAndWrite(path, "dynamic_weaponry", "" +
				//BOTANIA
				new CFGHelper.matString("botania:quartz_blaze", 16, 8, 5 - 1.6f, new DrawingUtils.ColorHelper(248, 145, 74).getRGB()).toString() +
				new CFGHelper.matString("botania:quartz_dark", 16, 7, 5 - 1.7f, new DrawingUtils.ColorHelper(28, 27, 26).getRGB()).toString() +
				new CFGHelper.matString("botania:quartz_elven", 16, 6, 5 - 1.5f, new DrawingUtils.ColorHelper(186, 240, 188).getRGB()).toString() +
				new CFGHelper.matString("botania:quartz_sunny", 16, 7.5, 5 - 1.5f, new DrawingUtils.ColorHelper(223, 229, 125).getRGB()).toString() +
				new CFGHelper.matString("botania:quartz_mana", 4, 1.5, 5 - 0.3f, new DrawingUtils.ColorHelper(159, 205, 222).getRGB()).toString() +
				new CFGHelper.matString("botania:manasteel_ingot", 300, 6, 5 - 1.8f, new DrawingUtils.ColorHelper(110, 168, 241).getRGB()).toString() +
				new CFGHelper.matString("botania:elementium_ingot", 720, 5.5, 5 - 2.6f, new DrawingUtils.ColorHelper(245, 175, 213).getRGB()).toString() +
				new CFGHelper.matString("botania:terrasteel_ingot", 2300, 7, 5 - 2.5f, new DrawingUtils.ColorHelper(117, 225, 0).getRGB()).toString() +
				//BUMBLE ZONE
				new CFGHelper.matString("the_bumblezone:honey_crystal_shards", 14, 2, 5 - 5.5f, new DrawingUtils.ColorHelper(238, 194, 123).getRGB()).toString() +
				//PSI
				new CFGHelper.matString("psi:psimetal", 900, 6, 5 - 2.5f, new DrawingUtils.ColorHelper(145, 137, 210).getRGB()).toString()
		);
		Config.createAndWrite(path, "example", "" +
				//EXAMPLES
				new CFGHelper.matString("ex:no_methods", 2300, 7, 5 - 2.5f, new DrawingUtils.ColorHelper(117, 225, 0).getRGB()).toString() +
				new CFGHelper.matString("ex:methods", 2300, 7, 5 - 2.5f, new DrawingUtils.ColorHelper(117, 225, 0).getRGB(), "a", "b", "c").toString()
		);
		Config.readConfig(path, "minecraft");
		Config.readConfig(path, "dynamic_weaponry");
	}
	
	@SubscribeEvent
	public void serverSetupEvent(FMLDedicatedServerSetupEvent event) {
		try {
			path = new File(event.getServerSupplier().get().getDataDirectory() + "\\config\\dynamic_weaponry");
			handleConfig();
		} catch (Exception err) {
		}
	}
	
	//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class ClientSetup {
		@SubscribeEvent
		public static void setup(FMLClientSetupEvent event) {
			path = new File(event.getMinecraftSupplier().get().gameDir + "\\config\\dynamic_weaponry");
			handleConfig();
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
		//BOTANIA
		if (ModList.get().isLoaded("botania")) {
			tryRegisterModdedEvent(new ResourceLocation("botania:quartz_blaze"), "event_hitentity", BlazeQuartzEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:quartz_dark"), "event_hitentity", SmokeyQuartzEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:quartz_elven"), "event_hitentity", ElvenQuartzEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:quartz_sunny"), "event_hitentity", SunnyQuartzEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:quartz_mana"), "event_hitentity", ManaQuartzEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:terrasteel_ingot"), "InvTick", TerrasteelEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:elementium_ingot"), "InvTick", ElementiumEvents.consumer);
			tryRegisterModdedEvent(new ResourceLocation("botania:manasteel_ingot"), "InvTick", ManasteelEvents.consumer);
		}
		if (ModList.get().isLoaded("the_bumblezone")) {
			tryRegisterModdedEvent(new ResourceLocation("the_bumblezone:honey_crystal_shards"), "event_hitentity", HoneyCrystalEvents.consumer);
		}
		if (ModList.get().isLoaded("psi")) {
			tryRegisterModdedEvent(new ResourceLocation("psi:psimetal"), "InvTick", PSIMetalEvents.consumer);
		}
	}
}
