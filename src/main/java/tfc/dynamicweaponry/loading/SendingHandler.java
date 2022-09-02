package tfc.dynamicweaponry.loading;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.PacketDistributor;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.network.DynamicWeaponryNetworkRegistry;
import tfc.dynamicweaponry.network.sync.SendMaterialsPacket;

public class SendingHandler {
	public static void onTick(TickEvent.ServerTickEvent event) {
		if (DynamicWeaponry.serverDataLoader.needsSending) {
			if (event.side == LogicalSide.SERVER) {
				DynamicWeaponryNetworkRegistry.NETWORK_INSTANCE.send(
						PacketDistributor.ALL.noArg(),
						new SendMaterialsPacket(DynamicWeaponry.serverDataLoader.getMaterialHolder())
				);
				DynamicWeaponry.serverDataLoader.needsSending = false;
			}
		}
	}
	
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer sp) {
			DynamicWeaponryNetworkRegistry.NETWORK_INSTANCE.send(
					PacketDistributor.PLAYER.with(() -> sp),
					new SendMaterialsPacket(DynamicWeaponry.serverDataLoader.getMaterialHolder())
			);
		}
	}
}
