package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.util.TriConsumer;

public class BlazeQuartzEvents {
	public static TriConsumer<LivingEntity, LivingEntity, Float> hitEvent = new TriConsumer<LivingEntity, LivingEntity, Float>() {
		@Override
		public void accept(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
			livingEntity.setFire((int) (120 * aFloat));
		}
	};
}
