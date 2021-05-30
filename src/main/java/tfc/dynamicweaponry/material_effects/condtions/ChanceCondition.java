package tfc.dynamicweaponry.material_effects.condtions;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.Tool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class ChanceCondition extends EffectCondition {
	public final float chance;
	
	public ChanceCondition(JsonObject info) {
		super(info);
		chance = info.getAsJsonPrimitive("chance").getAsFloat();
	}
	
	public ChanceCondition() {
		chance = 0;
	}
	
	@Override
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material) {
		return new Random(world.getGameTime()).nextDouble() <= chance;
	}
	
	@Override
	public EffectCondition create(JsonObject info) {
		return new ChanceCondition(info);
	}
}
