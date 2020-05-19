package tfc.dynamic_weaponary.Utils.Tool;

import net.minecraftforge.common.ToolType;

public enum ToolPartType {
	PICKAXE(ToolType.PICKAXE.getName()),
	AXE(ToolType.AXE.getName()),
	HOE("hoe"),
	BLADE("blade"),
	STICK("stick"),
	NONE("none"),
	SHOVEl(ToolType.SHOVEL.getName());
	
	public String type = "";
	
	ToolPartType(String type) {
		this.type = type;
	}
}
