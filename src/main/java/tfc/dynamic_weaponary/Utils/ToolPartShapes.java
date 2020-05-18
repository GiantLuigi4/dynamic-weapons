package tfc.dynamic_weaponary.Utils;

import java.util.ArrayList;

public class ToolPartShapes {
	private ArrayList<ToolPartShape> shapes = new ArrayList<>();
	
	public void Register(ToolPartShape shape) {
		shapes.add(shape);
	}
	
	public ToolPartType check(ToolPartShape shape) {
		for (ToolPartShape sp : shapes) {
			if (sp.check(shape)) {
				return shape.type;
			}
		}
		return ToolPartType.NONE;
	}
}
