package tfc.dynamicweaponry.access;

import tfc.dynamicweaponry.tool.Tool;

public interface IMayHoldATool {
	Tool myTool();
	void setTool(Tool tool);
}
