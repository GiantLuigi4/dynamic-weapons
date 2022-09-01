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

public class JsonAssetLoader extends SimpleJsonResourceReloadListener implements MaterialLoader{
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
		ClientMaterial material;
		int step = 0;
		try {
			ResourceLocation r = new ResourceLocation(object.getAsJsonPrimitive("name").getAsString());
			path = r + " in " + path;
			step = 1;
			int c = object.getAsJsonPrimitive("color").getAsInt();
			step = 2;
			int h = object.getAsJsonPrimitive("highlight").getAsInt();
			step = 3;
			float s = object.getAsJsonPrimitive("shininess").getAsFloat();
			step = 4;
			material = new ClientMaterial(c, h, s, r);
		} catch (NullPointerException err) {
			boolean unknown = false;
			System.out.println(element);
			NullPointerException ex = new NullPointerException("Cannot create material because " + path + " is missing a" + switch (step) {
				case 0 -> " string definition for name";
				case 1 -> "n int definition for color";
				case 2 -> "n int definition for highlight";
				case 3 -> " float definition for shininess";
				default -> "" + (unknown = true);
			});
			if (unknown) {
				err.printStackTrace();
			} else {
				ex.setStackTrace(err.getStackTrace());
				ex.printStackTrace();
			}
			return;
		} catch (Throwable err) {
			err.printStackTrace();
			return;
		}
		myMaterials.add(new Material(material));
	}
	
	private final Materials myMaterials = new Materials();
	
	@Override
	public Materials getMaterialHolder() {
		return myMaterials;
	}
}
