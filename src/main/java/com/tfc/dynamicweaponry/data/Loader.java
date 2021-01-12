package com.tfc.dynamicweaponry.data;

import com.google.gson.*;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Loader implements IResourceManagerReloadListener {
	private static final Gson gson = new GsonBuilder().create();
	
	public static final Loader INSTANCE = new Loader();
	
	public static void serverStartup(AddReloadListenerEvent event) {
		event.addListener(INSTANCE);
	}
	
	public final HashMap<ResourceLocation, PartType> partTypes = new HashMap<>();
	public final HashMap<ResourceLocation, ToolType> toolTypes = new HashMap<>();
	public final HashMap<ResourceLocation, Material> materials = new HashMap<>();
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		partTypes.clear();
		materials.clear();
		toolTypes.clear();
		
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
							JsonObject material = gson.fromJson(new String(bytes), JsonObject.class);
							
							Material mat = new Material(
									material.getAsJsonPrimitive("color").getAsInt(),
									material.getAsJsonPrimitive("durability").getAsInt(),
									material.getAsJsonPrimitive("weight").getAsDouble(),
									material.getAsJsonPrimitive("efficiency").getAsDouble(),
									material.getAsJsonPrimitive("attack").getAsDouble(),
									new ResourceLocation(material.getAsJsonPrimitive("item").getAsString())
							);
							
							materials.put(new ResourceLocation(resource.getLocation().getNamespace(), resource.getLocation().getPath().substring("weaponry/materials/".length()).replace(".json", "")), mat);
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
							Properties properties = new Properties(new String(bytes));
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
							
							partTypes.put(location, type);
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
							JsonObject object = gson.fromJson(new String(bytes), JsonObject.class);
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
										ToolPart part = new ToolPart(partTypes.get(location), i, toolType);
										
										if (object2.has("dependencies ")) {
											JsonArray dependencies = object2.getAsJsonArray("dependencies");
											ArrayList<ToolPart> deps = new ArrayList<>();
											for (JsonElement dependency : dependencies) {
												JsonObject dep = dependency.getAsJsonObject();
												int partNum = dep.getAsJsonPrimitive("part").getAsInt();
												String partName = dep.getAsJsonPrimitive("name").getAsString();
												ResourceLocation partLocation = new ResourceLocation(partName);
												deps.add(new ToolPart(partTypes.get(partLocation), partNum, null));
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
												incompats.add(new ToolPart(partTypes.get(partLocation), partNum, null));
											}
											part.incompatibilities = incompats.toArray(new ToolPart[0]);
										} else {
											part.incompatibilities = new ToolPart[0];
										}
										
										toolType.addPart(part);
									}
								}
								toolTypes.put(new ResourceLocation(resource.getLocation().getNamespace(), stringJsonElementEntry.getKey()), toolType);
							}
						} catch (Throwable err) {
							err.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable ignored) {
		}
	}
	
	public Material getMaterial(ResourceLocation location) {
		if (materials.containsKey(location))
			return materials.get(location);
		
		AtomicReference<Material> materialAtomicReference = new AtomicReference<>();
		materials.forEach((name, mat) -> {
			if (mat.item.equals(location)) {
				materialAtomicReference.set(mat);
			}
		});
		return materialAtomicReference.get();
	}
}
