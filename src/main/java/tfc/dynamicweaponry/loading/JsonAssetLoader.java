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
	
	// client only
	private final Materials myMaterials = new Materials();
	
	// client and server materials
	private final Materials pairedMaterials = new Materials() {
		@Override
		public void add(Material matr) {
			Material m = myMaterials.get(matr.regName);
			if (m != null)
				super.add(new Material(m.clientMaterial, matr));
			else
				super.add(matr);
		}
		
		@Override
		public Material get(ResourceLocation regName) {
			Material m = super.get(regName);
			if (m != null) return m;
			return myMaterials.get(regName);
		}
	};
	
	@Override
	public Materials getMaterialHolder() {
		return pairedMaterials;
	}
	
	public void pair(Materials materials) {
		pairedMaterials.clear();
		for (ResourceLocation key : materials.keys())
			pairedMaterials.add(materials.get(key));
	}
}
