package tfc.dynamic_weaponary.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfc.dynamic_weaponary.Block.ToolForge.ToolForge;
import tfc.dynamic_weaponary.Block.ToolForge.ToolForgeRenderer;
import tfc.dynamic_weaponary.Deffered_Registry.Blocks;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.ShaderOrb.Colors;
import tfc.dynamic_weaponary.Tool.CubeColors;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
	public static void run(FMLClientSetupEvent event) {
		Minecraft.getInstance().getItemColors().register(new Colors(), new IItemProvider() {
			@Override
			public Item asItem() {
				return Items.SHADER_ORB.get();
			}
		});
		Minecraft.getInstance().getItemColors().register(new CubeColors(), new IItemProvider() {
			@Override
			public Item asItem() {
				return Items.CUBE.get();
			}
		});
		RenderTypeLookup.setRenderLayer(Blocks.TOOL_FORGE.get(), RenderType.getCutout());
		ClientRegistry.bindTileEntityRenderer((TileEntityType<ToolForge.ForgeTE>) TileEntities.TOOL_FORGE.get(), ToolForgeRenderer::new);
	}
}
