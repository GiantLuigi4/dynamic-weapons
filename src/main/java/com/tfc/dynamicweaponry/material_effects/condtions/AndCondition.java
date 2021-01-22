package com.tfc.dynamicweaponry.material_effects.condtions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.item.tool.Tool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

//TODO: optimization via storing the inner conditions
public class AndCondition extends EffectCondition {
	public AndCondition(JsonObject info) {
		super(info);
	}
	
	public AndCondition() {
	}
	
	@Override
	public EffectCondition create(JsonObject info) {
		return new AndCondition(info);
	}
	
	@Override
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material, boolean previous) {
		JsonObject conditions = info.getAsJsonObject("conditions");
		boolean val = true;
		for (Map.Entry<String, JsonElement> stringJsonElementEntry : conditions.entrySet()) {
			if (!val) break;
			EffectCondition condition = EffectCondition.get(new ResourceLocation(stringJsonElementEntry.getKey()));
			condition = condition.create((JsonObject) stringJsonElementEntry.getValue());
			val = val && condition.test(entity, world, state, pos, tool, material);
		}
		return val || previous;
	}
}
