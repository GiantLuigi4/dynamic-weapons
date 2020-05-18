package tfc.dynamic_weaponary.Utils;

public class Material {
	public int durability;
	public int strength;
	public float weight;
	public int color;
	
	public Material(int durability, int strength, float weight, int color) {
		this.durability = durability;
		this.strength = strength;
		this.weight = weight;
		this.color = color;
	}
	
	public Material(int durability, int strength, float weight, DrawingUtils.ColorHelper color) {
		this.durability = durability;
		this.strength = strength;
		this.weight = weight;
		this.color = color.getRGB();
	}
}
