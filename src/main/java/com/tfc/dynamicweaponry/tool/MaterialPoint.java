package com.tfc.dynamicweaponry.tool;

import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MaterialPoint that = (MaterialPoint) o;
		return Objects.equals(material, that.material);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(material);
	}
}
