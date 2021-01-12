package com.tfc.dynamicweaponry.tool;

import com.tfc.assortedutils.API.nbt.ExtendedCompoundNBT;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.PartType;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class ToolComponent implements Comparable<ToolComponent> {
	public ArrayList<MaterialPoint> points;
	public String name;
	public PartType type;
	
	public ToolComponent(CompoundNBT nbt) {
		name = nbt.getString("name");
		type = Loader.INSTANCE.partTypes.get(new ResourceLocation(name));
		
		points = new ArrayList<>();
		if (nbt.contains("points")) {
			ListNBT pointsList = nbt.getList("points", Constants.NBT.TAG_COMPOUND);
			
			for (INBT inbt : pointsList) {
				ExtendedCompoundNBT compound = new ExtendedCompoundNBT((CompoundNBT) inbt, true);
				ResourceLocation mat = new ResourceLocation(compound.getString("material"));
				int x = compound.getInt("x");
				int y = compound.getInt("y");
				points.add(new MaterialPoint(x, y, mat));
			}
		}
	}
	
	public ListNBT serialize() {
		ListNBT thisNBT = new ListNBT();
		for (MaterialPoint point : points) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString("material", point.material.toString());
			nbt.putInt("x", point.x);
			nbt.putInt("y", point.y);
			thisNBT.add(nbt);
		}
		return thisNBT;
	}
	
	public void setPoint(Point pos, ResourceLocation material) {
		if (material != null) {
			setPoint(pos, null);
			points.add(new MaterialPoint(pos.x, pos.y, material));
		} else {
			MaterialPoint toRemove = null;
			for (MaterialPoint point : points)
				if (point.x == pos.x && point.y == pos.y) {
					toRemove = point;
					break;
				}
			while (toRemove != null) {
				for (MaterialPoint point : points)
					if (point.x == pos.x && point.y == pos.y) {
						toRemove = point;
						break;
					}
				if (toRemove != null) points.remove(toRemove);
				toRemove = null;
			}
		}
	}
	
	public MaterialPoint getPoint(int x, int y) {
		for (MaterialPoint point : points) {
			if (point.x == x && point.y == y) {
				return point;
			}
		}
		return null;
	}
	
	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 *
	 * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)
	 *
	 * <p>The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.
	 *
	 * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.
	 *
	 * <p>It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 *
	 * <p>In the foregoing description, the notation
	 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
	 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
	 * <tt>0</tt>, or <tt>1</tt> according to whether the value of
	 * <i>expression</i> is negative, zero or positive.
	 *
	 * @param o the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 * is less than, equal to, or greater than the specified object.
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException   if the specified object's type prevents it
	 *                              from being compared to this object.
	 */
	@Override
	public int compareTo(ToolComponent o) {
		if (this.type == null) return 1;
		if (o.type == null) return -1;
		return Integer.compare(type.renderLayer, o.type.renderLayer);
	}
}
