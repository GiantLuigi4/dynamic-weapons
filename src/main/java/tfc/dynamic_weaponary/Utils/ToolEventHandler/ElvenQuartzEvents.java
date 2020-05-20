package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.apache.logging.log4j.util.TriConsumer;

public class ElvenQuartzEvents {
	public static TriConsumer<LivingEntity, LivingEntity, Float> hitEvent = new TriConsumer<LivingEntity, LivingEntity, Float>() {
		@Override
		public void accept(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
			if (livingEntity.getTeam().isSameTeam(playerEntity.getTeam())) {
				livingEntity.addPotionEffect(new EffectInstance(Effects.LUCK, (int) (60 * aFloat), (int) Math.ceil(aFloat * 4)));
			} else {
				livingEntity.addPotionEffect(new EffectInstance(Effects.UNLUCK, (int) (60 * aFloat), (int) Math.ceil(aFloat * 4)));
			}
		}
	};
}
