package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class SunnyQuartzEvents {
	public void hitEntity(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
		livingEntity.addPotionEffect(new EffectInstance(Effects.GLOWING, (int) (20 * aFloat), 1));
	}
}
