package com.tfc.dynamicweaponry.tool;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class ResourcePallet {
	private final HashMap<Integer, ResourceLocation> locationHashMap = new HashMap<>();
	
	public ResourcePallet(ListNBT nbtList) {
		for (INBT inbt : nbtList) {
			CompoundNBT nbt = (CompoundNBT) inbt;
			int id = nbt.getInt("id");
			ResourceLocation location = new ResourceLocation(nbt.getString("location"));
			locationHashMap.put(id, location);
		}
	}
	
	public ResourcePallet() {
	}
	
	public ResourceLocation getFromInt(int id) {
		return locationHashMap.get(id);
	}
	
	public int getFromLocation(ResourceLocation location) {
		for (int index = 0; index < locationHashMap.values().size(); index++) {
			if (locationHashMap.get(index).equals(location)) {
				return index;
			}
		}
		return -1;
	}
	
	public boolean contains(int id) {
		return locationHashMap.containsKey(id);
	}
	
	public boolean contains(ResourceLocation location) {
		return locationHashMap.containsValue(location);
	}
	
	public void add(int id, ResourceLocation location) {
		locationHashMap.put(id, location);
	}
	
	public int size() {
		return locationHashMap.size();
	}
	
	public ListNBT serialize() {
		ListNBT thisNBT = new ListNBT();
		for (int i = 0; i < size(); i++) {
			ResourceLocation location = locationHashMap.get(i);
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("id", i);
			nbt.putString("location", location.toString());
			thisNBT.add(nbt);
		}
//		locationHashMap.forEach((id, name) -> {
//			CompoundNBT nbt = new CompoundNBT();
//			nbt.putInt("id", id);
//			nbt.putString("location", name.toString());
//		});
		return thisNBT;
	}
}
