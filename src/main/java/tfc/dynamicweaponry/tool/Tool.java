package tfc.dynamicweaponry.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import tfc.dynamicweaponry.util.TextureGen;
import tfc.dynamicweaponry.util.ToolImage;

import java.util.ArrayList;

public class Tool {
	public static final String formatVersion = "a";
	
	private ToolImage image;
	private final ToolLayer[] layers;
	
	private static final ArrayList<Integer> freeInts = new ArrayList<>();
	private static int latest = 0;
	private int id;
	
	public ToolImage getImage() {
		if (image == null) {
			synchronized (freeInts) { // safety measure
				int number = 0;
				if (freeInts.isEmpty()) {
					number = latest;
					latest++;
				} else number = freeInts.remove(0);
				image = TextureGen.generate(layers, number, true);
				image.write();
				id = number;
			}
		}
		return image;
	}
	
	private boolean wasFinalized = false;
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (wasFinalized) return;
		wasFinalized = true;
		try {
			final int id = this.id;
			Minecraft.getInstance().executeIfPossible(() -> {
				synchronized (freeInts) { // safety measure
					if (!freeInts.contains(id))
						freeInts.add(id);
				}
			});
		} catch (Throwable ignored) {
		}
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
