package com.tfc.dynamicweaponry.block;

import com.tfc.dynamicweaponry.data.DataLoader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.item.tool.MaterialPoint;
import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.item.tool.ToolComponent;
import com.tfc.dynamicweaponry.registry.Registry;
import com.tfc.dynamicweaponry.utils.Point;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

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
					
					Tool tool = new Tool(stack1);
					
					if (!player.isCreative()) {
						for (ToolComponent component : tool.components) {
							if (component.points.isEmpty()) {
								continue;
							}
							
							if (component.type == null) {
								player.sendStatusMessage(new StringTextComponent("Invalid part type found :thonk4:"), true);
								return ActionResultType.FAIL;
							}
							
							if (!tool.isPartCompatible(component.type.name)) {
								player.sendStatusMessage(new StringTextComponent("Part " + component.type.name + " is incompatible with one or more of the other part types on the tool"), true);
								return ActionResultType.FAIL;
							}
							
							if (!tool.areDepsFilled(component.type.name)) {
								player.sendStatusMessage(new StringTextComponent("Part " + component.type.name + " is missing one or more dependencies"), true);
								return ActionResultType.FAIL;
							}
							
							for (MaterialPoint point : component.points.toArray(new MaterialPoint[0])) {
								if (
										point.x < component.type.min.x ||
												point.y < 0 ||
												point.x > component.type.max.x ||
												point.y > 15
								) {
									component.setPoint(point, null);
								}
							}
							
							for (Point requiredPoint : component.type.getRequiredPoints()) {
								if (!component.checkPoint(requiredPoint)) {
									player.sendStatusMessage(new StringTextComponent("Not all required points are met"), true);
									return ActionResultType.FAIL;
								}
							}
						}
					}
					
					if (!player.isCreative()) {
						ArrayList<Material> materials = new ArrayList<>();
						HashMap<Material, Float> costs = new HashMap<>();
						
						for (ToolComponent component : tool.components) {
							for (MaterialPoint point : component.points) {
								Material material = DataLoader.INSTANCE.getMaterial(point.material);
								
								if (material != null) {
									if (!materials.contains(material)) {
										materials.add(material);
										float amt = tool.getMaterialCost(material);
										int count = player.inventory.count(ForgeRegistries.ITEMS.getValue(material.item));
										
										if (count < Math.ceil(amt)) {
											player.sendStatusMessage(new StringTextComponent("Need " + (int) ((amt - Math.ceil(count)) + 1) + " more " + material.item), true);
											return ActionResultType.FAIL;
										}
										
										costs.put(material, amt);
									}
								}
							}
						}
						
						if (!worldIn.isRemote) {
							for (Material material1 : materials) {
								int amtRemoved = 0;
								int cost = (int) Math.ceil(costs.get(material1));
								
								for (int index = 0; index < player.inventory.getSizeInventory(); index++) {
									ItemStack stack2 = player.inventory.getStackInSlot(index);
									
									if (stack2.getItem().getRegistryName().equals(material1.item)) {
										int consumed = Math.min(cost, stack2.getCount());
										player.inventory.decrStackSize(index, consumed);
										amtRemoved -= consumed;
										
										if (consumed >= amtRemoved) break;
									}
								}
							}
						}
					}
					
					nbt1.put("tool_info", tool.serialize());
					
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
	
	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}
	
	/**
	 * Called when A user uses the creative pick block button on this block
	 *
	 * @param state
	 * @param target The full target the player is looking at
	 * @param world
	 * @param pos
	 * @param player
	 * @return A ItemStack to add to the player's inventory, empty itemstack if nothing should be added.
	 */
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(Registry.TOOL_FORGE_ITEM.get());
	}
	
	//	@Override
//	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
//		super.tick(state, worldIn, pos, rand);
//		if (worldIn.isRemote) return;
//
//		TileEntity te = worldIn.getTileEntity(pos);
//
//		if (te instanceof ToolForgeTileEntity) {
//			ToolForgeTileEntity tileEntity = (ToolForgeTileEntity) te;
//			tileEntity.container.world = worldIn;
//			tileEntity.container.pos = pos;
//
//			if (!tileEntity.tool.equals(tileEntity.container.tool)) {
//				tileEntity.container.resync();
////				tileEntity.tool = tileEntity.container.tool.copy();
////				tileEntity.markDirty();
////				tileEntity.getWorld().notifyBlockUpdate(te.getPos(), te.getBlockState(), te.getBlockState(), 3);
//
////				for (ServerPlayerEntity player : worldIn.getPlayers()) {
////					if (player.getPosition().distanceSq(pos) <= 2048) {
////						IPacket updatePacket = tileEntity.getUpdatePacket();
////						player.connection.sendPacket(updatePacket);
////					}
////				}
//			}
//		}
//
//		worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 10);
//	}
	
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
