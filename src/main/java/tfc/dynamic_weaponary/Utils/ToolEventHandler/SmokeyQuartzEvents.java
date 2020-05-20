package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class SmokeyQuartzEvents {
	public void hitEntity(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
		livingEntity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int) (60 * aFloat), 1));
	}
}
