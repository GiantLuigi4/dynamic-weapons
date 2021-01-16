package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.dynamicweaponry.block.ToolForgeTileEntity;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Quaternion;

public class ToolForgeRenderer extends TileEntityRenderer<ToolForgeTileEntity> {
	public ToolForgeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	
	@Override
	public void render(ToolForgeTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ToolRenderer renderer = ToolRenderer.getInstance();
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt1.put("tool_info", tileEntityIn.container.tool.serialize());
		matrixStackIn.push();
		matrixStackIn.translate(0, 1.465625, 0);
		matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
		float scale = 6;
		matrixStackIn.translate(5f / 16, 5f / 16, 0);
		matrixStackIn.scale(scale / 16, scale / 16, 1);
		renderer.func_239207_a_(
				stack,
				ItemCameraTransforms.TransformType.FIXED,
				matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn
		);
		matrixStackIn.pop();
	}
}

