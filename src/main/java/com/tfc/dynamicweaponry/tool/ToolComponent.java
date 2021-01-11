package com.tfc.dynamicweaponry.tool;

import com.tfc.assortedutils.API.nbt.ExtendedCompoundNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class ToolComponent {
	public ArrayList<MaterialPoint> points;
	public String name;
	
	public ToolComponent(CompoundNBT nbt) {
		name = nbt.getString("name");
		ListNBT pointsList = nbt.getList("points", Constants.NBT.TAG_COMPOUND);
		
		points = new ArrayList<>();
		
		for (INBT inbt : pointsList) {
			ExtendedCompoundNBT compound = new ExtendedCompoundNBT((CompoundNBT) inbt, true);
			ResourceLocation mat = new ResourceLocation(compound.getString("material"));
			int x = compound.getInt("x");
			int y = compound.getInt("y");
			points.add(new MaterialPoint(x, y, mat));
		}
	}
}
