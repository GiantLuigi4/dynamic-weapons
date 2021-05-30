package tfc.dynamicweaponry.client;

import com.google.gson.*;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.InputStream;
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
	
	//TODO: learn this
//	public static void bakeModelEvent(ModelBakeEvent event) {
//		{
//			IUnbakedModel model = event.getModelLoader().getModelOrMissing(new ResourceLocation("dynamic_weaponry:arrows/arrow"));
//			IBakedModel model1 = model.bakeModel(event.getModelLoader(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, new ResourceLocation("dynamic_weaponry:arrows/arrow"));
//			event.getModelRegistry().put(new ModelResourceLocation("dynamic_weaponry:arrows/arrow", "normal"), model1);
//		}
//		{
//			IUnbakedModel model = event.getModelLoader().getModelOrMissing(new ResourceLocation("dynamic_weaponry:arrows/spectral_arrow"));
//			IBakedModel model1 = model.bakeModel(event.getModelLoader(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, new ResourceLocation("dynamic_weaponry:arrows/arrow"));
//			event.getModelRegistry().put(new ModelResourceLocation("dynamic_weaponry:arrows/arrow", "spectral"), model1);
//		}
//	}
	
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
		ToolRenderer.getInstance().resetCaches();
		
		parseMaterial(gson.fromJson(readResource(resource), JsonObject.class));
	}
	
	public void parseMaterial(JsonObject material) {
		ClientMaterialInfo mat = new ClientMaterialInfo(new ResourceLocation(material.getAsJsonPrimitive("item").getAsString()));
		
		if (material.has("color")) mat.color = material.getAsJsonPrimitive("color").getAsInt();
		if (material.has("colorBorder")) mat.color = material.getAsJsonPrimitive("colorBorder").getAsInt();
		else mat.setColor(mat.color);
		
		if (material.has("pattern") && FMLEnvironment.dist.isClient()) {
			JsonArray pattern = material.getAsJsonArray("pattern");
			int[][] patternArray = new int[pattern.size()][];
			
			for (int columnIndex = 0; columnIndex < pattern.size(); columnIndex++) {
				JsonElement column = pattern.get(columnIndex);
				
				if (column.isJsonArray()) {
					JsonArray row = column.getAsJsonArray();
					int[] array = new int[row.size()];
					
					for (int index = 0; index < row.size(); index++)
						array[index] = row.get(index).getAsInt();
					
					patternArray[columnIndex] = array;
				}
			}
			mat.pattern = patternArray;
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
