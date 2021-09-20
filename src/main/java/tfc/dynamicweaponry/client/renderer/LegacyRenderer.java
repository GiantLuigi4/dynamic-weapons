package tfc.dynamicweaponry.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.assortedutils.utils.Color;
import tfc.dynamicweaponry.item.tool.MaterialPoint;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;

public class LegacyRenderer {
	
	public static void renderArrow(ItemStack stack, MatrixStack matrixStack, Object2ObjectLinkedOpenHashMap<ResourceLocation, ArrayList<Point>> pointsOfInterest, Tool tool, ItemCameraTransforms.TransformType p_239207_2_, IVertexBuilder builder, int combinedOverlay, int combinedLight) {
		float pct = stack.getOrCreateTag().getFloat("pull_time");
		// so apparently, I seem to be forgetting to push and pop the matrix stack in my render cube code, and thus I have to account for this while rendering each cube of my arrow
		// I'm too lazy to fix it, so uh... yeah...
		// enjoy this mess :P
		if (tool.isBow() && pct != 0) {
//			IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation("dynamic_weaponry:arrows/arrow"));
//			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation("dynamic_weaponry:arrows/arrow"));
//			Minecraft.getInstance().getItemRenderer().renderItem();
//			IVertexBuilder builder1 = buffer.getBuffer(RenderType.getSolid());
//			for (Direction value : Direction.values()) {
//				List<BakedQuad> quads = model.getQuads(null,value,new Random(0));
//				for (BakedQuad quad : quads) {
//					builder1.addQuad(
//							matrixStack.getLast(),
//							quad,1,1,1,
//							combinedLight,combinedOverlay
//					);
//				}
//			}
//			List<BakedQuad> quads = model.getQuads(null,null,new Random(0));
//			for (BakedQuad quad : quads) {
//				builder1.addQuad(
//						matrixStack.getLast(),
//						quad,1,1,1,
//						combinedLight,combinedOverlay
//				);
//			}
			MaterialPoint point = new MaterialPoint(8, 8, new ResourceLocation("minecraft:string"));
			ArrayList<Point> points = pointsOfInterest.get(new ResourceLocation("dynamic_weaponry:bow_string"));
			point = ToolRenderer.getInstance().lerpToInterest(pct, point, points);
			
			matrixStack.translate(point.x / 4f, point.y / 4f, 0);
			matrixStack.translate(-0.75f, 0.5f, 0.075f);
			if (pct >= 0.925f) {
				matrixStack.translate(0.25f, 0.25f, 0);
			}
			matrixStack.scale(1, 1, 0.9f);
			
			boolean useNormals = p_239207_2_ != ItemCameraTransforms.TransformType.GUI;
			int arrowLen = 4;
			
			Item item = (ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getOrCreateTag().getString("selected_ammo"))));
			ArrowItem arrowItem = null;
			if (item instanceof ArrowItem) arrowItem = (ArrowItem) item;
			
			Color shaftA = new Color(40, 30, 11);
			Color shaftB = new Color(137, 103, 39);
			
			Color featherA = new Color(255, 255, 255);
			Color featherB = new Color(224, 224, 224);
			Color featherC = new Color(216, 216, 216);
			Color featherD = new Color(198, 198, 198);
			Color featherE = new Color(150, 150, 150);
			
			if (arrowItem instanceof SpectralArrowItem) {
				shaftA = new Color(123, 63, 5);
				shaftB = new Color(207, 140, 39);
				featherC = new Color(255, 255, 119);
				featherB = featherC;
				featherD = new Color(218, 185, 48);
				featherE = featherD;
			} else if (arrowItem instanceof TippedArrowItem) {
				Color potionColor = new Color(stack.getOrCreateTag().getInt("ammo_color"));
				
				featherA = new Color(potionColor.getHue(), potionColor.getSaturation(), featherA.getValue() * (potionColor.getValue()), false);
				featherB = new Color(potionColor.getHue(), potionColor.getSaturation(), featherB.getValue() * (potionColor.getValue()), false);
				featherC = new Color(potionColor.getHue(), potionColor.getSaturation(), featherC.getValue() * (potionColor.getValue()), false);
				featherD = new Color(potionColor.getHue(), potionColor.getSaturation(), featherD.getValue() * (potionColor.getValue()), false);
				featherE = new Color(potionColor.getHue(), potionColor.getSaturation(), featherE.getValue() * (potionColor.getValue()), false);
			}
			
			//Shaft
			for (int i = -1; i < arrowLen; i++) {
				matrixStack.push();
				matrixStack.translate(-0.25f * i, 0.25f * i, 0);
				ToolRenderer.getInstance().renderCube(shaftA.getRed() / 255f, shaftA.getGreen() / 255f, shaftA.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
				matrixStack.translate(0, 0, 0.25f);
				ToolRenderer.getInstance().renderCube(shaftB.getRed() / 255f, shaftB.getGreen() / 255f, shaftB.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
				matrixStack.pop();
			}
			//Head
			matrixStack.push();
			matrixStack.translate(-0.25f * (arrowLen + 1), 0.25f * (arrowLen + 1), 0);
			ToolRenderer.getInstance().renderCube(featherE.getRed() / 255f, featherE.getGreen() / 255f, featherE.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.translate(0, 0.25f, 0.25f);
			ToolRenderer.getInstance().renderCube(featherE.getRed() / 255f, featherE.getGreen() / 255f, featherE.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.push();
			matrixStack.translate(-0.25f, 0, 0);
			ToolRenderer.getInstance().renderCube(featherC.getRed() / 255f, featherC.getGreen() / 255f, featherC.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(-0.25f, 0, 0.25f);
			ToolRenderer.getInstance().renderCube(featherC.getRed() / 255f, featherC.getGreen() / 255f, featherC.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.translate(-0.5f, 0, 0);
			ToolRenderer.getInstance().renderCube(featherC.getRed() / 255f, featherC.getGreen() / 255f, featherC.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(-0.25f * (arrowLen + 1), 0.25f * (arrowLen + 2), 0);
			ToolRenderer.getInstance().renderCube(featherA.getRed() / 255f, featherA.getGreen() / 255f, featherA.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			//Tail
			matrixStack.translate(0, 0.25f, 0);
			matrixStack.push();
			matrixStack.translate(-0.25f * -2, 0.25f * -2, 0);
			ToolRenderer.getInstance().renderCube(featherD.getRed() / 255f, featherD.getGreen() / 255f, featherD.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.translate(0, 0.25f, -0.25f);
			ToolRenderer.getInstance().renderCube(featherD.getRed() / 255f, featherD.getGreen() / 255f, featherD.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.push();
			matrixStack.translate(0, 0, -0.25f);
			ToolRenderer.getInstance().renderCube(featherB.getRed() / 255f, featherB.getGreen() / 255f, featherB.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(0, 0, 0.25f);
			ToolRenderer.getInstance().renderCube(featherB.getRed() / 255f, featherB.getGreen() / 255f, featherB.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(-0.25f, 0, 0);
			ToolRenderer.getInstance().renderCube(featherB.getRed() / 255f, featherB.getGreen() / 255f, featherB.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.push();
			matrixStack.translate(0.25f, 0, 0);
			ToolRenderer.getInstance().renderCube(featherB.getRed() / 255f, featherB.getGreen() / 255f, featherB.getBlue() / 255f, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
			matrixStack.pop();
			matrixStack.pop();
		}
		matrixStack.pop();
	}
}
