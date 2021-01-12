package com.tfc.dynamicweaponry.tool;

import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MaterialPoint extends Point {
	public final ResourceLocation material;
	
	public MaterialPoint(int x, int y, ResourceLocation material) {
		super(x, y);
		this.material = material;
	}
	
	public MaterialPoint lerp(float pct, MaterialPoint other) {
		float x = MathHelper.lerp(pct, this.x, other.x);
		float y = MathHelper.lerp(pct, this.y, other.y);
		ResourceLocation material = (pct >= 0.5) ? other.material : this.material;
		return new MaterialPoint((int) x, (int) y, material);
	}
}
