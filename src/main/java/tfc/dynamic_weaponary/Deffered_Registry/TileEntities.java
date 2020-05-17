package tfc.dynamic_weaponary.Deffered_Registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.block.ShadingTable;

public class TileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, DynamicWeapons.ModID);
	
	public static final RegistryObject<TileEntityType<?>> SHADING_TABLE = TILE_ENTITIES.register("shading_table", () -> TileEntityType.Builder.create(ShadingTable.ShadingTE::new, Blocks.SHADING_TABLE.get()).build(null));
}
