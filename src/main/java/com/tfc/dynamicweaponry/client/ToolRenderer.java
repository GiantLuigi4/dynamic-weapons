package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.assortedutils.utils.Color;
import com.tfc.assortedutils.utils.CustomBuffer;
import com.tfc.dynamicweaponry.Config;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.item.tool.MaterialPoint;
import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.item.tool.ToolComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
//import org.codehaus.plexus.util.FastMap;
//import net.minecraft.client.renderer.texture.OverlayTexture;

public class ToolRenderer extends ItemStackTileEntityRenderer {
	public static final ToolRenderer render = new ToolRenderer();
	
	private static final Object2ObjectLinkedOpenHashMap<CompoundNBT, CustomBuffer> bufferCache = new Object2ObjectLinkedOpenHashMap<>();
	private static final Object2ObjectLinkedOpenHashMap<CompoundNBT, Tool> toolCache = new Object2ObjectLinkedOpenHashMap<>();
	
	private static final Quaternion quat90X = new Quaternion(90, 0, 0, true);
	private static final Quaternion quat180X = new Quaternion(180, 0, 0, true);
	private static final Quaternion quat90Y = new Quaternion(0, 90, 0, true);
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (!stack.getOrCreateTag().contains("HideFlags")) stack.getOrCreateTag().putInt("HideFlags", 2);
		
		if (Config.CLIENT.cacheBuffers.get() && !(buffer instanceof CustomBuffer)) {
			CustomBuffer builder;

//			bufferCache.clear();
			
			matrixStack.push();
			matrixStack.translate(0, 0, 0.45);
			matrixStack.scale(1f / 4, 1f / 4, 1f / 4);
			if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
				matrixStack.scale(0.86f, 0.86f, 1);
				matrixStack.translate(0.35f, 0.35f, 0);
				combinedLight = LightTexture.packLight(15, 0);
			}
			if (!bufferCache.containsKey(stack.getOrCreateTag())) {
				builder = new CustomBuffer();
				MatrixStack matrixStack1 = new MatrixStack();
				func_239207_a_(
						stack, ItemCameraTransforms.TransformType.FIXED, matrixStack1,
						builder, combinedLight, combinedOverlay
				);
			} else {
				builder = bufferCache.get(stack.getOrCreateTag());
			}
			
			for (CustomBuffer.CustomVertexBuilder builder2 : builder.builders) {
				IVertexBuilder builder1 = buffer.getBuffer(builder2.type);
				for (CustomBuffer.Vertex vert : builder2.vertices) {
					Vector3f vector3f = translate(matrixStack, (float) vert.x, (float) vert.y, (float) vert.z);
					Vector3f normal;
					if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI))
						normal = new Vector3f(0, 1, 0);
					else
						normal = new Vector3f(vert.nx, vert.ny, vert.nz);
					Matrix3f matrix3f = matrixStack.getLast().getNormal();
					normal.transform(matrix3f);
					normal.normalize();
					builder1.addVertex(
							vector3f.getX(),
							vector3f.getY(),
							vector3f.getZ(),
							vert.r / 255f,
							vert.g / 255f,
							vert.b / 255f,
							vert.a / 255f,
							vert.u, vert.v,
							combinedOverlay, combinedLight,
							normal.getX(), normal.getY(), normal.getZ()
					);
				}
			}
			matrixStack.pop();
			
			bufferCache.put(stack.getOrCreateTag(), builder);
			
			return;
		}

//		super.func_239207_a_(stack, p_239207_2_, matrixStack, buffer, combinedLight, combinedOverlay);
//		RenderHelper.drawBox(matrixStack, new AxisAlignedBB(0, 0, 0.45, 1, 1, 0.55f), 1, 1, 1, 1);
		matrixStack.push();
		if (!Config.CLIENT.cacheBuffers.get()) {
			matrixStack.translate(0, 0, 0.45);
			matrixStack.scale(1f / 4, 1f / 4, 1f / 4);
			if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
				matrixStack.scale(0.86f, 0.86f, 1);
				matrixStack.translate(0.35f, 0.35f, 0);
				combinedLight = LightTexture.packLight(15, 0);
			}
		}
		
		Tool tool;
		if (
				!toolCache.containsKey(stack.getOrCreateTag())
		) {
			tool = new Tool(stack);
			toolCache.put(stack.getOrCreateTag(), tool);
		} else tool = toolCache.get(stack.getOrCreateTag());
		tool.sort();
		boolean hasRenderedAnything = false;
		
		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(new ResourceLocation("dynamic_weaponry:textures/item/white_square.png")));
		
		
		for (ToolComponent component : tool.components) {
			try {
				for (MaterialPoint point : component.points) {
					Material material = Loader.INSTANCE.getMaterial(point.material);
					
					if (material != null) {
						Color color = Shading.shade(point, tool, component);
						matrixStack.push();
						matrixStack.translate(point.x / 4f, point.y / 4f, 0);
						
						float r = color.getRed() / 255f;
						float g = color.getGreen() / 255f;
						float b = color.getBlue() / 255f;
						
						matrixStack.translate(0, 0, 0.125f / 2);

//						if (component.type != null) {
//							matrixStack.scale(1,1,1+component.type.renderLayer/10f);
//						}
						renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI, point.x, point.y, component);
						
						matrixStack.pop();

//						matrixStack.push();
//						matrixStack.translate(point.x / 4f, point.y / 4f, 0);
//						cube.render(
//								matrixStack,
//								buffer.getBuffer(RenderType.getEntitySolid(new ResourceLocation("dynamic_weaponry:textures/item/white_square.png"))),
//								combinedLight,
//								combinedOverlay,
//								color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1
//						);
//						matrixStack.pop();
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
					float r = ((((x / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f;
					float g = ((((y / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f;
					float b = 1;
					
					matrixStack.translate(0, 0, 0.125f / 2);
					
					renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI);
					
					matrixStack.pop();
				}
			}
		}
		
		matrixStack.pop();
	}
	
	public void renderCube(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals, int pixelX, int pixelZ, ToolComponent tool) {
		renderSquare(r, g, b, x, y, z + 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		if (tool.getPoint(pixelX + 1, pixelZ) == null)
			renderSquare(r, g, b, x - 0.25f, y, z + 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		renderSquare(r, g, b, -0.25f, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		if (tool.getPoint(pixelX - 1, pixelZ) == null)
			renderSquare(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90X);
		if (tool.getPoint(pixelX, pixelZ - 1) == null)
			renderSquare(r, g, b, 0, -0.25f, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat180X);
		if (tool.getPoint(pixelX, pixelZ + 1) == null)
			renderSquare(r, g, b, 0, 0, 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
	}
	
	public void renderCube(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals) {
		renderSquare(r, g, b, x, y, z + 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		renderSquare(r, g, b, x - 0.25f, y, z + 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		renderSquare(r, g, b, -0.25f, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90Y);
		renderSquare(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat90X);
		renderSquare(r, g, b, 0, -0.25f, 0, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
		matrixStack.rotate(quat180X);
		renderSquare(r, g, b, 0, 0, 0.25f, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
	}
	
	public void renderSquare(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals) {
		Vector3f corner1 = translate(matrixStack, x, y, z);
		Vector3f corner2 = translate(matrixStack, x + 0.25f, y, z);
		Vector3f corner3 = translate(matrixStack, x + 0.25f, y + 0.25f, z);
		Vector3f corner4 = translate(matrixStack, x, y + 0.25f, z);
		
		Vector3f normal;
		
		if (useNormals) {
			//https://www.khronos.org/opengl/wiki/Calculating_a_Surface_Normal
			Vector3f normalU = new Vector3f(x, y, z);
			Vector3f normalV = normalU.copy();
			normalU.sub(new Vector3f(x + 0.25f, y, z));
			normalV.sub(new Vector3f(x + 0.25f, y + 0.25f, z));
			
			normal = new Vector3f(
					(normalU.getY() * normalV.getZ()) - (normalU.getZ() * normalV.getY()),
					(normalU.getZ() * normalV.getX()) - (normalU.getX() * normalV.getZ()),
					(normalU.getX() * normalV.getY()) - (normalU.getY() * normalV.getX())
			);
			
			normal.normalize();
			normal.mul(0.5f);
			
			if (!Config.CLIENT.cacheBuffers.get()) {
				Matrix3f matrix3f = matrixStack.getLast().getNormal();
				normal.transform(matrix3f);
			}
		} else {
			normal = new Vector3f(0, 1, 0);
		}
		
		builder.addVertex(
				corner1.getX(), corner1.getY(), corner1.getZ(),
				r, g, b, 1,
				0, 0,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		builder.addVertex(
				corner2.getX(), corner2.getY(), corner2.getZ(),
				r, g, b, 1,
				0, 0,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		builder.addVertex(
				corner3.getX(), corner3.getY(), corner3.getZ(),
				r, g, b, 1,
				0, 0,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		builder.addVertex(
				corner4.getX(), corner4.getY(), corner4.getZ(),
				r, g, b, 1,
				0, 0,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
	}
	
	public Vector3f translate(MatrixStack stack, float x, float y, float z) {
		Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
		vector4f.transform(stack.getLast().getMatrix());
		return new Vector3f(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	}
	
	public static ToolRenderer getInstance() {
		return render;
	}
}
