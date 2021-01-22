package com.tfc.dynamicweaponry.material_effects.condtions;

import com.google.gson.JsonObject;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.item.tool.Tool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DistributionCondition extends EffectCondition {
	public DistributionCondition(JsonObject info) {
		super(info);
	}
	
	public DistributionCondition() {
	}
	
	@Override
	public EffectCondition create(JsonObject info) {
		return new DistributionCondition(info);
	}
	
	@Override
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material) {
		String side = info.getAsJsonPrimitive("side").getAsString();
		if (side.equals("server")) return !world.isRemote;
		else return world.isRemote;
	}
}
