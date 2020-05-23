package tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.util.TriConsumer;

import static vazkii.botania.api.mana.ManaItemHandler.instance;

public class ManaQuartzEvents {
	public static TriConsumer consumer = (TriConsumer<LivingEntity, LivingEntity, Float>) ManaQuartzEvents::hitEntity;
	
	public static void hitEntity(Object attacked, LivingEntity attacker, Float percent) {
		if (attacker instanceof PlayerEntity) {
			instance().dispatchManaExact(attacker.getHeldItem(Hand.MAIN_HAND), (PlayerEntity) attacker, (int) (10 * percent), true);
		}
	}
}
