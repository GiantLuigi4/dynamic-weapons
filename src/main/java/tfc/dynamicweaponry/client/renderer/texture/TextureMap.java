package tfc.dynamicweaponry.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class TextureMap {
	private static final NativeImage image = new NativeImage(16, 16, true);
	private static final DynamicTexture tex = new DynamicTexture(image);
	private static final ResourceLocation location;
	private static final HashMap<String, ToolTexture> textures = new HashMap<>();
	
	static {
		location = Minecraft.getInstance().textureManager.getDynamicTextureLocation("dynamic_weaponry/dynamic_tool_16x", tex);
	}
	
	public static ResourceLocation load(String name) {
		textures.get(name).fill(image);
		tex.updateDynamicTexture();
		return location;
	}
	
	public static void tick() {
		ArrayList<String> toRemove = new ArrayList<>();
		for (String typeName : textures.keySet()) {
			ToolTexture texture = textures.get(typeName);
			if (texture.tick()) {
				toRemove.add(typeName);
			}
		}
	}
	
	public static ToolTexture get(String toLowerCase) {
		if (!textures.containsKey(toLowerCase)) textures.put(toLowerCase, new ToolTexture());
		return textures.get(toLowerCase);
	}
	
	public static boolean has(String name) {
		return textures.containsKey(name);
	}
}
