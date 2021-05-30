package tfc.dynamicweaponry.material_effects.condtions;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.Tool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EffectCondition implements IForgeRegistryEntry<EffectCondition> {
	protected static final Object2ObjectLinkedOpenHashMap<ResourceLocation, EffectCondition> conditions = new Object2ObjectLinkedOpenHashMap<>();
	public final JsonObject info;
	private ResourceLocation name;
	
	public EffectCondition(JsonObject info) {
		this.info = info;
	}
	
	public EffectCondition() {
		info = null;
	}
	
	public static EffectCondition get(ResourceLocation location) {
		return conditions.getOrDefault(location, null);
	}
	
	public abstract EffectCondition create(JsonObject info);
	
	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}
	
	@Override
	public EffectCondition setRegistryName(ResourceLocation name) {
		this.name = name;
		return this;
	}
	
	@Override
	public Class<EffectCondition> getRegistryType() {
		return EffectCondition.class;
	}
	
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material) {
		return false;
	}
	
	public boolean test(@Nullable LivingEntity entity, @Nonnull World world, @Nullable BlockState state, @Nullable BlockPos pos, Tool tool, Material material, boolean previous) {
		return previous || test(entity, world, state, pos, tool, material);
	}
}
