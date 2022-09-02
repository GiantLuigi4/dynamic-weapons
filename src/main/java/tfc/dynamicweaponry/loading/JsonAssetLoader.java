package tfc.dynamicweaponry.loading;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class JsonAssetLoader extends SimpleJsonResourceReloadListener implements MaterialLoader {
	Gson gson;
	
	public JsonAssetLoader(Gson p_10768_) {
		super(p_10768_, "weaponry/materials");
		this.gson = p_10768_;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
		myMaterials.clear();
		for (ResourceLocation location : pObject.keySet()) {
			JsonElement element = pObject.get(location);
			if (element instanceof JsonObject) parseElement(location, element);
			else if (element instanceof JsonArray) ((JsonArray) element).forEach((e) -> {
				parseElement(location, e);
			});
		}
	}
	
	public void parseElement(ResourceLocation location, JsonElement element) {
		JsonObject object;
		try {
			object = element.getAsJsonObject();
		} catch (Throwable err) {
			err.printStackTrace();
			return;
		}
		String path = location.toString();
		ClientMaterial material = ClientMaterial.parse(path, object);
		if (material == null) return;
		myMaterials.add(new Material(material));
	}
	
	private final Materials myMaterials = new Materials();
	
	@Override
	public Materials getMaterialHolder() {
		return myMaterials;
	}
}
