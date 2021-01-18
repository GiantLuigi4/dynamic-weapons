package com.tfc.dynamicweaponry.utils;

import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class Point {
	public final int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point lerp(float pct, Point other) {
		float x = MathHelper.lerp(pct, this.x, other.x);
		float y = MathHelper.lerp(pct, this.y, other.y);
		return new Point((int) x, (int) y);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point point = (Point) o;
		return x == point.x &&
				y == point.y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public String toString() {
		return "Point{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
