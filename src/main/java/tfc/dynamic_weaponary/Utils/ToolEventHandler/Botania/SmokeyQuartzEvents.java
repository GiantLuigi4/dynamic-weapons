package tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.apache.logging.log4j.util.TriConsumer;

public class SmokeyQuartzEvents {
	public static TriConsumer consumer = (TriConsumer<LivingEntity, LivingEntity, Float>) SmokeyQuartzEvents::hitEntity;
	
	public static void hitEntity(LivingEntity attacked, LivingEntity attacker, Float percent) {
		attacked.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int) (160 * percent), 1));
	}
}
