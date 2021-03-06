package tfc.dynamicweaponry.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamicweaponry.block.ToolForge;
import tfc.dynamicweaponry.block.ToolForgeContainer;
import tfc.dynamicweaponry.block.ToolForgeTileEntity;
import tfc.dynamicweaponry.item.forge.ToolForgeItem;
import tfc.dynamicweaponry.item.tool.DynamicTool;

public class Registry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "dynamic_weaponry");
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "dynamic_weaponry");
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "dynamic_weaponry");
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "dynamic_weaponry");
	
	public static final RegistryObject<Item> DYNAMIC_TOOL = ITEMS.register("dynamic_tool", DynamicTool::new);
	
	public static final RegistryObject<Block> TOOL_FORGE = BLOCKS.register("tool_forge", () -> new ToolForge(AbstractBlock.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Item> TOOL_FORGE_ITEM = ITEMS.register("tool_forge", ToolForgeItem::new);
	public static final RegistryObject<TileEntityType<ToolForgeTileEntity>> TOOL_FORGE_TE = TILE_ENTITIES.register("tool_forge", () -> TileEntityType.Builder.create(ToolForgeTileEntity::new, TOOL_FORGE.get()).build(null));
	public static final RegistryObject<ContainerType<?>> TOOL_FORGE_CONTAINER = CONTAINERS.register("tool_forge", () -> new ContainerType<>(ToolForgeContainer::new));
}
