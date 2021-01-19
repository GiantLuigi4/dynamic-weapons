package com.tfc.dynamicweaponry;

import com.tfc.dynamicweaponry.item.tool.Tool;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class FirstPersonItemRendererMixinCode {
	public static int getUseTime(ItemStack stack, int activeItemStackUseCount) {
		float speed = new Tool(stack).getDrawSpeed();
		float src = 1 * (stack.getOrCreateTag().getFloat("pull_time"));
		float targ = 1 * Math.min(1, stack.getOrCreateTag().getFloat("pull_time") + (speed / 2f));
		float time = (
				1 * MathHelper.lerp(
						Minecraft.getInstance().getRenderPartialTicks(),
						src, targ
				)
		) * 28;
		if (time >= 28) {
			return activeItemStackUseCount - 10000;
		}
		return (int) (
				stack.getUseDuration() - time
		);
	}
}
