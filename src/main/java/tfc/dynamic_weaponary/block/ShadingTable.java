package tfc.dynamic_weaponary.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.block.GUI.Shading.STContainer;

import javax.annotation.Nullable;

public class ShadingTable extends Block implements ITileEntityProvider {
	public ShadingTable(Properties builder) {
		super(builder);
	}
	
	public ShadingTable() {
		super(Properties.create(Material.ROCK)
				.hardnessAndResistance(3.0F, 40.0F)
				.sound(SoundType.STONE)
				.harvestLevel(0)
				.harvestTool(ToolType.PICKAXE)
		);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCollisionShape(state, worldIn, pos, context);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getRenderShape(state, worldIn, pos);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ShadingTE) {
				player.openContainer(new STContainer.Provider((ShadingTE) tile));
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		VoxelShape shape = VoxelShapes.or(
				Block.makeCuboidShape(0, 0, 0, 4, 16, 4),
				Block.makeCuboidShape(0, 0, 12, 4, 16, 16)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(0, 12, 0, 16, 16, 16)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(12, 0, 0, 16, 16, 4)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(12, 0, 12, 16, 16, 16)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(4, 15, 4, 12, 17, 12)
		);
		return shape;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ShadingTE();
	}
	
	public static class ShadingTE extends TileEntity {
		public boolean hasItem = false;
		
		public ShadingTE() {
			super(TileEntities.SHADING_TABLE.get());
		}
		
		@Override
		public TileEntity getTileEntity() {
			return new ShadingTE();
		}
		
		@Override
		public CompoundNBT write(CompoundNBT compound) {
			hasItem = compound.getBoolean("HasOrb");
			return compound;
		}
		
		@Override
		public void read(CompoundNBT compound) {
			super.read(compound);
			compound.putBoolean("HasOrb", hasItem);
		}
	}
}
