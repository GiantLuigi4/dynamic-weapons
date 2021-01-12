package com.tfc.dynamicweaponry.tool;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class Tool {
	public final ArrayList<ToolComponent> components = new ArrayList<>();
	public String name;
	
	public Tool(ItemStack item) {
		CompoundNBT nbt = item.getOrCreateTag();
		
		if (item.getOrCreateTag().contains("tool_info")) nbt = item.getOrCreateTag().getCompound("tool_info");
		
		if (nbt.contains("tool_type")) name = nbt.getString("tool_type");
		else name = "dynamic_weaponry:single_head";
		
		if (nbt.contains("parts")) {
			CompoundNBT parts = nbt.getCompound("parts");
			for (String s : parts.keySet()) {
				CompoundNBT part = parts.getCompound(s);
				try {
					components.add(new ToolComponent(part));
				} catch (Throwable ignored) {
				}
			}
		}
	}
	
	public Tool(String name) {
		this.name = name;
	}
	
	public void sort() {
		components.sort(ToolComponent::compareTo);
	}
	
	public CompoundNBT serialize() {
		CompoundNBT thisNBT = new CompoundNBT();
		thisNBT.putString("tool_type", name);
		CompoundNBT parts = new CompoundNBT();
		for (ToolComponent component : components) parts.put(component.name, component.serialize());
		thisNBT.put("parts", parts);
		return thisNBT;
	}
}
