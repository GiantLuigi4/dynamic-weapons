package tfc.dynamicweaponry.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.dynamicweaponry.FirstPersonItemRendererMixinCode;
import tfc.dynamicweaponry.item.tool.DynamicTool;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	protected ItemStack activeItemStack;
	@Shadow
	protected int activeItemStackUseCount;
	
	@Shadow
	public abstract ItemStack getHeldItem(Hand hand);
	
	@Inject(at = @At("HEAD"), method = "getItemInUseCount()I", cancellable = true)
	public void getUseTime(CallbackInfoReturnable<Integer> cir) {
		if (((Entity) (Object) this) instanceof PlayerEntity) {
			ItemStack stack = activeItemStack;
			if (stack.getItem() instanceof DynamicTool) {
				cir.setReturnValue(FirstPersonItemRendererMixinCode.getUseTime(stack, activeItemStackUseCount));
			}
		}
	}
}
