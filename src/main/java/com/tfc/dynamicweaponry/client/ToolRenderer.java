package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.assortedutils.utils.Color;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.tool.MaterialPoint;
import com.tfc.dynamicweaponry.tool.ToolComponent;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ToolRenderer extends ItemStackTileEntityRenderer {
	private static final ModelRenderer cube = new ModelRenderer(16, 16, 0, 0);
	
	static {
		//Generated with block bench
		cube.setRotationPoint(0.0F, 0.0F, 0.0F);
		cube.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
	}
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
//		super.func_239207_a_(stack, p_239207_2_, matrixStack, buffer, combinedLight, combinedOverlay);
//		RenderHelper.drawBox(matrixStack, new AxisAlignedBB(0, 0, 0.45, 1, 1, 0.55f), 1, 1, 1, 1);
		matrixStack.push();
		matrixStack.translate(0, 0, 0.45);
		matrixStack.scale(1f / 4, 1f / 4, 1f / 4);
		if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
			matrixStack.scale(0.86f, 0.86f, 1);
			matrixStack.translate(0.35f, 0.35f, 0);
			combinedLight = LightTexture.packLight(15, 0);
		}
		CompoundNBT nbt = stack.getOrCreateTag().getCompound("parts");
		boolean hasRenderedAnything = false;
		for (String s : nbt.keySet()) {
			CompoundNBT part = nbt.getCompound(s);
			try {
				ToolComponent component = new ToolComponent(part);
				for (MaterialPoint point : component.points) {
					Material material = Loader.INSTANCE.getMaterial(point.material);
					if (material != null) {
						Color color = new Color(material.color);
						matrixStack.push();
						matrixStack.translate(point.x / 4f, point.y / 4f, 0);
						cube.render(
								matrixStack,
								buffer.getBuffer(RenderType.getEntitySolid(new ResourceLocation("dynamic_weaponry:textures/item/white_square.png"))),
								combinedLight,
								combinedOverlay,
								color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1
						);
						matrixStack.pop();
						hasRenderedAnything = true;
					}
				}
			} catch (Throwable ignored) {
			}
		}
		if (!hasRenderedAnything) {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					matrixStack.push();
					matrixStack.translate(x / 4f, y / 4f, 0);
					cube.render(
							matrixStack,
							buffer.getBuffer(RenderType.getEntitySolid(new ResourceLocation("dynamic_weaponry:textures/item/white_square.png"))),
							combinedLight,
							combinedOverlay,
							(((((x / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f),
							(((((y / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f),
							1, 1
					);
					matrixStack.pop();
				}
			}
		}
		matrixStack.pop();
	}
}
