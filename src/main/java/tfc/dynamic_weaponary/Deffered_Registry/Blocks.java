package tfc.dynamic_weaponary.Deffered_Registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.block.ShadingTable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Blocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, DynamicWeapons.ModID);
	public static final RegistryObject<Block> SHADING_TABLE = registerNormalBlock("shading_table", () -> new ShadingTable());
	
	//From deepwaters
	private static <T extends Block> RegistryObject<T> baseRegister(String name, Supplier<? extends T> block, Function<RegistryObject<T>, Supplier<? extends Item>> item) {
		RegistryObject<T> register = BLOCKS.register(name, block);
		Items.ITEMS.register(name, item.apply(register));
		return register;
	}
	
	private static <T extends Block> RegistryObject<T> registerNormalBlock(String name, Supplier<? extends Block> block) {
		
		RegistryObject<T> registryObject = (RegistryObject<T>) baseRegister(name, block,
				Blocks::registerBlockItem);
		return registryObject;
	}
	
	private static <T extends Block> Supplier<BlockItem> registerBlockItem(final RegistryObject<T> block) {
		return () -> new BlockItem(Objects.requireNonNull(block.get()),
				new Item.Properties().group(ItemGroup.DECORATIONS));
	}
}
