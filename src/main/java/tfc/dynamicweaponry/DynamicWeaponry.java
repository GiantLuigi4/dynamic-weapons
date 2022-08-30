package tfc.dynamicweaponry;

import net.minecraftforge.fml.common.Mod;
import tfc.dynamicweaponry.item.Register;

@Mod("dynamic_weaponry")
public class DynamicWeaponry {
	public DynamicWeaponry() {
		Register.init();
	}
}
