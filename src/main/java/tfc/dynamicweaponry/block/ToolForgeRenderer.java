package tfc.dynamicweaponry.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import tfc.dynamicweaponry.item.ToolModel;
import tfc.dynamicweaponry.tool.Tool;

public class ToolForgeRenderer implements BlockEntityRenderer<ToolForgeBlockEntity> {
	@Override
	public void render(ToolForgeBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//		Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
//		boolean lowDetail = pBlockEntity.getBlockPos().distToCenterSqr(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z) > 20;
		Tool tool = pBlockEntity.getTool(Minecraft.getInstance().level);
		if (tool == null) return;
		pPoseStack.mulPose(new Quaternion(-90, 0, 0, true));
		pPoseStack.translate(0, -1, 0.5f - 1f / 32);
		pPoseStack.translate(5f / 16, 5f / 16, 0);
		pPoseStack.scale(1f / 16, 1f / 16, 1);
		pPoseStack.scale(6f, 6f, 1);
		pPoseStack.last().normal().mul(new Quaternion(0, 0, -90, true));
		ToolModel.render(tool, pBufferSource, pPoseStack, pPackedOverlay, pPackedLight, ItemTransforms.TransformType.FIXED);
	}
}
