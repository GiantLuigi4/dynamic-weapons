package tfc.dynamic_weaponary.Tool;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.PixelStorage;

public class ModularItemRenderer extends ItemStackTileEntityRenderer {
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();
		PixelStorage image = new PixelStorage(16, 16);
		if (!itemStackIn.hasTag()) {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					image.setPixel(x, y, new DrawingUtils.ColorHelper(x * 15, y * 15, (16 - x) + y));
				}
			}
		} else {
			try {
				image = PixelStorage.fromString(itemStackIn.getTag().getString("image"));
			} catch (Exception err) {
			}
		}
		try {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					CubeColors.color = image.getPixel(x, y).getRGB();
					matrixStackIn.push();
					matrixStackIn.translate(x / 16D, 0, y / 16D);
					if (image.getPixel(x, y).getAlpha() != 0) {
						Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(Items.CUBE.get()), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
					}
					matrixStackIn.pop();
				}
			}
		} catch (Exception ignored) {
		}
		matrixStackIn.pop();
		super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}
