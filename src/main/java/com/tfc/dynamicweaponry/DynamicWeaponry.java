package com.tfc.dynamicweaponry;

import com.tfc.assortedutils.API.networking.AutomatedSimpleChannel;
import com.tfc.dynamicweaponry.block.ToolForgeContainer;
import com.tfc.dynamicweaponry.block.ToolForgeTileEntity;
import com.tfc.dynamicweaponry.client.Setup;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.item.tool.ToolComponent;
import com.tfc.dynamicweaponry.network.DataPacket;
import com.tfc.dynamicweaponry.network.PaintPixelPacket;
import com.tfc.dynamicweaponry.network.ToolPacket;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.registry.RegistryClient;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.tfc.dynamicweaponry.network.ToolForgeDataPacket;
//import com.tfc.dynamicweaponry.network.ToolPacket;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	
	public static final AutomatedSimpleChannel NETWORK_INSTANCE = AutomatedSimpleChannel.create(
			new ResourceLocation("dynamic_weaponry", "main"),
			() -> "1",
			"1"::equals,
			"1"::equals
	);

//	private static final ArrayList<ToolForgeDataPacket> packets = new ArrayList<>();
	
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	
	public DynamicWeaponry() {
		Registry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		MinecraftForge.EVENT_BUS.addListener(Loader::serverStartup);
		MinecraftForge.EVENT_BUS.addListener(Loader::onPlayerJoin);
		MinecraftForge.EVENT_BUS.addListener(Loader::tick);
		
		NETWORK_INSTANCE.registerPacket(DataPacket.class, DataPacket::new);
		NETWORK_INSTANCE.registerPacket(ToolPacket.class, ToolPacket::new, (packet, context) -> {
			Container container = context.get().getSender().openContainer;
			
			if (container instanceof ToolForgeContainer) {
				TileEntity te = ((ToolForgeContainer) container).world.getTileEntity(((ToolForgeContainer) container).pos);
				
				if (te instanceof ToolForgeTileEntity) {
					ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
					tileEntity.container.tool = packet.tool;
					tileEntity.tool = packet.tool;

					tileEntity.container.resync();
					
					tileEntity.markDirty();

//					packets.add(new ToolForgeDataPacket(tileEntity.tool, tileEntity.getPos(), (Chunk) tileEntity.getWorld().getChunk(tileEntity.getPos())));
					
					tileEntity.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);
//
//					for (PlayerEntity playerE : ((ToolForgeContainer) container).world.getPlayers()) {
//						ServerPlayerEntity player = (ServerPlayerEntity)playerE;
//						if (player.getPosition().distanceSq(((ToolForgeContainer) container).pos) <= 2048) {
//							IPacket updatePacket = tileEntity.getUpdatePacket();
//							player.connection.sendPacket(updatePacket);
//						}
//					}
				}
				context.get().setPacketHandled(true);
			}
		});
		NETWORK_INSTANCE.registerPacket(PaintPixelPacket.class, PaintPixelPacket::new, (packet, context) -> {
			Container container = context.get().getSender().openContainer;
			
			if (container instanceof ToolForgeContainer) {
				TileEntity te = ((ToolForgeContainer) container).world.getTileEntity(((ToolForgeContainer) container).pos);
				
				if (te instanceof ToolForgeTileEntity) {
					ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
					ToolComponent component = tileEntity.container.tool.getComponent(new ResourceLocation(packet.component));
					
					if (packet.resourceLocation == null) component.setPoint(new Point(packet.x, packet.y), null);
					else
						component.setPoint(new Point(packet.x, packet.y), new ResourceLocation(packet.resourceLocation));
					
					tileEntity.container.resync();
					
					tileEntity.markDirty();
					
					tileEntity.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);
				}
				context.get().setPacketHandled(true);
			}
		});
//		NETWORK_INSTANCE.registerPacket(ToolForgeDataPacket.class, ToolForgeDataPacket::new);
		
		if (FMLEnvironment.dist.isClient()) {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(Setup::setup);
			RegistryClient.CONTAINERS_SCREENS.register(FMLJavaModLoadingContext.get().getModEventBus());
		}
//		MinecraftForge.EVENT_BUS.addListener(DynamicWeaponry::tick);
	}

//	public static void sendPackets() {
//		for (ToolForgeDataPacket packet : packets) {
//			NETWORK_INSTANCE.send(
//					PacketDistributor.TRACKING_CHUNK.with(() -> packet.chunk),
//					packet
//			);
//		}
//	}
//
//	public static void tick(TickEvent.WorldTickEvent event) {
//		if (event.world.isRemote) return;
//
//		if (!packets.isEmpty()) {
//			sendPackets();
//			packets.clear();
//		}
//	}
}
