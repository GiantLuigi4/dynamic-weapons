package tfc.dynamic_weaponary.Tool;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.MaterialBasedPixelStorage;
import tfc.dynamic_weaponary.Utils.Optimization.ImageList;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ModularItemRenderer extends ItemStackTileEntityRenderer {
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();
		MaterialBasedPixelStorage image = new MaterialBasedPixelStorage(16, 16);
		if (!itemStackIn.hasTag()) {
			Object[] materials = MaterialList.materialHashMap.keySet().toArray();
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					image.setPixel(x, y, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(materials[new Random(x * y).nextInt(materials.length - 1)].toString()))));
				}
			}
		} else {
			try {
				image = ImageList.get(itemStackIn.getTag().getString("image"));
				if (image == null) {
					image = ImageList.addOrReplaceImage(itemStackIn.getTag().getString("image"));
				}
			} catch (Exception err) {
			}
		}
		try {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					DrawingUtils.ColorHelper color = image.getPixel(x, y);
					matrixStackIn.push();
					matrixStackIn.translate(x / 16D, 0, y / 16D);
					if (color.getAlpha() != 0 && (color.getRed() != 0 || color.getGreen() != 0 || color.getBlue() != 0)) {
						CubeColors.color = color.getRGB();
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
