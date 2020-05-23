package tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania;

import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.util.TriConsumer;

public class BlazeQuartzEvents {
	public static TriConsumer consumer = (TriConsumer<LivingEntity, LivingEntity, Float>) BlazeQuartzEvents::hitEntity;
	
	public static void hitEntity(LivingEntity attacked, LivingEntity attacker, Float percent) {
		attacked.setFire((int) (120 * percent));
	}
}
