package tfc.dynamicweaponry.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.access.IMayHoldATool;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IMayHoldATool {
	@Inject(at = @At("RETURN"), method = "copy")
	public void postCopy(CallbackInfoReturnable<ItemStack> cir) {
		ItemStack out = cir.getReturnValue();
		if (out != ItemStack.EMPTY) {
			((IMayHoldATool) (Object) out).setTool(myTool());
		}
	}
	
	@Unique
	Tool heldTool;
	
	@Override
	public Tool myTool() {
		return heldTool;
	}
	
	@Override
	public void setTool(Tool tool) {
		heldTool = tool;
	}
}
