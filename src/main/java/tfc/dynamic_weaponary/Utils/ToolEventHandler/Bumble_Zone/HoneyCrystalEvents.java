package tfc.dynamic_weaponary.Utils.ToolEventHandler.Bumble_Zone;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.apache.logging.log4j.util.TriConsumer;

public class HoneyCrystalEvents {
	public static TriConsumer consumer = (TriConsumer<LivingEntity, LivingEntity, Float>) HoneyCrystalEvents::hitEntity;
	
	public static void hitEntity(LivingEntity attacked, LivingEntity attacker, Float percent) {
		attacked.addPotionEffect(new EffectInstance(Effects.SLOWNESS, (int) (120 * percent), 1));
	}
}
