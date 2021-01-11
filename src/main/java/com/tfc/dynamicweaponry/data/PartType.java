package com.tfc.dynamicweaponry.data;

import com.tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;
import java.util.Objects;

public class PartType {
	private final ArrayList<Point> requiredPoints = new ArrayList<>();
	private final ArrayList<Point> stabilityPoints = new ArrayList<>();
	
	private final Point min, max;
	
	private boolean isLocked = false;
	
	public PartType(Point min, Point max) {
		this.min = min;
		this.max = max;
	}
	
	public void lock() {
		isLocked = true;
	}
	
	public void addRequiredPoint(Point point) {
		if (isLocked) throw new RuntimeException(new IllegalAccessException("Adding a required point after the part type is locked"));
		requiredPoints.add(point);
	}
	
	public Point[] getRequiredPoints() {
		return requiredPoints.toArray(new Point[0]);
	}
	
	@Override
	public String toString() {
		return "PartType{" +
				"requiredPoints=" + requiredPoints +
				", stabilityPoints=" + stabilityPoints +
				", isLocked=" + isLocked +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PartType partType = (PartType) o;
		return isLocked == partType.isLocked &&
				Objects.equals(requiredPoints, partType.requiredPoints) &&
				Objects.equals(stabilityPoints, partType.stabilityPoints);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(requiredPoints, stabilityPoints, isLocked);
	}
}
