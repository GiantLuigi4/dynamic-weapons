package tfc.dynamic_weaponary.Deffered_Registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.ShaderOrb.Properties;
import tfc.dynamic_weaponary.ShaderOrb.ShaderItem;

public class Items {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, DynamicWeapons.ModID);
	public static final RegistryObject<Item> SHADER_ORB = ITEMS.register("shader_orb", () -> new ShaderItem(new Properties()));
}
