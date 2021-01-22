package com.tfc.dynamicweaponry.material_effects.effects;

import com.tfc.dynamicweaponry.item.tool.Tool;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SelfRepairEffect extends ToolEffect {
	@Override
	public void onInventoryTick(ItemStack stack, Tool tool, float materialPercent, EffectInstance instance, boolean isSelected, @Nullable LivingEntity owner) {
		super.onInventoryTick(stack, tool, materialPercent, instance, isSelected, owner);
		if (owner != null)
			stack.setDamage(stack.getDamage() - instance.info.getAsJsonPrimitive("amount").getAsInt());
	}
}
