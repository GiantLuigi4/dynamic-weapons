package tfc.dynamicweaponry;

import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamicweaponry.block.ToolForgeRenderer;
import tfc.dynamicweaponry.loading.JsonAssetLoader;

@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	public static final JsonAssetLoader clientAssetLoader = new JsonAssetLoader(new GsonBuilder().setLenient().create());
	
	public DynamicWeaponry() {
		Register.init();
		if (FMLEnvironment.dist.isClient()) {
			ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
			reloadableResourceManager.registerReloadListener(clientAssetLoader);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(DynamicWeaponry::setup);
		}
	}
	
	public static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(Register.TOOL_FORGE_BLOCK.get(), RenderType.cutoutMipped());
		BlockEntityRenderers.register(Register.TOOL_FORGE_BLOCK_ENTITY.get(), (a) -> new ToolForgeRenderer());
	}
}
