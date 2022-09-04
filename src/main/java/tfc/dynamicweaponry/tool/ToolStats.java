package tfc.dynamicweaponry.tool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

// TODO
public class ToolStats {
	private final double attack;
	private final double durability;
	private final double speed;
	private final boolean[] frontBlade;
	private final String type0;
	private final boolean[] backBlade;
	private final String type1;
	
	public ToolStats(double attack, double durability, double speed, boolean[] frontBlade, String type0, boolean[] backBlade, String type1) {
		this.attack = attack;
		this.durability = durability;
		this.speed = speed;
		this.frontBlade = frontBlade;
		this.type0 = type0;
		this.backBlade = backBlade;
		this.type1 = type1;
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putDouble("attack", attack);
		tag.putDouble("durability", durability);
		tag.putDouble("speed", speed);
		ListTag blade = new ListTag();
		for (int i = 0; i < frontBlade.length; i++) if (frontBlade[i]) blade.add(IntTag.valueOf(i));
		tag.put("frontHead", blade);
		if (type0 != null) tag.putString("type0", type0);
		if (type1 != null) tag.putString("type0", type1);
		return tag;
	}
}
