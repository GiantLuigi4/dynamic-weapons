package com.tfc.dynamicweaponry.data;

import com.tfc.assortedutils.utils.Color;
import net.minecraft.util.ResourceLocation;

public class Material {
	public final int color;
	public final int colorBorder;
	public final int durability;
	public final double weight;
	public final double efficiency;
	public final double attack;
	public final ResourceLocation item;
	
	public Material(int color, int durability, double weight, double efficiency, double attack, ResourceLocation item) {
		this.color = color;
		this.colorBorder = new Color(color).darker(0.5f, -12).getRGB();
		this.durability = durability;
		this.weight = weight;
		this.efficiency = efficiency;
		this.attack = attack;
		this.item = item;
	}
	
	public Material(int color, int colorBorder, int durability, double weight, double efficiency, double attack, ResourceLocation item) {
		this.color = color;
		this.colorBorder = colorBorder;
		this.durability = durability;
		this.weight = weight;
		this.efficiency = efficiency;
		this.attack = attack;
		this.item = item;
	}
}
