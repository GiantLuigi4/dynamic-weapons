package tfc.dynamicweaponry.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import tfc.dynamicweaponry.Temp;
import tfc.dynamicweaponry.util.ToolImage;

public class ToolRenderer extends BlockEntityWithoutLevelRenderer {
	public ToolRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}
	
	private static VertexConsumer vertex(VertexConsumer consumer, PoseStack poseStack, double x, double y, double z) {
		return consumer.vertex(poseStack.last().pose(), (float) x, (float) y, (float) z);
	}
	
	@Override
	public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		ToolImage image = Temp.image;
		VertexConsumer consumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(image.location));
		float step = 1f / 16;
		
		// TODO: apply normal matrix
		
		for (int x = 0; x < 17; x++) {
			float pctX = x / 16f;
			
			if (x < 16) {
				// far end
				vertex(consumer, pPoseStack, pctX, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, -1).endVertex();
				vertex(consumer, pPoseStack, pctX, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX + step, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, -1).endVertex();
				vertex(consumer, pPoseStack, pctX, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX + step, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, -1).endVertex();
				vertex(consumer, pPoseStack, pctX, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, -1).endVertex();
				
				// bottom
				vertex(consumer, pPoseStack, 0, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, -1, 0).endVertex();
				vertex(consumer, pPoseStack, 0, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1 - (pctX + step)).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, -1, 0).endVertex();
				vertex(consumer, pPoseStack, 1, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1 - (pctX + step)).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, -1, 0).endVertex();
				vertex(consumer, pPoseStack, 1, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, -1, 0).endVertex();
			}
			if (x > 0) {
				// near end
				vertex(consumer, pPoseStack, pctX, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
				vertex(consumer, pPoseStack, pctX, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX - step, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
				vertex(consumer, pPoseStack, pctX, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(pctX - step, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
				vertex(consumer, pPoseStack, pctX, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(pctX, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
				
				// top
				vertex(consumer, pPoseStack, 0, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 1, 0).endVertex();
				vertex(consumer, pPoseStack, 0, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1 - (pctX - step)).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 1, 0).endVertex();
				vertex(consumer, pPoseStack, 1, pctX, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1 - (pctX - step)).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 1, 0).endVertex();
				vertex(consumer, pPoseStack, 1, pctX, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1 - pctX).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 1, 0).endVertex();
			}
		}
		
		vertex(consumer, pPoseStack, 0, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 1, 0, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 1, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(1, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 0, 1, 0.5 + step / 2).color(255, 255, 255, 255).uv(0, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(1, 0, 0).endVertex();
		
		vertex(consumer, pPoseStack, 0, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(-1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 1, 1, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 0).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(-1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 1, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(1, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(-1, 0, 0).endVertex();
		vertex(consumer, pPoseStack, 0, 0, 0.5 - step / 2).color(255, 255, 255, 255).uv(0, 1).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(-1, 0, 0).endVertex();
	}
}
