package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.dynamicweaponry.block.ToolForgeTileEntity;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;

import java.util.List;
import java.util.Random;

public class ToolForgeItemRenderer extends ItemStackTileEntityRenderer {
	public ToolForgeItemRenderer() {
	}
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		super.func_239207_a_(stack, p_239207_2_, matrixStack, buffer, combinedLight, combinedOverlay);
		
		matrixStack.push();
		if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
		
		}
		
		{
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(Registry.TOOL_FORGE.get().getDefaultState());
			IVertexBuilder builder = buffer.getBuffer(RenderType.getCutout());
			for (Direction dir : Direction.values()) {
				List<BakedQuad> quads = model.getQuads(
						Registry.TOOL_FORGE.get().getDefaultState(),
						dir, new Random(0)
				);
				for (BakedQuad quad : quads) {
					builder.addQuad(
							matrixStack.getLast(),
							quad, 1, 1, 1,
							combinedLight, combinedOverlay
					);
				}
			}
			List<BakedQuad> quads = model.getQuads(
					Registry.TOOL_FORGE.get().getDefaultState(),
					null, new Random(0)
			);
			for (BakedQuad quad : quads) {
				builder.addQuad(
						matrixStack.getLast(),
						quad, 1, 1, 1,
						combinedLight, combinedOverlay
				);
			}
		}
		
		if (stack.hasTag() && stack.getTag().contains("BlockEntityTag")) {
			ToolForgeTileEntity tileEntity = new ToolForgeTileEntity();
			tileEntity.read(
					Registry.TOOL_FORGE.get().getDefaultState(),
					stack.getTag().getCompound("BlockEntityTag")
			);
			ToolRenderer renderer = ToolRenderer.getInstance();
			ItemStack stack1 = new ItemStack(Registry.DYNAMIC_TOOL.get());
			CompoundNBT nbt1 = stack1.getOrCreateTag();
			nbt1.put("tool_info", tileEntity.container.tool.serialize());
			matrixStack.push();
			matrixStack.translate(0, 1.465625, 0);
			matrixStack.rotate(new Quaternion(90, 0, 0, true));
			float scale = 6;
			matrixStack.translate(5f / 16, 5f / 16, 0);
			matrixStack.scale(scale / 16, scale / 16, 1);
			renderer.func_239207_a_(
					stack1,
					ItemCameraTransforms.TransformType.FIXED,
					matrixStack, buffer, combinedLight, combinedOverlay
			);
			matrixStack.pop();
		}
		matrixStack.pop();
	}
}
