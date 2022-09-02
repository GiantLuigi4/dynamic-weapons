package tfc.dynamicweaponry;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tfc.dynamicweaponry.block.ToolForgeBlock;
import tfc.dynamicweaponry.block.ToolForgeBlockEntity;
import tfc.dynamicweaponry.item.DynamicTool;
import tfc.dynamicweaponry.screens.tool.ToolForgeContainer;

public class Register {
	protected static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ModLoadingContext.get().getActiveNamespace());
	protected static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ModLoadingContext.get().getActiveNamespace());
	protected static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModLoadingContext.get().getActiveNamespace());
	protected static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModLoadingContext.get().getActiveNamespace());
	
	public static final RegistryObject<Item> TOOL_ITEM = Register.ITEM_REGISTER.register("dynamic_tool", () -> new DynamicTool(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Block> TOOL_FORGE_BLOCK = Register.BLOCK_REGISTER.register("tool_forge", () -> new ToolForgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)));
	public static final RegistryObject<BlockEntityType<ToolForgeBlockEntity>> TOOL_FORGE_BLOCK_ENTITY = Register.BLOCK_ENTITY_REGISTER.register("tool_forge", () -> BlockEntityType.Builder.of(ToolForgeBlockEntity::new, TOOL_FORGE_BLOCK.get()).build(null));
	public static final RegistryObject<MenuType<ToolForgeContainer>> TOOL_FORGE_CONTAINER = Register.MENU_TYPE_REGISTER.register("tool_forge", () -> ToolForgeContainer.MENU_TYPE);
	
	static {
		ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCK_ENTITY_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		MENU_TYPE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static void init() {
	}
}
