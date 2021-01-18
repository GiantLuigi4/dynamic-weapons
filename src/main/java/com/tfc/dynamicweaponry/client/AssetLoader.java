package com.tfc.dynamicweaponry.client;

import com.google.gson.*;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AssetLoader implements IResourceManagerReloadListener {
	public static final AssetLoader INSTANCE = new AssetLoader();
	
	private static final Gson gson = new GsonBuilder().create();
	
	private final HashMap<ResourceLocation, ClientMaterialInfo> materialInfos = new HashMap<>();
	
	private static String readResource(IResource resource) {
		try {
			InputStream stream = resource.getInputStream();
			byte[] bytes = new byte[stream.available()];
			stream.read(bytes);
			stream.close();
			return new String(bytes);
		} catch (Throwable err) {
			err.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		Collection<ResourceLocation> resourceList = resourceManager.getAllResourceLocations("weaponry/materials", (file) -> file.endsWith(".json"));
		
		try {
			for (ResourceLocation resourceLocation : resourceList) {
				IResource resource = resourceManager.getResource(resourceLocation);
				
				try {
					parseMaterial(resource);
				} catch (Throwable err) {
					err.printStackTrace();
				}
			}
		} catch (Throwable err) {
			RuntimeException exception = new RuntimeException(err);
			exception.setStackTrace(err.getStackTrace());
			throw exception;
		}
	}
	
	public void parseMaterial(IResource resource) {
		parseMaterial(gson.fromJson(readResource(resource), JsonObject.class));
	}
	
	public void parseMaterial(JsonObject material) {
		ClientMaterialInfo mat = new ClientMaterialInfo(new ResourceLocation(material.getAsJsonPrimitive("item").getAsString()));
		
		if (material.has("color")) mat.color = material.getAsJsonPrimitive("color").getAsInt();
		if (material.has("colorBorder")) mat.color = material.getAsJsonPrimitive("colorBorder").getAsInt();
		else mat.setColor(mat.color);
		
		if (material.has("pattern") && FMLEnvironment.dist.isClient()) {
			JsonArray pattern = material.getAsJsonArray("pattern");
			ArrayList<int[]> arrayList = new ArrayList<>();
			
			for (JsonElement column : pattern) {
				if (column.isJsonArray()) {
					JsonArray row = column.getAsJsonArray();
					ArrayList<Integer> rowArrayList = new ArrayList<>();
					
					for (JsonElement jsonElement : row) {
						JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
						
						if (primitive.isNumber()) {
							rowArrayList.add(primitive.getAsInt());
						}
					}
					
					int[] intArray = new int[rowArrayList.size()];
					for (int index = 0; index < rowArrayList.size(); index++) intArray[index] = rowArrayList.get(index);
					arrayList.add(intArray);
				}
			}
			int[][] intArray = new int[arrayList.size()][];
			for (int index = 0; index < arrayList.size(); index++) intArray[index] = arrayList.get(index);
			mat.pattern = intArray;
		}
		
		if (materialInfos.containsKey(mat.item)) {
			ClientMaterialInfo info = materialInfos.get(mat.item);
			if (material.has("color")) info.color = mat.color;
			if (material.has("colorBorder")) info.colorBorder = mat.colorBorder;
			if (material.has("pattern")) info.pattern = mat.pattern;
			materialInfos.replace(mat.item, info);
		} else {
			materialInfos.put(mat.item, mat);
		}
	}
	
	public ClientMaterialInfo getMaterial(ResourceLocation item) {
		return materialInfos.getOrDefault(item, null);
	}
}
