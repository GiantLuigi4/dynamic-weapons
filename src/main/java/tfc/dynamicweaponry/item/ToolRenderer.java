package tfc.dynamicweaponry.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import tfc.dynamicweaponry.access.IMayHoldATool;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.util.ToolImage;

public class ToolRenderer extends BlockEntityWithoutLevelRenderer {
	public ToolRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}
	
	@Override
	public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		pStack.getItem().inventoryTick(pStack, Minecraft.getInstance().level, Minecraft.getInstance().cameraEntity, 0, false);
		Tool tool = ((IMayHoldATool) (Object) pStack).myTool();
		if (tool == null) return;
		ToolModel.render(tool, pBuffer, pPoseStack, pPackedOverlay, pPackedLight, pTransformType);
	}
}
