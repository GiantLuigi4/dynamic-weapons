package tfc.dynamic_weaponary.Tool;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import tfc.dynamic_weaponary.Utils.DrawingUtils;

public class ModularItemRenderer extends ItemStackTileEntityRenderer {
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		DrawingUtils.drawTexturedRect(-0.5f, -0.5f, 0, 0, 256, 256, 32, 16, 1, 1, 1, 1);
		super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}
