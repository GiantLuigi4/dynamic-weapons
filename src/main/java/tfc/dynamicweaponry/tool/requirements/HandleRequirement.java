package tfc.dynamicweaponry.tool.requirements;

import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.tool.ToolLayer;
import tfc.dynamicweaponry.util.Point;

public class HandleRequirement extends Requirement {
	public static final HandleRequirement INSTANCE = new HandleRequirement();
	
	Point[] requiredPoints = new Point[]{
			new Point(3, 13),
			new Point(2, 12),
			new Point(3, 12),
			new Point(4, 12),
			new Point(5, 11),
			new Point(4, 11),
			new Point(3, 11),
			new Point(4, 10),
			new Point(5, 10),
	};
	
	@Override
	public Point[] requiredPoints(Tool tool) {
		return requiredPoints;
	}
	
	@Override
	public boolean test(Tool tool) {
		int count = 0;
		for (Point requiredPoint : requiredPoints(tool)) {
			for (ToolLayer layer : tool.getLayers()) {
				if (layer.get(requiredPoint.x, 15 - requiredPoint.y) != null) {
					count++;
					break;
				}
			}
		}
		return count > 4;
	}
	
	ResourceLocation name = new ResourceLocation("dynamic_weaponry:handle");
	
	@Override
	public ResourceLocation name() {
		return name;
	}
}
