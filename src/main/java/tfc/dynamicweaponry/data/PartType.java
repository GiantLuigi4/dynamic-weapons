package tfc.dynamicweaponry.data;

import net.minecraft.util.ResourceLocation;
import tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;
import java.util.Objects;

public class PartType {
	private final ArrayList<Point> requiredPoints = new ArrayList<>();
	private final ArrayList<Point> stabilityPoints = new ArrayList<>();
	public final Point min, max;
	public final int renderLayer;
	public ResourceLocation name;
	protected boolean allowAngles = false;
	
	public boolean isAllowAngles() {
		return allowAngles;
	}
	
	protected String[] contributesTo = null;
	
	private boolean isLocked = false;
	
	public String[] getContributesTo() {
		return contributesTo;
	}
	
	public PartType(ResourceLocation name, Point min, Point max, int renderLayer) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.renderLayer = renderLayer;
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
