package tfc.dynamicweaponry.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Register {
	protected static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ModLoadingContext.get().getActiveNamespace());
	
	public static final RegistryObject<Item> TOOL_ITEM = Register.ITEM_REGISTER.register("dynamic_tool", () -> new DynamicTool(new Item.Properties().stacksTo(1)));
	
	static {
		ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static void init() {
	}
}
