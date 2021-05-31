package tfc.dynamicweaponry.item.tool;

import com.tfc.assortedutils.API.nbt.ExtendedCompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import tfc.dynamicweaponry.data.DataLoader;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.data.PartType;
import tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;
import java.util.Objects;

public class ToolComponent implements Comparable<ToolComponent> {
	public ArrayList<MaterialPoint> points;
	public String name;
	public PartType type;
	
	public ToolComponent(CompoundNBT nbt) {
		deserialize(nbt, new ResourcePallet());
	}
	
	public ToolComponent(CompoundNBT nbt, ResourcePallet pallet) {
		deserialize(nbt, pallet);
	}
	
	public void deserialize(CompoundNBT nbt, ResourcePallet pallet) {
		name = nbt.getString("name");
		type = DataLoader.INSTANCE.partTypes.get(new ResourceLocation(name));
		
		points = new ArrayList<>();
		if (nbt.contains("points")) {
			ListNBT pointsList = nbt.getList("points", Constants.NBT.TAG_COMPOUND);
			
			for (INBT inbt : pointsList) {
				ExtendedCompoundNBT compound = new ExtendedCompoundNBT((CompoundNBT) inbt, true);
				ResourceLocation mat;
				
				if (pallet.size() > 0)
					mat = pallet.getFromInt(compound.getInt("material"));
				else mat = new ResourceLocation(compound.getString("material"));
				
				int x = compound.getInt("x");
				int y = compound.getInt("y");
				points.add(new MaterialPoint(x, y, mat));
			}
		}
	}
	
	public CompoundNBT serialize(ResourcePallet pallet) {
		CompoundNBT thisNBT = new CompoundNBT();
		ListNBT pointList = new ListNBT();
		thisNBT.putString("name", name);
		
		for (MaterialPoint point : points) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("material", pallet.getFromLocation(new ResourceLocation(point.material.toString())));
			nbt.putInt("x", point.x);
			nbt.putInt("y", point.y);
			pointList.add(nbt);
		}
		
		thisNBT.put("points", pointList);
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
	
	@Override
	public int compareTo(ToolComponent o) {
		if (this.type == null) return 1;
		if (o.type == null) return -1;
		int v = Integer.compare(type.renderLayer, o.type.renderLayer);
		if (v == 0) return Integer.compare(name.hashCode(), o.name.hashCode());
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolComponent component = (ToolComponent) o;
		return Objects.equals(points, component.points) &&
				Objects.equals(name, component.name) &&
				Objects.equals(type, component.type);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(points, name, type);
	}
	
	public boolean checkPoint(Point requiredPoint) {
		ArrayList<Point> checkedPoints = new ArrayList<>();
		ArrayList<Point> justCheckedPoints = new ArrayList<>();
		ArrayList<Point> checkingPoints = new ArrayList<>();
		
		if (this.getPoint(requiredPoint.x, requiredPoint.y) != null) checkingPoints.add(requiredPoint);
		else return false;
		
		int numHit = 0;
		
		for (Point requiredPoint1 : this.type.getRequiredPoints()) {
			boolean hit = false;
			
			if (checkedPoints.contains(requiredPoint1)) {
				numHit++;
				continue;
			} else {
				checkingPoints.addAll(checkedPoints);
				checkedPoints.clear();
			}
			
			if (requiredPoint1.equals(requiredPoint)) continue;
			
			while (!checkingPoints.isEmpty()) {
				for (Point p1 : checkingPoints) {
					for (int xOff = -1; xOff <= 1; xOff++) {
						for (int yOff = -1; yOff <= 1; yOff++) {
							if (!type.isAllowAngles()) if (Math.abs(xOff) == Math.abs(yOff)) continue;
							
							Point p = new Point(p1.x + xOff, p1.y + yOff);
							
							if (
									!checkedPoints.contains(p) &&
											!justCheckedPoints.contains(p) &&
											!checkingPoints.contains(p) &&
											this.getPoint(p.x, p.y) != null
							) {
								justCheckedPoints.add(p);
								
								if (p.x == requiredPoint1.x && p.y == requiredPoint1.y) {
									hit = true;
									break;
								}
							}
						}
						
						if (hit) break;
					}
					
					if (hit) break;
				}
				checkingPoints.clear();
				checkingPoints.addAll(justCheckedPoints);
				checkedPoints.addAll(checkingPoints);
				justCheckedPoints.clear();
				
				if (hit) {
					numHit++;
					break;
				}
			}
		}
		
		return numHit >= (this.type.getRequiredPoints().length) - 1;
	}
	
	public float getHarvestLevel() {
		float amt = 0;
		int pointCount = 0;
		for (MaterialPoint point : points) {
			Material material = DataLoader.INSTANCE.getMaterial(point.material);
			if (material != null && material.getHarvestLevel() != -1) {
				pointCount++;
				amt += material.getHarvestLevel();
			}
		}
		amt /= (float) pointCount;
		return amt;
	}
}
