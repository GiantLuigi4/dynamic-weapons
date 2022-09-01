package tfc.dynamicweaponry.mixin.data;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.access.IHoldADataLoader;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
	@Inject(at = @At("TAIL"), method = "<init>")
	public void postInit(ClientPacketListener p_205505_, ClientLevel.ClientLevelData p_205506_, ResourceKey p_205507_, Holder p_205508_, int p_205509_, int p_205510_, Supplier p_205511_, LevelRenderer p_205512_, boolean p_205513_, long p_205514_, CallbackInfo ci) {
		((IHoldADataLoader) this).setLoader(DynamicWeaponry.clientAssetLoader);
	}
}
