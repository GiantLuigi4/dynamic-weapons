package tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.apache.logging.log4j.util.TriConsumer;

public class ElvenQuartzEvents {
	public static TriConsumer consumer = (TriConsumer<LivingEntity, LivingEntity, Float>) ElvenQuartzEvents::hitEntity;
	
	public static void hitEntity(LivingEntity attacked, LivingEntity playerEntity, Float aFloat) {
		try {
			if (attacked.getTeam().isSameTeam(playerEntity.getTeam())) {
				attacked.addPotionEffect(new EffectInstance(Effects.LUCK, (int) (160 * aFloat), (int) Math.ceil(aFloat * 4)));
			} else {
				attacked.addPotionEffect(new EffectInstance(Effects.UNLUCK, (int) (160 * aFloat), (int) Math.ceil(aFloat * 4)));
			}
		} catch (Exception err) {
			attacked.addPotionEffect(new EffectInstance(Effects.UNLUCK, (int) (160 * aFloat), (int) Math.ceil(aFloat * 4)));
		}
	}
}
