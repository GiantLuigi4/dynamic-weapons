package tfc.dynamicweaponry;

import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfc.dynamicweaponry.item.Register;
import tfc.dynamicweaponry.loading.JsonAssetLoader;

@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	public static final JsonAssetLoader clientAssetLoader = new JsonAssetLoader(new GsonBuilder().setLenient().create());
	
	public DynamicWeaponry() {
		Register.init();
		if (FMLEnvironment.dist.isClient()) {
			ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
			reloadableResourceManager.registerReloadListener(clientAssetLoader);
		}
	}
}
