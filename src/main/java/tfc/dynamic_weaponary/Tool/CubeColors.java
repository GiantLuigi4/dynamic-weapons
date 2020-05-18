package tfc.dynamic_weaponary.Tool;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CubeColors implements IItemColor {
	static int color = 0;
	
	public CubeColors() {
	}
	
	@Override
	public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
		return color;
	}
}
