package tfc.dynamicweaponry.material_effects.condtions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.Tool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ModulusTickCondition extends EffectCondition {
	private final String comparator;
	private final int amt;
	private final int mod;
	
	public ModulusTickCondition(JsonObject info) {
		super(info);
		String comparator = null;
		for (Map.Entry<String, JsonElement> stringJsonElementEntry : info.entrySet()) {
			if (stringJsonElementEntry.getKey().equals("equals")) {
				comparator = "equals";
				break;
			} else if (stringJsonElementEntry.getKey().equals("less_than") || stringJsonElementEntry.getKey().equals("less_then")) {
				comparator = "less_than";
				break;
			} else if (stringJsonElementEntry.getKey().equals("greater_than") || stringJsonElementEntry.getKey().equals("greater_then")) {
				comparator = "greater_than";
				break;
			}
		}
		if (comparator == null) throw new RuntimeException("comparator was not found");
		this.comparator = comparator;
		amt = info.getAsJsonPrimitive(comparator).getAsInt();
		mod = info.getAsJsonPrimitive("mod").getAsInt();
	}
	
	public ModulusTickCondition() {
		comparator = null;
		amt = 0;
		mod = 0;
	}
	
	@Override
	@Nonnull
	public EffectCondition create(JsonObject info) {
		return new ModulusTickCondition(info);
	}
	
	@Override
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material) {
		if (comparator.equals("equals")) {
			if (world.getGameTime() % mod == amt) return true;
		} else if (comparator.equals("less_than")) {
			if (world.getGameTime() % mod < amt) return true;
		} else if (comparator.equals("greater_than")) {
			if (world.getGameTime() % mod > amt) return true;
		}
		return super.test(entity, world, state, pos, tool, material);
	}
}
