package tfc.dynamicweaponry;

import net.minecraft.resources.ResourceLocation;

public class Material {
	public final int color;
	public final int highlightColor;
	public final float shininess;
	public final ResourceLocation regName;
	
	public Material(int color, int highlightColor, float shininess, ResourceLocation regName) {
		this.color = color;
		this.highlightColor = highlightColor;
		this.shininess = shininess;
		this.regName = regName;
	}
}
