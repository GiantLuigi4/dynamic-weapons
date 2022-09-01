package tfc.dynamicweaponry.loading;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class ClientMaterial {
	public final int color;
	public final int highlightColor;
	public final float shininess;
	public final ResourceLocation regName;
	
	public ClientMaterial(int color, int highlightColor, float shininess, ResourceLocation regName) {
		this.color = color;
		this.highlightColor = highlightColor;
		this.shininess = shininess;
		this.regName = regName;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClientMaterial material = (ClientMaterial) o;
		return color == material.color && highlightColor == material.highlightColor && Float.compare(material.shininess, shininess) == 0 && Objects.equals(regName, material.regName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(color, highlightColor, shininess, regName);
	}
	
	public Tag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("color", color);
		tag.putInt("highlight", highlightColor);
		tag.putFloat("shininess", shininess);
		tag.putString("name", regName.toString());
	}
}
