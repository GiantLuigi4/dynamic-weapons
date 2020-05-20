package tfc.dynamic_weaponary.Utils.ToolEventHandler;

import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.util.TriConsumer;

public class TerrasteelEvents {
	public static TriConsumer<LivingEntity, LivingEntity, Float> invEvent = new TriConsumer<LivingEntity, LivingEntity, Float>() {
		@Override
		public void accept(LivingEntity livingEntity, LivingEntity playerEntity, Float aFloat) {
			//vazkii.botania.api.mana.ManaItemHandler.instance().requestManaExactForTool(stack, (PlayerEntity) player, getManaPerDamage() * 2, true))
		}
	};
}
