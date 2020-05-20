package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.apache.logging.log4j.util.TriConsumer;

public class SmokeyQuartzEvents {
	public static TriConsumer<LivingEntity, LivingEntity, Float> hitEvent = new TriConsumer<LivingEntity, LivingEntity, Float>() {
		@Override
		public void accept(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
			livingEntity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int) (60 * aFloat), 1));
		}
	};
}
