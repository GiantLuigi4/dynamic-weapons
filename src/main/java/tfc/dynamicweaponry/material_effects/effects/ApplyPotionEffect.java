package tfc.dynamicweaponry.material_effects.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamicweaponry.item.tool.Tool;

public class ApplyPotionEffect extends ToolEffect {
	@Override
	public void onStrike(ItemStack stack, Tool tool, float materialPercent, EffectInstance instance, LivingEntity attacker, LivingEntity target) {
		target.addPotionEffect(
				new net.minecraft.potion.EffectInstance(
						ForgeRegistries.POTIONS.getValue(new ResourceLocation(instance.info.getAsJsonPrimitive("effect").getAsString())),
						(int) Math.max(1, (instance.info.getAsJsonPrimitive("duration").getAsInt() * materialPercent)),
						instance.info.getAsJsonPrimitive("amplifier").getAsInt(),
						false, true
				)
		);
	}
}
