package com.tfc.dynamicweaponry.tool;

import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Tool {
	public final ArrayList<ToolComponent> components = new ArrayList<>();
	public String name;
	
	public Tool(ItemStack item) {
		CompoundNBT nbt = item.getOrCreateTag();
		
		if (item.getOrCreateTag().contains("tool_info")) nbt = item.getOrCreateTag().getCompound("tool_info");
		
		if (nbt.contains("tool_type")) name = nbt.getString("tool_type");
		else name = "dynamic_weaponry:single_head";
		
		ListNBT palletNBT;
		if (nbt.contains("pallet")) palletNBT = nbt.getList("pallet", Constants.NBT.TAG_COMPOUND);
		else palletNBT = new ListNBT();
		ResourcePallet pallet = new ResourcePallet(palletNBT);
		
		if (nbt.contains("parts")) {
			CompoundNBT parts = nbt.getCompound("parts");
			for (String s : parts.keySet()) {
				CompoundNBT part = parts.getCompound(s);
				try {
					components.add(new ToolComponent(part, pallet));
				} catch (Throwable ignored) {
				}
			}
		}
	}
	
	public double getDamage() {
		double amt = 0;
		
		for (ToolComponent component : this.components) {
			int count = 0;
			HashMap<Material, Integer> counts = new HashMap<>();
			
			if (component.points.isEmpty()) continue;
			
			for (MaterialPoint point : component.points) {
				Material mat = Loader.INSTANCE.getMaterial(point.material);
				count++;
				if (!counts.containsKey(mat)) counts.put(mat, 1);
				else counts.replace(mat, counts.get(mat) + 1);
			}
			
			for (Material mat : counts.keySet()) {
				Integer matAmt = counts.get(mat);
				float matPercent = ((float) matAmt) / count;
				if (mat != null) amt += matPercent * mat.attack;
				amt /= 1.3f;
			}
		}


//		amt -= Math.max(0,20-count);
		
		return amt + (getWeight() / 20f) - 1;
	}
	
	public double getWeight() {
		double amt = 0;
		for (ToolComponent component : this.components) {
			for (MaterialPoint point : component.points) {
				Material mat = Loader.INSTANCE.getMaterial(point.material);
				if (mat != null)
					amt += mat.weight;
			}
		}
		return amt;
	}
	
	public double getEfficiency() {
		double amt = 0;
		
		
		for (ToolComponent component : this.components) {
			int count = 0;
			HashMap<Material, Integer> counts = new HashMap<>();
			
			for (MaterialPoint point : component.points) {
				Material mat = Loader.INSTANCE.getMaterial(point.material);
				count++;
				if (!counts.containsKey(mat)) counts.put(mat, 1);
				else counts.replace(mat, counts.get(mat) + 1);
			}
			
			for (Material mat : counts.keySet()) {
				Integer matAmt = counts.get(mat);
				float matPercent = ((float) matAmt) / count;
				if (mat != null) amt += matPercent * mat.efficiency;
				amt /= 1.3;
			}
		}
		
		return amt;
	}
	
	public double getAttackSpeed() {
		return MathHelper.lerp(0.25, ((getWeight()) * (0.1 / getEfficiency())), 1.6) / 2;
	}
	
	public double getDurability() {
		double amt = 0;
		
		float scl = 0.01f;

//		int count = 0;
//		HashMap<Material,Integer> counts = new HashMap<>();
		
		for (ToolComponent component : this.components) {
			for (MaterialPoint point : component.points) {
				Material mat = Loader.INSTANCE.getMaterial(point.material);
//				count++;
//				if (!counts.containsKey(mat)) counts.put(mat, 1);
//				else counts.replace(mat, counts.get(mat) + 1);
				
				if (mat != null) amt += mat.durability * scl;
			}
		}

//		for (Material mat : counts.keySet()) {
//			Integer matAmt = counts.get(mat);
//			float matPercent = ((float) matAmt) / count;
//			amt += matPercent * mat.efficiency;
//		}
		
		return amt;
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
		ResourcePallet pallet = new ResourcePallet();
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				if (!pallet.contains(point.material)) {
					pallet.add(pallet.size(), point.material);
				}
			}
		}
		for (ToolComponent component : components) {
			parts.put(component.name, component.serialize(pallet));
		}
		thisNBT.put("parts", parts);
		thisNBT.put("pallet", pallet.serialize());
		return thisNBT;
	}
	
	public Tool copy() {
		sort();
		CompoundNBT nbt = this.serialize();
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt1.put("tool_info", nbt);
		Tool tool = new Tool(stack);
		tool.sort();
		return tool;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tool tool = (Tool) o;
		return Objects.equals(components, tool.components) &&
				Objects.equals(name, tool.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(components, name);
	}
}
