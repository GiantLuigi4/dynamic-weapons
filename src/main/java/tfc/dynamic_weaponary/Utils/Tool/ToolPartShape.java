package tfc.dynamic_weaponary.Utils.Tool;

public class ToolPartShape {
	ToolPartType type;
	
	public ToolPartShape(ToolPartType type) {
		this.type = type;
	}
	
	public boolean check(ToolPartShape shape) {
		return false;
	}
}
