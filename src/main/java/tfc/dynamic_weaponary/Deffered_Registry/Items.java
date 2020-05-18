package tfc.dynamic_weaponary.Deffered_Registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.ShaderOrb.Properties;
import tfc.dynamic_weaponary.ShaderOrb.ShaderItem;
import tfc.dynamic_weaponary.Tool.IItemTeir;
import tfc.dynamic_weaponary.Tool.ModularItem;

public class Items {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, DynamicWeapons.ModID);
	public static final RegistryObject<Item> SHADER_ORB = ITEMS.register("shader_orb", () -> new ShaderItem(new Properties()));
	public static final RegistryObject<Item> TOOL = ITEMS.register("tool", () -> new ModularItem(1, 1, new IItemTeir(), null, new tfc.dynamic_weaponary.Tool.Properties()));
	public static final RegistryObject<Item> CUBE = ITEMS.register("cube", () -> new Item(new Properties()));
}
