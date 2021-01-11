package com.tfc.dynamicweaponry.data;

import com.google.gson.*;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loader implements IResourceManagerReloadListener {
	private static final Gson gson = new GsonBuilder().create();
	
	public static final Loader INSTANCE = new Loader();
	
	public static void serverStartup(FMLServerStartingEvent event) {
		((IReloadableResourceManager) event.getServer().getDataPackRegistries().getResourceManager()).addReloadListener(INSTANCE);
	}
	
	public final HashMap<ResourceLocation, PartType> partTypes = new HashMap<>();
	public final HashMap<ResourceLocation, ToolType> toolTypes = new HashMap<>();
	public final HashMap<ResourceLocation, Material> materials = new HashMap<>();
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		partTypes.clear();
		materials.clear();
		toolTypes.clear();
		
		for (String resourceNamespace : resourceManager.getResourceNamespaces()) {
			try {
				{
					List<IResource> resourceList = resourceManager.getAllResources(new ResourceLocation(resourceNamespace, "weaponry/materials"));
					for (IResource resource : resourceList) {
						if (resource.getLocation().toString().endsWith(".properties")) {
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
								
								materials.put(new ResourceLocation(resourceNamespace, resource.getLocation().getPath().substring("weaponry/materials/".length())), mat);
							} catch (Throwable err) {
								err.printStackTrace();
							}
						}
					}
				}
			} catch (Throwable ignored) {
			}
		}
		
		for (String resourceNamespace : resourceManager.getResourceNamespaces()) {
			try {
				{
					List<IResource> resourceList = resourceManager.getAllResources(new ResourceLocation(resourceNamespace, "weaponry/part_types"));
					for (IResource resource : resourceList) {
						if (resource.getLocation().toString().endsWith(".properties")) {
							try {
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
								
								PartType type = new PartType(new Point(minX, minY), new Point(maxX, maxY));
								
								for (int i = 0; i < reqPoints; i++) {
									int pointX = Integer.parseInt(properties.getValue("requiredPoint" + (i + 1) + "_X"));
									int pointY = Integer.parseInt(properties.getValue("requiredPoint" + (i + 1) + "_Y"));
									
									type.addRequiredPoint(new Point(pointX, pointY));
								}
								
								type.lock();
								
								partTypes.put(new ResourceLocation(resourceNamespace, resource.getLocation().getPath().substring("weaponry/part_types/".length())), type);
							} catch (Throwable err) {
								err.printStackTrace();
							}
						}
					}
				}
			} catch (Throwable ignored) {
			}
		}
		
		for (String resourceNamespace : resourceManager.getResourceNamespaces()) {
			try {
				{
					List<IResource> resourceList = resourceManager.getAllResources(new ResourceLocation(resourceNamespace, "weaponry/tool_types"));
					for (IResource resource : resourceList) {
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
										JsonArray array = object1.getAsJsonArray("part" + i);
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
										toolTypes.put(new ResourceLocation(resourceNamespace, resource.getLocation().getPath().substring("weaponry/tool_types/".length())), toolType);
									}
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
	}
}
