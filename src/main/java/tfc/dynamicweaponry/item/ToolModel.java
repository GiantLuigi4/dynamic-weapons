package tfc.dynamicweaponry.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.util.ToolImage;

public class ToolModel {
	private static VertexConsumer vertex(VertexConsumer consumer, PoseStack poseStack, double x, double y, double z) {
		Matrix4f pMatrix = poseStack.last().pose();
		return consumer.vertex(
				pMatrix.m00 * x + pMatrix.m01 * y + pMatrix.m02 * z + pMatrix.m03 * 1,
				pMatrix.m10 * x + pMatrix.m11 * y + pMatrix.m12 * z + pMatrix.m13 * 1,
				pMatrix.m20 * x + pMatrix.m21 * y + pMatrix.m22 * z + pMatrix.m23 * 1
		);
//		return consumer.vertex(poseStack.last().pose(), (float) x, (float) y, (float) z);
	}
	
	private static VertexConsumer normal(VertexConsumer consumer, ItemTransforms.TransformType pTransformType, PoseStack stack, float x, float y, float z) {
		// TODO: what
		if (pTransformType.equals(ItemTransforms.TransformType.GUI))
			return consumer.normal(stack.last().normal(), 0, x, 0);
		if (pTransformType.firstPerson())
			return consumer.normal(x, y, z);
		// TODO: figure this out
		Matrix3f matr = stack.last().normal().copy();
		matr.mul(new Quaternion(0, 90, 0, true));
		return consumer.normal(stack.last().normal(), x, y, z);
	}
	
	public static void render(Tool tool, MultiBufferSource pBuffer, PoseStack pPoseStack, int pPackedOverlay, int pPackedLight, ItemTransforms.TransformType pTransformType) {
		ToolImage image = tool.getImage();
		VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(image.location));
		float step = 1f / 16;
		
		for (int x = 0; x < 17; x++) {
			float pctX = x / 16f;
			
			if (x < 16) {
				// far end
				normal(vertex(consumer, pPoseStack, pctX, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, -1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX + step, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, -1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX + step, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, -1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, -1).endVertex();
				
				// bottom
				normal(vertex(consumer, pPoseStack, 0, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, -1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 0, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1 - (pctX + step)).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, -1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 1, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1 - (pctX + step)).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, -1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 1, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, -1, 0).endVertex();
			}
			if (x > 0) {
				// near end
				normal(vertex(consumer, pPoseStack, pctX, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, 1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX - step, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, 1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX - step, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, 1).endVertex();
				normal(vertex(consumer, pPoseStack, pctX, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 0, 1).endVertex();
				
				// top
				normal(vertex(consumer, pPoseStack, 0, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 0, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1 - (pctX - step)).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 1, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1 - (pctX - step)).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 1, 0).endVertex();
				normal(vertex(consumer, pPoseStack, 1, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 0, 1, 0).endVertex();
			}
		}
		
		normal(vertex(consumer, pPoseStack, 0, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 1, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 1, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 0, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, 1, 0, 0).endVertex();
		
		normal(vertex(consumer, pPoseStack, 0, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, -1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 1, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, -1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 1, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, -1, 0, 0).endVertex();
		normal(vertex(consumer, pPoseStack, 0, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight), pTransformType, pPoseStack, -1, 0, 0).endVertex();
	}
}
