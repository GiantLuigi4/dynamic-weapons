package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ElvenQuartzEvents {
	public void hitEntity(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
		if (livingEntity.getTeam().isSameTeam(playerEntity.getTeam())) {
			livingEntity.addPotionEffect(new EffectInstance(Effects.LUCK, (int) (60 * aFloat), (int) Math.ceil(aFloat * 4)));
		} else {
			livingEntity.addPotionEffect(new EffectInstance(Effects.UNLUCK, (int) (60 * aFloat), (int) Math.ceil(aFloat * 4)));
		}
	}
}
