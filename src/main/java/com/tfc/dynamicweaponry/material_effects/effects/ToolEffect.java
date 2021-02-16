package com.tfc.dynamicweaponry.material_effects.effects;

import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.utils.EnumStat;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class ToolEffect implements IForgeRegistryEntry<ToolEffect> {
	protected static final Object2ObjectLinkedOpenHashMap<ResourceLocation, ToolEffect> effects = new Object2ObjectLinkedOpenHashMap<>();
	private ResourceLocation name;
	
	public static ToolEffect get(ResourceLocation location) {
		return effects.getOrDefault(location, null);
	}
	
	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}
	
	@Override
	public ToolEffect setRegistryName(ResourceLocation name) {
		this.name = name;
		return this;
	}
	
	@Override
	public Class<ToolEffect> getRegistryType() {
		return ToolEffect.class;
	}
	
	public void onStrike(ItemStack stack, Tool tool, float materialPercent, EffectInstance instance, LivingEntity attacker, LivingEntity target) {
	}
	
	public void onInventoryTick(ItemStack stack, Tool tool, float materialPercent, EffectInstance instance, boolean isSelected, @Nullable LivingEntity owner) {
	}
	
	public void onBlockBreak(ItemStack stack, Tool tool, float materialPercent, EffectInstance instance, BlockState broken, BlockPos pos, World world, LivingEntity miner) {
	}
	
	public float calcMaterialStats(ItemStack stack, Tool tool, float materialPercetn, EffectInstance instance, @Nullable Entity wielder, EnumStat stat) {
		return -1;
	}
	
	public void onLeftClick(ItemStack stack, Tool tool, float materialPercetn, EffectInstance instance, @Nullable Entity wielder) {
	}
}
