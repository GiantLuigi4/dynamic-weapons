package tfc.dynamic_weaponary.Utils.ToolEventHandler.PSI;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageDeductPsi;

public class PSIMetalEvents {
	public static TriConsumer consumer = (TriConsumer<ItemStack, LivingEntity, Float>) PSIMetalEvents::invEvent;
	
	public static void invEvent(ItemStack stack, LivingEntity playerEntity, Float percent) {
		ItemStack stack2 = PsiAPI.getPlayerCAD((PlayerEntity) playerEntity);
		if (stack2 != null) {
			ICAD cad = ((ICAD) stack2.getItem());
//			cad.incrementTime(stack2,(int)(200*((1-percent)+1)));
			if (playerEntity instanceof PlayerEntity && stack.getDamage() > 0) {
				PlayerEntity player = (PlayerEntity) playerEntity;
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				int regenTime = stack.getOrCreateTag().getInt("regenTime");
				if (deductPsi((int) (200 * (((1 - percent) + 1))), 0, true, false, data)) {
					stack.setDamage(stack.getDamage() - 1);
				}
				
				stack.getOrCreateTag().putInt("regenTime", regenTime + 1);
			}
		}
	}
	
	public static boolean deductPsi(int psi, int cd, boolean sync, boolean shatter, PlayerDataHandler.PlayerData data) {
		int currentPsi = data.availablePsi;
		PlayerEntity player = (PlayerEntity) data.playerWR.get();
		if (player != null) {
			ItemStack cadStack = data.getCAD();
			if (!cadStack.isEmpty()) {
				ICAD cad = (ICAD) cadStack.getItem();
				int storedPsi = cad.getStoredPsi(cadStack);
				if (storedPsi == -1) {
					return false;
				}
			}
			
			if (data.isOverflowed()) {
				return false;
			}
			
			if (data.availablePsi < 0) {
				return false;
			}
			
			data.availablePsi -= psi;
			if (data.regenCooldown < cd) {
				data.regenCooldown = cd;
			}
			
			if (data.availablePsi < 0) {
				int overflow = -data.availablePsi;
				data.availablePsi = 0;
				if (!cadStack.isEmpty()) {
					ICAD cad = (ICAD) cadStack.getItem();
					overflow = cad.consumePsi(cadStack, overflow);
				}
				
				if (!shatter && overflow > 0) {
					float dmg = (float) overflow / (float) (data.loopcasting ? 50 : 125);
					try {
						if (!data.playerWR.get().world.isRemote) {
							player.attackEntityFrom(PlayerDataHandler.damageSourceOverload, dmg);
						}
					} catch (Exception err) {
					}
					
					data.overflowed = true;
				}
			}
			
			if (sync && player instanceof ServerPlayerEntity) {
				MessageDeductPsi message = new MessageDeductPsi(currentPsi, data.availablePsi, data.regenCooldown, shatter);
				MessageRegister.sendToPlayer(message, player);
			}
			
			data.save();
		}
		return true;
	}
}
