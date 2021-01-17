package com.tfc.dynamicweaponry.utils;

import net.minecraft.util.math.MathHelper;

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
}
