package com.tfc.dynamicweaponry.block;

import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class ToolForge extends Block implements ITileEntityProvider {
	public ToolForge(Properties properties) {
		super(properties);
	}
	
	private static final VoxelShape shape = VoxelShapes.combine(
			VoxelShapes.or(
					Block.makeCuboidShape(0, 0, 0, 4, 16, 4),
					Block.makeCuboidShape(0, 0, 12, 4, 16, 16),
					Block.makeCuboidShape(0, 11, 0, 16, 16, 16),
					Block.makeCuboidShape(12, 0, 0, 16, 16, 4),
					Block.makeCuboidShape(12, 0, 12, 16, 16, 16)
			),
			Block.makeCuboidShape(5, 15, 5, 11, 17, 11),
			IBooleanFunction.ONLY_FIRST
	);
	
	/**
	 * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
	 * Return the same thing you would from that function.
	 * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
	 *
	 * @param state The state of the current block
	 * @param world The world to create the TE in
	 * @return A instance of a class extending TileEntity
	 */
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ToolForgeTileEntity();
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ToolForgeTileEntity();
	}
	
	/**
	 * Called throughout the code as a replacement for block instanceof BlockContainer
	 * Moving this to the Block base class allows for mods that wish to extend vanilla
	 * blocks, and also want to have a tile entity on that block, may.
	 * <p>
	 * Return true from this function to specify this block has a tile entity.
	 *
	 * @param state State of the current block
	 * @return True if block has a tile entity, false otherwise
	 */
	@Override
	public boolean hasTileEntity(BlockState state) {
		return super.hasTileEntity(state);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof ToolForgeTileEntity) {
			ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
			
			if (player.isSneaking()) {
				ItemStack stack = player.getHeldItem(handIn);
				if (stack.isEmpty()) {
					CompoundNBT nbt = tileEntity.serializeNBT();
					ItemStack stack1 = new ItemStack(Registry.DYNAMIC_TOOL.get());
					CompoundNBT nbt1 = stack1.getOrCreateTag();
					nbt1.put("tool_info", nbt.getCompound("tool"));
					player.setHeldItem(handIn, stack1);
				}
			} else {
				if (!worldIn.isRemote) {
					ToolForgeContainer container = tileEntity.container;
					tileEntity.container.world = worldIn;
					tileEntity.container.pos = pos;
					player.openContainer = container;
					container.open(player);
				}
			}
		}
		
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (worldIn.isRemote) return;
		
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof ToolForgeTileEntity) {
			ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
			tileEntity.container.world = worldIn;
			tileEntity.container.pos = pos;
			
			if (!tileEntity.tool.equals(tileEntity.container.tool)) {
				tileEntity.container.resync();
//				tileEntity.tool = tileEntity.container.tool.copy();
//				tileEntity.markDirty();
//				tileEntity.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);

//				for (ServerPlayerEntity player : worldIn.getPlayers()) {
//					if (player.getPosition().distanceSq(pos) <= 2048) {
//						IPacket updatePacket = tileEntity.getUpdatePacket();
//						player.connection.sendPacket(updatePacket);
//					}
//				}
			}
		}
		
		worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 10);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 10);
	}

//	@Override
//	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//		return shape;
//	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

//	@Override
//	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//		return shape;
//	}
}