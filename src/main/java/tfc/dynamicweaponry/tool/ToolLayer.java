package tfc.dynamicweaponry.tool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.loading.MaterialLoader;
import tfc.dynamicweaponry.loading.Materials;

import java.util.ArrayList;
import java.util.Arrays;

public class ToolLayer {
	ResourceLocation[] materials;
	
	public ToolLayer() {
		materials = new ResourceLocation[16 * 16];
	}
	
	@Deprecated(forRemoval = true)
	public ToolLayer(Material[] materials) {
		this();
		for (int i = 0; i < materials.length; i++) {
			if (materials[i] != null)
				this.materials[i] = materials[i].regName;
		}
	}
	
	public ToolLayer(Material[] materials, Materials materialList) {
		this(materials);
		this.materialList = materialList;
		assert materialList != null;
	}
	
	private static int index(int x, int y) {
		return x * 16 + y;
	}
	
	Materials materialList;
	
	public Materials getMaterialList() {
		assert materialList != null;
		return materialList;
	}
	
	public static ToolLayer fromTag(MaterialLoader loader, Tag tg) {
		if (tg instanceof CompoundTag tag) {
			CompoundTag materialData = getCompound(tag, "pallet");
			CompoundTag layerData = getCompound(tag, "layer");
			assert layerData != null;
			assert materialData != null;
			
			ToolLayer layer = new ToolLayer();
			layer.materialList = loader.getMaterialHolder();
			assert layer.materialList != null;
			
			for (String allKey : layerData.getAllKeys()) {
				try {
					int index = Integer.parseInt(allKey);
					String regName = materialData.getString(String.valueOf(layerData.getInt(allKey)));
					layer.materials[index] = new ResourceLocation(regName);
				} catch (Throwable err) {
					err.printStackTrace();
				}
			}
			return layer;
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolLayer layer = (ToolLayer) o;
		return Arrays.equals(materials, layer.materials);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(materials);
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
		int index = index(i, i1);
		if (index < 0 || index > materials.length) return;
		if (mat == null) materials[index] = null;
		else materials[index] = mat.regName;
	}
	
	public Material get(int x, int y) {
		return get(index(x, y));
	}
	
	public Material[] array() {
		Material[] materials = new Material[this.materials.length];
		for (int i = 0; i < materials.length; i++) {
			materials[i] = get(i);
		}
		return materials;
	}
	
	protected Material get(int i) {
		ResourceLocation rl = materials[i];
		if (rl != null) return materialList.get(rl);
		return null;
	}
	
	public Tag toTag() {
		ArrayList<String> usedRegNames = new ArrayList<>();
		CompoundTag materialData = new CompoundTag();
		CompoundTag materialDataReverse = new CompoundTag();
		for (ResourceLocation px1 : materials) {
			if (px1 == null) continue;
			if (!usedRegNames.contains(px1.toString())) {
				materialData.putInt(px1.toString(), usedRegNames.size());
				materialDataReverse.putString(String.valueOf(usedRegNames.size()), px1.toString());
				usedRegNames.add(px1.toString());
			}
		}
		
		CompoundTag layer = new CompoundTag();
		for (int i = 0; i < materials.length; i++) {
			ResourceLocation px1 = materials[i];
			if (px1 == null) continue;
			layer.putInt(String.valueOf(i), materialData.getInt(px1.toString()));
		}
		
		CompoundTag layerTag = new CompoundTag();
		layerTag.put("layer", layer);
		layerTag.put("pallet", materialDataReverse);
		layerTag.putInt("width", 16);
		layerTag.putInt("height", 16);
		return layerTag;
	}
}
