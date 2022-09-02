package tfc.dynamicweaponry;

import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfc.dynamicweaponry.block.ToolForgeRenderer;
import tfc.dynamicweaponry.loading.JsonAssetLoader;
import tfc.dynamicweaponry.loading.JsonDataLoader;
import tfc.dynamicweaponry.loading.SendingHandler;
import tfc.dynamicweaponry.network.DynamicWeaponryNetworkRegistry;
import tfc.dynamicweaponry.screens.tool.ToolForgeContainer;
import tfc.dynamicweaponry.screens.tool.ToolForgeScreen;

@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	public static final JsonAssetLoader clientAssetLoader = new JsonAssetLoader(new GsonBuilder().setLenient().create());
	public static final JsonDataLoader serverDataLoader = new JsonDataLoader(new GsonBuilder().setLenient().create());
	
	public DynamicWeaponry() {
		Register.init();
		DynamicWeaponryNetworkRegistry.init();
		MinecraftForge.EVENT_BUS.addListener(DynamicWeaponry::addDataLoader);
		MinecraftForge.EVENT_BUS.addListener(SendingHandler::onTick);
		MinecraftForge.EVENT_BUS.addListener(SendingHandler::onPlayerJoin);
		if (FMLEnvironment.dist.isClient()) {
			ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
			reloadableResourceManager.registerReloadListener(clientAssetLoader);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(DynamicWeaponry::setup);
		}
	}
	
	public static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(Register.TOOL_FORGE_BLOCK.get(), RenderType.cutoutMipped());
		BlockEntityRenderers.register(Register.TOOL_FORGE_BLOCK_ENTITY.get(), (a) -> new ToolForgeRenderer());
		MenuScreens.register(ToolForgeContainer.MENU_TYPE, ToolForgeScreen::new);
	}
	
	public static void addDataLoader(AddReloadListenerEvent event) {
		event.addListener(serverDataLoader);
	}
}
