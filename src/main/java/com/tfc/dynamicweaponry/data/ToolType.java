package com.tfc.dynamicweaponry.data;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Objects;

public class ToolType {
	private final ArrayList<ToolPart> parts = new ArrayList<>();
	
	protected boolean isBow = false;
	
	public boolean isBow() {
		return isBow;
	}
	
	private boolean isLocked = false;
	
	public ToolType() {
	}
	
	public void lock() {
		isLocked = true;
	}
	
	public void addPart(ToolPart part) {
		if (isLocked) throw new RuntimeException(new IllegalAccessException("Adding a part type after the tool type is locked"));
		parts.add(part);
	}
	
	public ToolPart[] getParts() {
		return parts.toArray(new ToolPart[0]);
	}
	
	@Override
	public String toString() {
		return "ToolType{" +
				"parts=" + parts +
				", isLocked=" + isLocked +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolType toolType = (ToolType) o;
		return isLocked == toolType.isLocked &&
				Objects.equals(parts, toolType.parts);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(parts, isLocked);
	}
	
	public ToolPart getPart(ResourceLocation type) {
		for (ToolPart part : parts) {
			if (part.type != null && part.type.name.equals(type)) {
				return part;
			}
		}
		return null;
	}
}
