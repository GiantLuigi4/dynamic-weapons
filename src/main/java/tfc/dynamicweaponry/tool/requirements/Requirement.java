package tfc.dynamicweaponry.tool.requirements;

import net.minecraft.resources.ResourceLocation;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.util.Point;

public abstract class Requirement {
	public abstract Point[] requiredPoints(Tool tool);
	public abstract boolean test(Tool tool);
	public abstract ResourceLocation name();
}
