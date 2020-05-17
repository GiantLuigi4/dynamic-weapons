package tfc.dynamic_weaponary.ShaderOrb;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Properties extends Item.Properties {
	public Properties() {
		this.maxStackSize(1).group(ItemGroup.MISC);
	}
}
