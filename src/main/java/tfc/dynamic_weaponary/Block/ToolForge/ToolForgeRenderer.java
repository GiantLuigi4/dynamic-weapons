package tfc.dynamic_weaponary.Block.ToolForge;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tfc.dynamic_weaponary.Deffered_Registry.Items;

@OnlyIn(Dist.CLIENT)
public class ToolForgeRenderer extends TileEntityRenderer<ToolForge.ForgeTE> {
	public ToolForgeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	
	@Override
	public void render(ToolForge.ForgeTE forgeTE, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.translate(0.495, 0.96, 0.505);
		matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
		matrixStackIn.scale(1 / 16f, 1 / 16f, 1 / 16f);
		matrixStackIn.scale(6, 6, 6);
		
		ItemStack renderStack = new ItemStack(Items.TOOL.get());
		try {
			CompoundNBT nbt = renderStack.getOrCreateTag();
			nbt.putString("image", forgeTE.image);
		} catch (Exception err) {
		}
		
		Minecraft.getInstance().getItemRenderer().renderItem(renderStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
	}
}
