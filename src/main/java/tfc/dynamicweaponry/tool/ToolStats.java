package tfc.dynamicweaponry.tool;

// TODO
public class ToolStats {
	private final double attack;
	private final double durability;
	private final double speed;
	private final int[] bladeShape;
	private final String[] toolTypes;
	
	public ToolStats(double attack, double durability, double speed, int[] bladeShape, String[] toolTypes) {
		this.attack = attack;
		this.durability = durability;
		this.speed = speed;
		this.bladeShape = bladeShape;
		this.toolTypes = toolTypes;
	}
}
