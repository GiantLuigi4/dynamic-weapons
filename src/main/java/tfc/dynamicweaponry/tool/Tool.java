package tfc.dynamicweaponry.tool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import tfc.dynamicweaponry.util.TextureGen;
import tfc.dynamicweaponry.util.ToolImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Tool {
	public static final String formatVersion = "a";
	
	private AtomicReference<ToolImage> image;
	private final ToolLayer[] layers;
	
	private static final HashMap<Tool, ToolImage> images = new HashMap<>();
	
	boolean hashed = false;
	int hashCode = 0;
	
	public ToolImage getImage() {
		if (!images.containsKey(this) || image == null) {
			if (image == null) {
				if (images.containsKey(this)) {
					image = new AtomicReference<>(images.get(this));
					return image.get();
				}
			}
			synchronized (images) {
				long id = images.size();
				if (images.size() > 3000) {
					ToolImage image = images.remove((Tool) images.keySet().toArray()[0]);
					id = image.id;
					image.close();
				}
				image = new AtomicReference<>(TextureGen.generate(layers, id, true));
				images.put(this, image.get());
				image.get().write();
			}
		}
		return image.get();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tool tool = (Tool) o;
		return Arrays.equals(layers, tool.layers);
	}
	
	@Override
	public int hashCode() {
		if (hashed) return hashCode;
		
		int result = Arrays.hashCode(layers);
		hashCode = result;
		hashed = true;
		
		return result;
	}
	
	public Tool(ToolLayer[] layers) {
		this.layers = layers;
	}
	
	public static Tool fromTag(Tag tg) {
		if (tg instanceof CompoundTag tag) {
			String ver = getIfPresent(tag, "version", null);
			if (ver == null) return null;
			if (ver.equals(formatVersion)) {
				CompoundTag layersTag = getCompound(tag, "layers");
				if (layersTag == null) return null;
				int count = layersTag.size();
				ToolLayer[] layers = new ToolLayer[count];
				Tool tool = new Tool(layers);
				for (String allKey : layersTag.getAllKeys()) {
					try {
						int index = Integer.parseInt(allKey);
						layers[index] = ToolLayer.fromTag(layersTag.get(allKey));
					} catch (Throwable err) {
						err.printStackTrace();
					}
				}
				return tool;
			} // else {} // legacy parsers would go here, if needed
		}
		return null;
	}
	
	protected static String getIfPresent(CompoundTag tg, String name, String value) {
		if (tg.contains(name, Tag.TAG_STRING)) return tg.getString(name);
		return value;
	}
	
	protected static CompoundTag getCompound(CompoundTag tg, String name) {
		if (tg.contains(name, Tag.TAG_COMPOUND)) return tg.getCompound(name);
		return null;
	}
	
	public Tag toTag() {
		CompoundTag toolTag = new CompoundTag();
		toolTag.putString("version", formatVersion);
		CompoundTag statsTag = new CompoundTag();
		// TODO
		toolTag.put("stats", statsTag);
		CompoundTag layers = new CompoundTag();
		for (int i = 0; i < this.layers.length; i++)
			layers.put(String.valueOf(i), this.layers[i].toTag());
		toolTag.put("layers", layers);
		return toolTag;
	}
}
