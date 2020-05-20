package tfc.dynamic_weaponary.Utils.Tool;

import tfc.dynamic_weaponary.Utils.DrawingUtils;

public class Material {
	public int durability;
	public double strength;
	public double weight;
	public int color;
	
	public Material(int durability, double strength, double weight, int color) {
		this.durability = durability;
		this.strength = strength;
		this.weight = weight;
		this.color = color;
	}
	
	public Material(int durability, double strength, double weight, DrawingUtils.ColorHelper color) {
		this.durability = durability;
		this.strength = strength;
		this.weight = weight;
		this.color = color.getRGB();
	}
}
