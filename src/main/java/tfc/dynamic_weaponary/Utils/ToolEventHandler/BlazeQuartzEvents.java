package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;

public class BlazeQuartzEvents {
	public void hitEntity(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
		livingEntity.setFire((int) (120 * aFloat));
	}
}
