package com.tfc.dynamicweaponry.tool;

import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.util.ResourceLocation;

public class MaterialPoint extends Point {
	public final ResourceLocation material;
	
	public MaterialPoint(int x, int y, ResourceLocation material) {
		super(x, y);
		this.material = material;
	}
}
