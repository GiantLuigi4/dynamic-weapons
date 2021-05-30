package tfc.dynamicweaponry.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfc.dynamicweaponry.registry.Registry;

public class Setup {
	public static void setup(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(Registry.TOOL_FORGE.get(), RenderType.getCutout());
		ClientRegistry.bindTileEntityRenderer(Registry.TOOL_FORGE_TE.get(), ToolForgeRenderer::new);
	}
}
