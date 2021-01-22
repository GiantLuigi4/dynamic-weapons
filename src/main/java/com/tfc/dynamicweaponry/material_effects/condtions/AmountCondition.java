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

public class AmountCondition extends EffectCondition {
	public final float percent;
	
	public AmountCondition(JsonObject info) {
		super(info);
		percent = info.getAsJsonPrimitive("percent").getAsFloat();
	}
	
	public AmountCondition() {
		percent = 0;
	}
	
	@Override
	public EffectCondition create(JsonObject info) {
		return new AmountCondition(info);
	}
	
	@Override
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material) {
		float amt = tool.calcPercent(material);
		return amt <= percent;
	}
}
