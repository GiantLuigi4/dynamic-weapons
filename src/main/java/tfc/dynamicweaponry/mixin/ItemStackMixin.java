package tfc.dynamicweaponry.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.access.IMayHoldATool;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IMayHoldATool {
	@Inject(at = @At("RETURN"), method = "copy")
	public void postCopy(CallbackInfoReturnable<ItemStack> cir) {
		ItemStack out = cir.getReturnValue();
		if (out != ItemStack.EMPTY) {
			Tool tl = myTool();
			((IMayHoldATool) (Object) out).setTool(tl);
		}
	}
	
	@Unique
	AtomicReference<Tool> heldTool = new AtomicReference<>(null);
	
	@Override
	public Tool myTool() {
		return heldTool.get();
	}
	
	@Override
	public void setTool(Tool tool) {
		heldTool.set(tool);
	}
}
