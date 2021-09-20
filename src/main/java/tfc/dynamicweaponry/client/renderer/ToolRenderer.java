package tfc.dynamicweaponry.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import tfc.assortedutils.utils.Color;
import tfc.assortedutils.utils.CustomBuffer;
import tfc.dynamicweaponry.Config;
import tfc.dynamicweaponry.client.renderer.texture.TextureMap;
import tfc.dynamicweaponry.client.renderer.texture.ToolTexture;
import tfc.dynamicweaponry.data.DataLoader;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.MaterialPoint;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.item.tool.ToolComponent;
import tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Deque;

public class ToolRenderer extends ItemStackTileEntityRenderer {
	public static final ToolRenderer render = new ToolRenderer();
	
	private static final Object2ObjectLinkedOpenHashMap<CompoundNBT, CustomBuffer> bufferCache = new Object2ObjectLinkedOpenHashMap<>();
	
	private static final Object2ObjectLinkedOpenHashMap<ResourceLocation, ArrayList<Point>> pointsOfInterest = new Object2ObjectLinkedOpenHashMap<>();
	
	static {
		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(15, 11));
		points.add(new Point(5, 1));
		points.add(new Point(10, 4));
		pointsOfInterest.put(new ResourceLocation("dynamic_weaponry:bow_string"), points);
	}
	
	public void resetCaches() {
		bufferCache.clear();
	}
	
	private static final Quaternion quat90X = new Quaternion(90, 0, 0, true);
	private static final Quaternion quat180X = new Quaternion(180, 0, 0, true);
	private static final Quaternion quat90Y = new Quaternion(0, 90, 0, true);
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if (false) {
			String name = stack.getOrCreateTag().toString()
					.replace("{", "_v")
					.replace("}", "v_")
					.replace("[", "_c")
					.replace("]", "c_")
					.replace("(", "_e")
					.replace(")", "e_")
					.toLowerCase();
			if (!TextureMap.has(name)) {
				ToolTexture texture = TextureMap.get(name);
				Tool tool = new Tool(stack);
				for (ToolComponent component : tool.components) {
					for (MaterialPoint point : component.points) {
						if (point.material == null) continue;
						Color c = Shading.shade(point, tool, component);
						texture.setPixel(point, c);
					}
				}
			}
			
			matrixStack.push();
			{
				matrixStack.translate(0, 0, 0.45);
				matrixStack.scale(1f / 4, 1f / 4, 1f / 4);
				if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
					matrixStack.scale(0.86f, 0.86f, 1);
					matrixStack.translate(0.35f, 0.35f, 0);
					combinedLight = LightTexture.packLight(15, 0);
				}
				IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(TextureMap.load(name)));
				matrixStack.scale(16, 16, 16);
				matrixStack.rotate(quat180X);
				matrixStack.translate(0, -4f / 16, -0.062f / 16);
				
				matrixStack.push();
				{
					matrixStack.translate(0, 0, -0.0005f / 16);
					renderSquare(1, 1, 1, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, true);
				}
				matrixStack.pop();
				
				matrixStack.rotate(quat180X);
				matrixStack.translate(4f / 16, -4f / 16, 0.250501f / 16);
				matrixStack.translate(-4f / 16, 0, 0);
				matrixStack.rotate(new Quaternion(0, 0, 90, true));
				renderSquare(1, 1, 1, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, true, true);
			}
			matrixStack.pop();
			
			matrixStack.push();
			{
			}
			matrixStack.pop();
			
			return;
		}
		
		//TODO: move everything below here to LegacyRenderer
		if (Config.CLIENT.cacheBuffers.get() && !(buffer instanceof CustomBuffer)) {
			CustomBuffer builder;
			
			matrixStack.push();
			
			matrixStack.translate(0, 0, 0.45);
			matrixStack.scale(1f / 4, 1f / 4, 1f / 4);
			if (p_239207_2_.equals(ItemCameraTransforms.TransformType.GUI)) {
				matrixStack.scale(0.86f, 0.86f, 1);
				matrixStack.translate(0.35f, 0.35f, 0);
				combinedLight = LightTexture.packLight(15, 0);
			}
			
			CompoundNBT cacheEntry = stack.getOrCreateTag().copy();
			cacheEntry.remove("Durability");
			
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
			
			try {
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
			} catch (ConcurrentModificationException | NullPointerException err) {
				err.printStackTrace();
			}
			matrixStack.pop();
			
			if (!bufferCache.containsKey(stack.getOrCreateTag()))
				bufferCache.put(stack.getOrCreateTag(), builder);
			
			return;
		}
		
		{
			Deque<MatrixStack.Entry> stackDeque = matrixStack.stack;
			matrixStack = new MatrixStack();
			matrixStack.stack.addAll(stackDeque);
		}
		
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
		
		tool = new Tool(stack);
		
		tool.sort();
		boolean hasRenderedAnything = false;
		
		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(new ResourceLocation("dynamic_weaponry:textures/item/white_square.png")));
		
		for (ToolComponent component : tool.components) {
			try {
				for (MaterialPoint point : component.points) {
					Material material = DataLoader.INSTANCE.getMaterial(point.material);
					
					if (material != null) {
						matrixStack.push();
						Color color = Shading.shade(point, tool, component);
						
						float r = color.getRed() / 255f;
						float g = color.getGreen() / 255f;
						float b = color.getBlue() / 255f;
						
						boolean isLerpedPoint = false;
						MaterialPoint src = point;
						
						if (pointsOfInterest.containsKey(component.type.name)) {
							float pct = stack.getOrCreateTag().getFloat("pull_time");
							ArrayList<Point> points = pointsOfInterest.get(component.type.name);
							point = lerpToInterest(pct, point, points);
							isLerpedPoint = true;
						}
						
						matrixStack.translate(0, 0, 0.125f / 2);
						
						if (isLerpedPoint) {
							float pct = stack.getOrCreateTag().getFloat("pull_time");
							ArrayList<Point> points = pointsOfInterest.get(component.type.name);
							
							for (int x = -1; x <= 1; x++) {
								for (int y = -1; y <= 1; y++) {
									if (x != 0 && y != 0) {
										MaterialPoint otherPoint = tool.getPoint(src.x + x, src.y + y);
										if (otherPoint != null) {
											otherPoint = lerpToInterest(pct, otherPoint, points);
											MaterialPoint last = null;
											if (otherPoint != null) {
												src = otherPoint;
												boolean isVertical =
														(Math.abs(otherPoint.y - point.y) <
																Math.abs(otherPoint.x - point.x));
												
												for (int i = 0; i < 16; i++) {
													MaterialPoint point1 = point.lerp(i / 16f, otherPoint);
													
													if (
															!point1.equals(last) &&
																	(
																			last == null ||
																					(isVertical ? point1.x != last.x :
																							point1.y != last.y)
																	)
													) {
														matrixStack.push();
														
														matrixStack.translate(point1.x / 4f, point1.y / 4f, 0);
														
														renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI);
//														renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI, point.x, point.y, tool);
														
														matrixStack.pop();
														
														last = point1;
													} else if (last == null) {
														last = point1;
													}
												}
												break;
											}
										}
									}
								}
							}
						} else {
							if (component.name.contains("lower") && point.y < 8) {
								float pct = stack.getOrCreateTag().getFloat("pull_time");
								if (point.y < 5) {
									point = new MaterialPoint((int) MathHelper.lerp(pct / 3f, point.x, 9), point.y, point.material);
								} else {
									point = new MaterialPoint((int) MathHelper.lerp(pct / 3f, point.x, 7), point.y, point.material);
								}
								matrixStack.translate(point.x / 4f, point.y / 4f, 0);
								renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI);
							} else if (component.name.contains("upper") && point.x > 7) {
								float pct = stack.getOrCreateTag().getFloat("pull_time");
								if (point.x > 10) {
									point = new MaterialPoint(point.x, (int) MathHelper.lerp(pct / 3f, point.y, 8), point.material);
								} else {
									point = new MaterialPoint(point.x, (int) MathHelper.lerp(pct / 3f, point.y, 10), point.material);
								}
								matrixStack.translate(point.x / 4f, point.y / 4f, 0);
								renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI);
							} else {
								matrixStack.translate(point.x / 4f, point.y / 4f, 0);
								renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI, point.x, point.y, tool);
							}
						}
						
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
					float r = ((((x / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f;
					float g = ((((y / 16f) * 255) + combinedOverlay + combinedLight) % 255) / 255f;
					float b = 1;
					
					matrixStack.translate(0, 0, 0.125f / 2);
					
					renderCube(r, g, b, 0, 0, 0, builder, combinedOverlay, combinedLight, matrixStack, p_239207_2_ != ItemCameraTransforms.TransformType.GUI);
					
					matrixStack.pop();
				}
			}
		}
		
		LegacyRenderer.renderArrow(stack, matrixStack, pointsOfInterest, tool, p_239207_2_, builder, combinedOverlay, combinedLight);
	}
	
	public MaterialPoint lerpToInterest(float pct, MaterialPoint point, ArrayList<Point> interests) {
		double minDist = Float.POSITIVE_INFINITY;
		Point point2 = null;
		
		for (Point point1 : interests) {
			double dist = new Vector3d(point1.x, point1.y, 0).distanceTo(new Vector3d(point.x, point.y, 0));
			if (dist < minDist) {
				minDist = dist;
				point2 = point1;
			}
		}
		
		if (point2 != null) {
			Point point1 = point2.lerp((1 - (pct / 1.25f)), point);
			point = new MaterialPoint(point1.x, point1.y, point.material);
		}
		
		return point;
	}
	
	public void renderCube(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals, int pixelX, int pixelZ, Tool tool) {
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
		renderSquare(r, g, b, x, y, z, builder, combinedOverlay, combinedLight, matrixStack, useNormals, 0, 1, 0, 1);
	}
	
	public void renderSquare(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals, boolean mirror) {
		if (mirror)
			renderSquareMirrored(r, g, b, x, y, z, builder, combinedOverlay, combinedLight, matrixStack, useNormals, 0, 1, 0, 1);
		else renderSquare(r, g, b, x, y, z, builder, combinedOverlay, combinedLight, matrixStack, useNormals);
	}
	
	public void renderSquareMirrored(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals, float minU, float maxU, float minV, float maxV) {
		matrixStack.push();
		matrixStack.rotate(new Quaternion(0, 0, -90, true));
		Vector3f corner1 = translate(matrixStack, x, y, z);
		Vector3f corner2 = translate(matrixStack, x + 0.25f, y, z);
		Vector3f corner3 = translate(matrixStack, x + 0.25f, y + 0.25f, z);
		Vector3f corner4 = translate(matrixStack, x, y + 0.25f, z);
		matrixStack.pop();
		
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

//			normal.mul(0.5f);

//			if (!Config.CLIENT.cacheBuffers.get()) {
			Matrix3f matrix3f = matrixStack.getLast().getNormal();
			normal.transform(matrix3f);
//			}
			
			normal.normalize();
		} else {
			normal = new Vector3f(0, 1, 0);
		}

//		float a = minU;
//		minU = maxU;
//		maxU = a;
//		float a = minV;
//		minV = maxV;
//		maxV = a;
		
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
				1, 0,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		
		builder.addVertex(
				corner3.getX(), corner3.getY(), corner3.getZ(),
				r, g, b, 1,
				1, 1,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		
		builder.addVertex(
				corner4.getX(), corner4.getY(), corner4.getZ(),
				r, g, b, 1,
				0, 1,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
	}
	
	public void renderSquare(float r, float g, float b, float x, float y, float z, IVertexBuilder builder, int combinedOverlay, int combinedLight, MatrixStack matrixStack, boolean useNormals, float minU, float maxU, float minV, float maxV) {
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

//			normal.mul(0.5f);

//			if (!Config.CLIENT.cacheBuffers.get()) {
			Matrix3f matrix3f = matrixStack.getLast().getNormal();
			normal.transform(matrix3f);
//			}
			
			normal.normalize();
		} else {
			normal = new Vector3f(0, 1, 0);
		}
		
		builder.addVertex(
				corner1.getX(), corner1.getY(), corner1.getZ(),
				r, g, b, 1,
				minU, maxV,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		
		builder.addVertex(
				corner2.getX(), corner2.getY(), corner2.getZ(),
				r, g, b, 1,
				maxU, maxV,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		
		builder.addVertex(
				corner3.getX(), corner3.getY(), corner3.getZ(),
				r, g, b, 1,
				maxU, minV,
				combinedOverlay, combinedLight,
				normal.getX(), normal.getY(), normal.getZ()
		);
		
		builder.addVertex(
				corner4.getX(), corner4.getY(), corner4.getZ(),
				r, g, b, 1,
				minU, minV,
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
