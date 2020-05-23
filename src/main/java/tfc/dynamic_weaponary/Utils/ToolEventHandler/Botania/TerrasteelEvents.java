package tfc.dynamic_weaponary.Utils.ToolEventHandler.Botania;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

import static vazkii.botania.api.mana.ManaItemHandler.instance;

public class TerrasteelEvents {
	public static TriConsumer consumer = (TriConsumer<ItemStack, LivingEntity, Float>) TerrasteelEvents::invEvent;
	
	public static void invEvent(ItemStack stack, LivingEntity playerEntity, Float percent) {
		try {
			if (!playerEntity.getEntityWorld().isRemote) {
				if (stack.getDamage() != 0) {
					if (instance().requestManaExactForTool(stack, (PlayerEntity) playerEntity, (int) (100 * ((1 - percent) + 1)), true)) {
						stack.setDamage(stack.getDamage() - 1);
					}
				}
			}
		} catch (Exception err) {
		}
	}
}
