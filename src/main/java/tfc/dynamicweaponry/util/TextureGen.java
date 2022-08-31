package tfc.dynamicweaponry.util;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tfc.dynamicweaponry.Material;
import tfc.dynamicweaponry.tool.ToolLayer;

import java.util.ArrayList;
import java.util.Arrays;

public class TextureGen {
	public static ToolImage generate(ToolLayer[] layers, long texId, boolean registerTexture) {
		ToolImage img;
		if (registerTexture) {
			NativeImage image = new NativeImage(16, 16, false);
			DynamicTexture texture = new DynamicTexture(image);
			ResourceLocation location = new ResourceLocation("dynamic_weaponry:" + texId);
			Minecraft.getInstance().textureManager.register(location, texture);
			img = new ToolImage(image, texture, location, texId);
		} else {
			img = new ToolImage(null, null, null, 0);
		}
		
		Vector3f light = new Vector3f(2, 1, 0);
		light.normalize();
		ArrayList<ToolLayer> completedLayers = new ArrayList<>();
		ArrayList<ToolLayer> incompleteLayers = new ArrayList<>() {{
			this.addAll(Arrays.asList(layers));
		}};
		ArrayList<ToolLayer> otherLayersList = new ArrayList<>() {{
			this.addAll(Arrays.asList(layers));
		}};
		
		for (ToolLayer layer : layers) {
			Material[] innerShape = new Material[16 * 16];
			
			// I don't even know how to describe this part
			// but this is important for shading
			ToolLayer[] otherLayers = completedLayers.toArray(new ToolLayer[0]);
			completedLayers.add(layer);
			ToolLayer[] shadeLayers = completedLayers.toArray(new ToolLayer[0]);
			
			ToolLayer[] otherLayersDirt = incompleteLayers.toArray(new ToolLayer[0]);
			incompleteLayers.remove(layer);
			
			ToolLayer[] singletonLayer = new ToolLayer[]{layer};
			
			otherLayersList.remove(layer);
			ToolLayer[] outlineLayers = otherLayersList.toArray(new ToolLayer[0]);
			otherLayersList.add(layer);
			
			// edge detection based shading
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					Material pixel0 = layer.get(x, y);
					if (pixel0 != null) {
						// compute the outline scalar
						// used to determine if the pixel is an inner pixel, and also the shading of the outline
						float outline = outlineShade(outlineLayers, singletonLayer, shadeLayers, pixel0, x, y, light);
						if (outline == 1) // if it is not an outline pixel, then it can get highlights
							innerShape[index(x, y)] = pixel0;
						
						// compute the bump scalar
						float scl = shade(0.5f, otherLayers, x, y, light);
						scl *= scl;
						// adds a bit of extra shading around the edge of the layer
						scl = Mth.lerp(0.3f, scl, shade(0.75f, otherLayersDirt, x, y, light));
						// take whichever one makes it darker
						scl = Math.min(scl, outline);
						ExpandedColor expandedColor = new ExpandedColor(pixel0.color);
						// hue shift+darken
						expandedColor = expandedColor.darker(scl, 5);
						img.setRGB(x, 15 - y, expandedColor.getRGB());
					}
				}
			}
			
			// highlights
			ToolLayer[] tempLayer = new ToolLayer[]{new ToolLayer(innerShape)};
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					int index = index(x, y);
					
					Material pixel = innerShape[index];
					if (pixel != null) {
						// ray step is probably the least expensive calculation
						// it simply checks a few surrounding pixels, then walks up left three times
						float step = rayStep(layer, innerShape, x, y);
						if (step != 0) {
							// in vanilla, outlines only end up on the edge of the tool
							if (edgesMatched(innerShape, x, y) > 0) {
								Vector3f vec = edgeDetect(tempLayer, x, y);
								if (!vec.equals(new Vector3f(0, 0, 0))) {
									float edgeDetect = shade(0, tempLayer, x, y, light);
									if (edgeDetect != 0) {
										ExpandedColor color = new ExpandedColor(innerShape[index].highlightColor);
										float shine = (1 - pixel.shininess);
										// modifications to the values
										// makes the highlights look better, but I have no idea how to describe what this is doing
										float v = (shine * (1 - step));
										if (v > 0.5f) v = 0.5f;
										v = 0.5f - v;
										v /= 3;
										v = 1 - v;
										color = color.darker(v, 10);
										img.setRGB(x, 15 - y, color.getRGB());
									}
								}
							}
						}
					}
				}
			}
		}

		return img;
	}
	
	private static boolean containsPixel(ToolLayer[] layers, int x, int y) {
		for (ToolLayer layer : layers) {
			if (layer.get(x, y) != null) {
				return true;
			}
		}
		return false;
	}
	
	private static float outlineShade(ToolLayer[] otherLayers, ToolLayer[] singletonLayer, ToolLayer[] shadeLayers, Material material, int x, int y, Vector3f light) {
		if (containsPixel(otherLayers, x, y)) {
			if (!edgeDetect(otherLayers, x, y).equals(new Vector3f(0, 0, 0))) {
				return shade(material.shininess, singletonLayer, x, y, light);
			} else {
				return 1;
			}
		}
		if (edgesMatchedExcluding(otherLayers, singletonLayer[0].array(), x, y) > 1) {
			return shade(material.shininess, singletonLayer, x, y, light);
		}
		return shade(material.shininess, shadeLayers, x, y, light);
//		if (containsPixel(otherLayers, x, y)) {
//			if (!edgeDetect(otherLayers, x, y).equals(new Vector3f(0, 0, 0))) {
////				if (edgeDetect(currentLayer) == yesEdges) {
////					outline = true;
////				}
//				return shade(material.shininess, singletonLayer, x, y, light);
//			} else {
//				return 1;
//			}
//		}
	}
	
	private static int edgesMatchedExcluding(ToolLayer[] layers, Material[] exclude, int x, int y) {
		int count = 0;
		
		for (int i = -1; i <= 1; i += 2) {
			int offsetX = x + i;
			if (offsetX >= 0 && offsetX < 16) {
				if (exclude[index(offsetX, y)] == null) {
					for (ToolLayer layer : layers) {
						if (layer.get(offsetX, y) != null) {
							count++;
							break;
						}
					}
				}
			}
			
			int offsetY = y + i;
			if (offsetX >= 0 && offsetX < 16) {
				if (exclude[index(x, offsetY)] == null) {
					for (ToolLayer layer : layers) {
						if (layer.get(x, offsetY) != null) {
							count++;
							break;
						}
					}
				}
			}
		}
		
		return count;
	}
	
	private static int edgesMatched(Material[] materials, int x, int y) {
		int count = 0;
		
		for (int i = -1; i <= 1; i += 2) {
			int offsetX = x + i;
			if (offsetX >= 0 && offsetX < 16) {
				if (materials[index(offsetX, y)] != null) {
					count++;
				}
			}
			
			int offsetY = y + i;
			if (offsetX >= 0 && offsetX < 16) {
				if (materials[index(x, offsetY)] != null) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	private static float rayStep(ToolLayer layer, Material[] materials, int x, int y) {
		if (
				materials[index(x + 1, y - 1)] != null &&
						layer.get(x - 1, y + 1) != null
		) {
			return 0;
		}
		for (int i = 1; i < 3; i++) {
			if (
					(x - i) < 0 ||
							(y + i) > 15 ||
							materials[index(x - i, y + i)] == null
			) {
				return i / 3f;
			}
		}
		return 0;
	}
	
	private static int index(int x, int y) {
		return x * 16 + y;
	}
	
	private static float shade(float scl, ToolLayer[] layers, int x, int y, Vector3f light) {
		Vector3f vec = edgeDetect(layers, x, y);
		if (vec.equals(new Vector3f(0, 0, 0))) return 1;
		float v = vec.dot(light);
		if (v < 0) v = v + 1;
		v = 1 - v;
		v = (v + 0.5f) / 2;
		v = Mth.lerp(scl, v, 1);
		return v;
	}
	
	private static Vector3f edgeDetect(ToolLayer[] layers, int x, int y) {
		boolean any = false;
		for (ToolLayer layer : layers) {
			if (layer.get(x, y) != null) {
				any = true;
				break;
			}
		}
		if (!any) {
			return new Vector3f(0, 0, 0);
		}
		
		boolean above = true;
		boolean below = true;
		boolean left = true;
		boolean right = true;
		for (ToolLayer layer : layers) {
			for (int i = -1; i <= 1; i += 2) {
				int offsetX = x + i;
				if (offsetX >= 0 && offsetX < 16) {
					boolean h = layer.get(offsetX, y) == null;
					if (!h) {
						if (i == -1) left = false;
						else right = false;
					}
				}
				
				int offsetY = y + i;
				if (offsetY >= 0 && offsetY < 16) {
					boolean h = layer.get(x, offsetY) == null;
					if (!h) {
						if (i == -1) below = false;
						else above = false;
					}
				}
			}
		}
		if (!left && !right && !above && !below) {
			return new Vector3f(0, 0, 0);
		}
		
		// TODO: this handles corners poorly
		Vector3f horizVec = new Vector3f(
				((right || below ? 2 : 0) - (left ? 1 : 0)),
				((below ? 1 : 0) - (above ? 1 : 0)),
				0
		);
		horizVec.normalize();
		return horizVec;
	}
}
