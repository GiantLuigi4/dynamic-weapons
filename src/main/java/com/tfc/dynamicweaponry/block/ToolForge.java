package com.tfc.dynamicweaponry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class ToolForge extends Block {
	public ToolForge(Properties properties) {
		super(properties);
	}
	
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
		return true;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof ToolForgeTileEntity) {
				ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
				ToolForgeContainer container = tileEntity.container;
				player.openContainer = container;
				container.open(player);
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof ToolForgeTileEntity) {
			ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
			if (!tileEntity.tool.equals(tileEntity.container.tool)) {
				tileEntity.tool = tileEntity.container.tool.copy();
			}
		}
	}
}
