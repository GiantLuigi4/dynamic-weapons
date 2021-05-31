package tfc.dynamicweaponry.utils;

public class Line {
	final Point pointA, pointB;
	
	public Line(Point pointA, Point pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
	}
	
	public double dist(Point point) {
		double minDist = Double.MAX_VALUE;
		for (int i = 0; i < 32; i++) {
			minDist = Math.min(minDist, pointA.lerp(i / 32f, pointB).distance(point));
		}
		return minDist;
	}
	
	public Point getInterp(double val) {
		return pointA.lerp((float) val, pointB);
	}
}
