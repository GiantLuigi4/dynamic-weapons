package tfc.dynamicweaponry.material_effects.effects;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.material_effects.condtions.EffectCondition;

public class EffectInstance {
	public final ToolEffect toolEffect;
	public final JsonObject info;
	public final EffectCondition[] conditions;
	public final Material owner;
	
	public EffectInstance(String key, JsonObject info, EffectCondition[] conditions, Material owner) {
		toolEffect = ToolEffect.get(new ResourceLocation(key));
		this.info = info;
		this.conditions = conditions;
		this.owner = owner;
	}
	
	public boolean test(LivingEntity entity, World world, BlockState state, BlockPos pos, Tool tool, Material mat) {
		if (conditions.length == 0) return true;
		boolean val = false;
		for (EffectCondition condition : conditions) {
			val = condition.test(entity, world, state, pos, tool, mat, val);
		}
		return val;
	}
}
