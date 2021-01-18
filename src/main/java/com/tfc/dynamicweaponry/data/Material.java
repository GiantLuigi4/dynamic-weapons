package com.tfc.dynamicweaponry.data;

import net.minecraft.util.ResourceLocation;

public class Material {
	public final int durability;
	public final double weight;
	public final double efficiency;
	public final double attack;
	public final ResourceLocation item;
	protected boolean isDyable = false;
	protected float harvestLevel = 0;
	
	private boolean isLocked = false;
	
	public float getHarvestLevel() {
		return harvestLevel;
	}
	
	public Material(int durability, double weight, double efficiency, double attack, ResourceLocation item) {
		this.durability = durability;
		this.weight = weight;
		this.efficiency = efficiency;
		this.attack = attack;
		this.item = item;
	}
	
	public boolean isDyable() {
		return isDyable;
	}
	
	protected void setDyable(boolean dyable) {
		if (isLocked)
			throw new RuntimeException(new IllegalAccessException("Setting a material to dyable after it's been locked"));
		this.isDyable = dyable;
	}
	
	protected void lock() {
		this.isLocked = true;
	}
}
