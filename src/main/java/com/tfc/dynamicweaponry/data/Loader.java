package com.tfc.dynamicweaponry.data;

import com.google.gson.*;
import com.tfc.dynamicweaponry.DynamicWeaponry;
import com.tfc.dynamicweaponry.network.DataPacket;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Loader implements IResourceManagerReloadListener {
	private static final Gson gson = new GsonBuilder().create();
	
	public static final Loader INSTANCE = new Loader();
	
	public static boolean needsGlobalRefresh = false;
	
	public static void serverStartup(AddReloadListenerEvent event) {
		event.addListener(INSTANCE);
	}
	
	public final HashMap<ResourceLocation, String> partTypesRaw = new HashMap<>();
	public final HashMap<ResourceLocation, String> toolTypesRaw = new HashMap<>();
	public final HashMap<ResourceLocation, String> materialsRaw = new HashMap<>();
	
	public final HashMap<ResourceLocation, PartType> partTypes = new HashMap<>();
	
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		resyncData((ServerPlayerEntity) event.getEntity());
	}
	
	public final HashMap<ResourceLocation, ToolType> toolTypes = new HashMap<>();
	
	public static void tick(TickEvent.WorldTickEvent event) {
		if (needsGlobalRefresh && event.world instanceof ServerWorld)
			for (PlayerEntity player : event.world.getPlayers()) {
				if (player instanceof ServerPlayerEntity)
					resyncData((ServerPlayerEntity) player);
			}
		else return;
		needsGlobalRefresh = false;
	}
	
	public final HashMap<ResourceLocation, Material> materials = new HashMap<>();
	
	public static void resyncData(ServerPlayerEntity entity) {
		if (FMLEnvironment.dist.isClient()) {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUniqueID().equals(entity.getUniqueID())) {
				return;
			}
		}
		
		JsonObject tools = new JsonObject();
		INSTANCE.toolTypesRaw.forEach((name, type) -> {
			JsonObject object = gson.fromJson(type, JsonObject.class);
			for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet())
				tools.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
		});
		
		ArrayList<String> parts = new ArrayList<>();
		INSTANCE.partTypesRaw.forEach((name, type) -> {
			parts.add(name + "\n" + type);
		});
		
		ArrayList<String> materials = new ArrayList<>();
		INSTANCE.materialsRaw.forEach((name, type) -> {
			materials.add(name + "\n" + type);
		});
		
		DynamicWeaponry.NETWORK_INSTANCE.send(
				PacketDistributor.PLAYER.with(() -> entity),
				new DataPacket(
						materials.toArray(new String[0]),
						parts.toArray(new String[0]),
						gson.toJson(tools)
				)
		);
	}
	
	private static void parseMaterial(ResourceLocation resourceLocation, String data) {
		JsonObject material = gson.fromJson(data, JsonObject.class);
		
		Material mat = new Material(
				material.getAsJsonPrimitive("color").getAsInt(),
				material.getAsJsonPrimitive("durability").getAsInt(),
				material.getAsJsonPrimitive("weight").getAsDouble(),
				material.getAsJsonPrimitive("efficiency").getAsDouble(),
				material.getAsJsonPrimitive("attack").getAsDouble(),
				new ResourceLocation(material.getAsJsonPrimitive("item").getAsString())
		);
		
		ResourceLocation location = new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath().substring("weaponry/materials/".length()).replace(".json", ""));
		INSTANCE.materials.put(location, mat);
		INSTANCE.materialsRaw.put(resourceLocation, data);
	}
	
	private static void parseToolTypes(ResourceLocation resourceLocation, String data) {
		JsonObject object = gson.fromJson(data, JsonObject.class);
		for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
			JsonObject object1 = stringJsonElementEntry.getValue().getAsJsonObject();
			int count = object1.getAsJsonPrimitive("part count").getAsInt();
			ToolType toolType = new ToolType();
			for (int i = 0; i < count; i++) {
				JsonArray array = object1.getAsJsonArray("part" + (i + 1));
				for (JsonElement element : array) {
					JsonObject object2 = element.getAsJsonObject();
					String name = object2.getAsJsonPrimitive("name").getAsString();
					ResourceLocation location = new ResourceLocation(name);
					ToolPart part = new ToolPart(INSTANCE.partTypes.get(location), i, toolType);
					
					if (object2.has("dependencies ")) {
						JsonArray dependencies = object2.getAsJsonArray("dependencies");
						ArrayList<ToolPart> deps = new ArrayList<>();
						for (JsonElement dependency : dependencies) {
							JsonObject dep = dependency.getAsJsonObject();
							int partNum = dep.getAsJsonPrimitive("part").getAsInt();
							String partName = dep.getAsJsonPrimitive("name").getAsString();
							ResourceLocation partLocation = new ResourceLocation(partName);
							deps.add(new ToolPart(INSTANCE.partTypes.get(partLocation), partNum, null));
						}
						part.dependencies = deps.toArray(new ToolPart[0]);
					} else {
						part.dependencies = new ToolPart[0];
					}
					
					if (object2.has("incompatibilities")) {
						JsonArray incompatibilities = object2.getAsJsonArray("incompatibilities");
						ArrayList<ToolPart> incompats = new ArrayList<>();
						for (JsonElement incompat : incompatibilities) {
							JsonObject incompatibility = incompat.getAsJsonObject();
							int partNum = incompatibility.getAsJsonPrimitive("part").getAsInt();
							String partName = incompatibility.getAsJsonPrimitive("name").getAsString();
							ResourceLocation partLocation = new ResourceLocation(partName);
							incompats.add(new ToolPart(INSTANCE.partTypes.get(partLocation), partNum, null));
						}
						part.incompatibilities = incompats.toArray(new ToolPart[0]);
					} else {
						part.incompatibilities = new ToolPart[0];
					}
					
					toolType.addPart(part);
				}
			}
			ResourceLocation location = new ResourceLocation(stringJsonElementEntry.getKey());
			INSTANCE.toolTypes.put(location, toolType);
		}
		INSTANCE.toolTypesRaw.put(resourceLocation, data);
	}
	
	private static void parsePartType(ResourceLocation location, String data) {
		Properties properties = new Properties(data);
		int reqPoints = Integer.parseInt(properties.getValue("requiredPoints"));
		int minX = Integer.parseInt(properties.getValue("minX"));
		int minY = Integer.parseInt(properties.getValue("minY"));
		int maxX = Integer.parseInt(properties.getValue("maxX"));
		int maxY = Integer.parseInt(properties.getValue("maxY"));
		
		PartType type = new PartType(
				location,
				new Point(minX, minY),
				new Point(maxX, maxY),
				Integer.parseInt(properties.getValue("order"))
		);
		
		for (int i = 0; i < reqPoints; i++) {
			int pointX = Integer.parseInt(properties.getValue("requiredPoint" + (i + 1) + "_X"));
			int pointY = Integer.parseInt(properties.getValue("requiredPoint" + (i + 1) + "_Y"));
			
			type.addRequiredPoint(new Point(pointX, pointY));
		}
		
		type.lock();

//		ResourceLocation rlocation = new ResourceLocation(location.getNamespace(), location.getPath().substring("weaponry/part_types/".length()).replace(".properties", ""));
		INSTANCE.partTypes.put(location, type);
		INSTANCE.partTypesRaw.put(location, data);
	}
	
	public static void deserializePacket(DataPacket packet) {
		for (String data : packet.materials) {
			String[] strings = data.replace("\r", "").split("\n", 2);
			String regName = strings[0];
			String dataS = strings[1];
			parseMaterial(new ResourceLocation(regName), dataS);
		}
		for (String data : packet.parts) {
			String[] strings = data.replace("\r", "").split("\n", 2);
			String regName = strings[0];
			String dataS = strings[1];
			parsePartType(new ResourceLocation(regName), dataS);
		}
		{
			parseToolTypes(new ResourceLocation("server:tools"), packet.tools);
		}
	}
	
	public Material getMaterial(ResourceLocation location) {
		if (materials.containsKey(location))
			return materials.get(location);
		
		AtomicReference<Material> materialAtomicReference = new AtomicReference<>();
		try {
			materials.forEach((name, mat) -> {
				if (mat.item.equals(location)) {
					materialAtomicReference.set(mat);
				}
			});
		} catch (Throwable ignored) {
		}
		return materialAtomicReference.get();
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		partTypes.clear();
		partTypesRaw.clear();
		materials.clear();
		materialsRaw.clear();
		toolTypes.clear();
		toolTypesRaw.clear();
		
		try {
			{
				Collection<ResourceLocation> resourceList = resourceManager.getAllResourceLocations("weaponry/materials", (file) -> file.endsWith(".json"));
				for (ResourceLocation resourceLocation : resourceList) {
					IResource resource = resourceManager.getResource(resourceLocation);
					if (resource.getLocation().toString().endsWith(".json")) {
						try {
							InputStream stream = resource.getInputStream();
							byte[] bytes = new byte[stream.available()];
							stream.read(bytes);
							stream.close();
							parseMaterial(resourceLocation, new String(bytes));
						} catch (Throwable err) {
							err.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable ignored) {
			ignored.printStackTrace();
		}
		
		try {
			{
				Collection<ResourceLocation> resourceList = resourceManager.getAllResourceLocations("weaponry/part_types", (file) -> file.endsWith(".properties"));
				for (ResourceLocation resourceLocation : resourceList) {
					IResource resource = resourceManager.getResource(resourceLocation);
					if (resource.getLocation().toString().endsWith(".properties")) {
						try {
							ResourceLocation location = new ResourceLocation(resource.getLocation().getNamespace(), resource.getLocation().getPath().substring("weaponry/part_types/".length()).replace(".properties", ""));
							InputStream stream = resource.getInputStream();
							byte[] bytes = new byte[stream.available()];
							stream.read(bytes);
							stream.close();
							parsePartType(location, new String(bytes));
						} catch (Throwable err) {
							err.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable ignored) {
		}
		
		try {
			{
				Collection<ResourceLocation> resourceList = resourceManager.getAllResourceLocations("weaponry/tool_types", (file) -> file.endsWith(".json"));
				for (ResourceLocation resourceLocation : resourceList) {
					IResource resource = resourceManager.getResource(resourceLocation);
					if (resource.getLocation().toString().endsWith(".json")) {
						try {
							InputStream stream = resource.getInputStream();
							byte[] bytes = new byte[stream.available()];
							stream.read(bytes);
							stream.close();
							parseToolTypes(resource.getLocation(), new String(bytes));
						} catch (Throwable err) {
							err.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable ignored) {
		}
		
		needsGlobalRefresh = true;
	}
}
