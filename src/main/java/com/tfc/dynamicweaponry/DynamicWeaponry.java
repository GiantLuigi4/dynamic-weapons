package com.tfc.dynamicweaponry;

import com.tfc.assortedutils.API.networking.AutomatedSimpleChannel;
import com.tfc.dynamicweaponry.block.ToolForgeContainer;
import com.tfc.dynamicweaponry.block.ToolForgeTileEntity;
import com.tfc.dynamicweaponry.client.AssetLoader;
import com.tfc.dynamicweaponry.client.Setup;
import com.tfc.dynamicweaponry.data.DataLoader;
import com.tfc.dynamicweaponry.item.tool.DynamicTool;
import com.tfc.dynamicweaponry.item.tool.ToolComponent;
import com.tfc.dynamicweaponry.material_effects.condtions.ConditionRegistryBuilder;
import com.tfc.dynamicweaponry.material_effects.effects.ToolEffectRegistryBuilder;
import com.tfc.dynamicweaponry.network.DataPacket;
import com.tfc.dynamicweaponry.network.PaintPixelPacket;
import com.tfc.dynamicweaponry.network.PaintToolPacket;
import com.tfc.dynamicweaponry.network.ToolPacket;
import com.tfc.dynamicweaponry.registry.DynamicWeaponryRegistries;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.registry.RegistryClient;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
	protected static final Logger LOGGER = LogManager.getLogger();
	
	public DynamicWeaponry() {
		Registry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		Registry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(DynamicWeaponry::createRegistries);
		
		DynamicWeaponryRegistries.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DynamicWeaponryRegistries.CONDITIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		MinecraftForge.EVENT_BUS.addListener(DataLoader::serverStartup);
		MinecraftForge.EVENT_BUS.addListener(DataLoader::onPlayerJoin);
		MinecraftForge.EVENT_BUS.addListener(DataLoader::tick);
		
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
		NETWORK_INSTANCE.registerPacket(PaintToolPacket.class, PaintToolPacket::new, (packet, context) -> {
			Container container = context.get().getSender().openContainer;
			
			if (container instanceof ToolForgeContainer) {
				TileEntity te = ((ToolForgeContainer) container).world.getTileEntity(((ToolForgeContainer) container).pos);
				
				if (te instanceof ToolForgeTileEntity) {
					ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
					ToolComponent component = tileEntity.container.tool.getComponent(new ResourceLocation(packet.component));
					
					if (packet.type == 0) {
						for (int x = packet.x; x <= packet.x2; x++) {
							for (int y = packet.y; y <= packet.y2; y++) {
								if (packet.resourceLocation == null) component.setPoint(new Point(x, y), null);
								else
									component.setPoint(new Point(x, y), new ResourceLocation(packet.resourceLocation));
							}
						}
					} else if (packet.type == 1) {
						Point src = new Point(packet.x, packet.y);
						Point end = new Point(packet.x2, packet.y2);
						int lastX = -1;
						int lastY = -1;
						
						int offX = packet.backwards ? (int) (Math.max(end.x, src.x)) : 0;
						int offY = packet.upsideDown ? (int) (Math.max(end.y, src.y)) : 0;
						
						boolean isVertical =
								(Math.abs(end.y - src.y) <
										Math.abs(end.x - src.x));
						
						for (int i = 0; i < 16; i++) {
							Point lerped = src.lerp(i / 16f, end);
							
							if (isVertical) {
								if (lerped.x == lastX) continue;
							} else {
								if (lerped.y == lastY) continue;
							}
							
							if (
									(lerped.x == end.x || lerped.y == end.y) &&
											(Math.abs(lastX - end.x) <= 1 ||
													Math.abs(lastY - end.y) <= 1)
							) {
								lastX = end.x;
								lastY = end.y;
								Point point = new Point(
										(packet.backwards ? -(lastX - src.x) : lastX) + offX,
										(packet.upsideDown ? -(lastY - src.y) : lastY) + offY
								);
								if (packet.resourceLocation == null) component.setPoint(point, null);
								else
									component.setPoint(point, new ResourceLocation(packet.resourceLocation));
								break;
							}
							
							lastX = lerped.x;
							lastY = lerped.y;
							
							Point point = new Point(
									(packet.backwards ? -(lastX - src.x) : lastX) + offX,
									(packet.upsideDown ? -(lastY - src.y) : lastY) + offY
							);
							if (packet.resourceLocation == null) component.setPoint(point, null);
							else
								component.setPoint(point, new ResourceLocation(packet.resourceLocation));
						}
						lastX = end.x;
						lastY = end.y;
						Point point = new Point(
								(packet.backwards ? -(lastX - src.x) : lastX) + offX,
								(packet.upsideDown ? -(lastY - src.y) : lastY) + offY
						);
						if (packet.resourceLocation == null) component.setPoint(point, null);
						else
							component.setPoint(point, new ResourceLocation(packet.resourceLocation));
					}
					
					tileEntity.container.resync();
					
					tileEntity.markDirty();
					
					tileEntity.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);
				}
				context.get().setPacketHandled(true);
			}
		});

		if (FMLEnvironment.dist.isClient()) {
			IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) Minecraft.getInstance().getResourceManager();
			reloadableResourceManager.addReloadListener(AssetLoader.INSTANCE);
			
			FMLJavaModLoadingContext.get().getModEventBus().addListener(Setup::setup);
			RegistryClient.CONTAINERS_SCREENS.register(FMLJavaModLoadingContext.get().getModEventBus());
			
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
		}
		
		MinecraftForge.EVENT_BUS.addListener(DynamicWeaponry::onAnvilUpdate);
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
	
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		if (event.getRight().getItem() instanceof DynamicTool) {
			event.setCanceled(event.isCancelable());
		}
		
		if (event.getPlayer() != null && !event.getPlayer().world.isRemote) return;
		if (event.getLeft().getItem() instanceof DynamicTool) {
			boolean repairable = event.getLeft().getItem().getIsRepairable(event.getLeft(), event.getRight());
			if (repairable) {
				int i = 0;
				
				ItemStack itemstack1 = event.getLeft().copy();
				ItemStack itemstack2 = event.getRight();
				int l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
				
				int i3;
				for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
					int j3 = itemstack1.getDamage() - l2;
					itemstack1.setDamage(j3);
					++i;
					l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
				}
				
				int j = itemstack1.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
				
				event.setResult(Event.Result.ALLOW);
				
				if (i3 >= 1) {
					event.setMaterialCost(Math.max(3, i3));
					event.setCost(j);
					event.setOutput(itemstack1);
				}
			}
		}
	}
	
	public static void createRegistries(RegistryEvent.NewRegistry event) {
		(new ToolEffectRegistryBuilder()).create();
		(new ConditionRegistryBuilder()).create();
	}
}
