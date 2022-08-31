package tfc.dynamicweaponry.tool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.Material;
import tfc.dynamicweaponry.loading.Materials;

import java.util.ArrayList;

public class ToolLayer {
	Material[] materials;
	
	public ToolLayer() {
		materials = new Material[16 * 16];
	}
	
	public ToolLayer(Material[] materials) {
		this.materials = materials;
	}
	
	private static int index(int x, int y) {
		return x * 16 + y;
	}
	
	public static ToolLayer fromTag(Tag tg) {
		if (tg instanceof CompoundTag tag) {
			CompoundTag materialData = getCompound(tag, "pallet");
			CompoundTag layerData = getCompound(tag, "layer");
			ToolLayer layer = new ToolLayer();
			for (String allKey : layerData.getAllKeys()) {
				try {
					int index = Integer.parseInt(allKey);
					String regName = materialData.getString(String.valueOf(layerData.getInt(allKey)));
					layer.materials[index] = Materials.get(new ResourceLocation(regName));
				} catch (Throwable err) {
					err.printStackTrace();
				}
			}
			return layer;
		}
		return null;
	}
	
	protected static String getIfPresent(CompoundTag tg, String name, String value) {
		if (tg.contains(name, Tag.TAG_STRING)) return tg.getString(name);
		return value;
	}
	
	protected static int getIfPresent(CompoundTag tg, String name, int value) {
		if (tg.contains(name, Tag.TAG_INT)) return tg.getInt(name);
		return value;
	}
	
	protected static CompoundTag getCompound(CompoundTag tg, String name) {
		if (tg.contains(name, Tag.TAG_COMPOUND)) return tg.getCompound(name);
		return null;
	}
	
	public void set(int i, int i1, Material mat) {
		materials[index(i, i1)] = mat;
	}
	
	public Material get(int x, int y) {
		return materials[index(x, y)];
	}
	
	public Material[] array() {
		return materials;
	}
	
	public Tag toTag() {
		ArrayList<String> usedRegNames = new ArrayList<>();
		CompoundTag materialData = new CompoundTag();
		CompoundTag materialDataReverse = new CompoundTag();
		for (Material pxl : materials) {
			if (pxl != null) {
				if (!usedRegNames.contains(pxl.regName.toString())) {
					materialData.putInt(pxl.regName.toString(), usedRegNames.size());
					materialDataReverse.putString(String.valueOf(usedRegNames.size()), pxl.regName.toString());
					usedRegNames.add(pxl.regName.toString());
				}
			}
		}
		
		CompoundTag layer = new CompoundTag();
		for (int i = 0; i < materials.length; i++) {
			Material pxl = materials[i];
			if (pxl != null)
				layer.putInt(String.valueOf(i), materialData.getInt(pxl.regName.toString()));
		}
		
		CompoundTag layerTag = new CompoundTag();
		layerTag.put("layer", layer);
		layerTag.put("pallet", materialDataReverse);
		layerTag.putInt("width", 16);
		layerTag.putInt("height", 16);
		return layerTag;
	}
}
