package tfc.dynamic_weaponary.block.ToolForge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.Utils.PixelStorage;
import tfc.dynamic_weaponary.Utils.VectorImage;

import javax.annotation.Nullable;

public class ToolForge extends Block implements ITileEntityProvider {
	public ToolForge() {
		super(Properties.create(Material.ROCK)
				.hardnessAndResistance(3.0F, 40.0F)
				.sound(SoundType.STONE)
				.harvestLevel(0)
				.harvestTool(ToolType.PICKAXE)
		);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ForgeTE();
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ForgeTE();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		if (!world.isRemote) {
			if (!player.isSneaking()) {
				TileEntity tile = world.getTileEntity(pos);
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile);
				return ActionResultType.SUCCESS;
			} else {
				ItemStack renderStack = new ItemStack(Items.TOOL.get());
				try {
					CompoundNBT nbt = renderStack.getOrCreateTag();
					nbt.putString("image", ToolForgeContainer.tile.image.toString());
				} catch (Exception err) {
				}
				player.setHeldItem(hand, renderStack);
			}
		}
		return ActionResultType.SUCCESS;
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
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		VoxelShape shape = VoxelShapes.or(
				Block.makeCuboidShape(0, 0, 0, 4, 16, 4),
				Block.makeCuboidShape(0, 0, 12, 4, 16, 16)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(0, 11, 0, 16, 16, 16)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(12, 0, 0, 16, 16, 4)
		);
		shape = VoxelShapes.or(shape,
				Block.makeCuboidShape(12, 0, 12, 16, 16, 16)
		);
		shape = VoxelShapes.combine(shape,
				Block.makeCuboidShape(5, 15, 5, 11, 17, 11),
				IBooleanFunction.ONLY_FIRST
		);
		return shape;
	}
	
	public static class ForgeTE extends TileEntity implements INamedContainerProvider {
		public PixelStorage image;
		
		public ForgeTE() {
			super(TileEntities.TOOL_FORGE.get());
		}
		
		@Override
		public ITextComponent getDisplayName() {
			return new StringTextComponent("none");
		}
		
		@Nullable
		@Override
		public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
			ToolForgeContainer container = new ToolForgeContainer(id, playerInv);
			container.setTile(this);
			return container;
		}
		
		@Override
		public void read(CompoundNBT compound) {
			super.read(compound);
			try {
				image = VectorImage.fromString(compound.getString("image"));
			} catch (Exception err) {
			}
		}
		
		@Override
		public CompoundNBT write(CompoundNBT compound) {
			try {
				compound.putString("image", image.toString());
			} catch (Exception err) {
			}
			return super.write(compound);
		}
		
		@Nullable
		@Override
		public SUpdateTileEntityPacket getUpdatePacket() {
			return super.getUpdatePacket();
		}
		
		@Override
		public CompoundNBT getUpdateTag() {
			return write(new CompoundNBT());
		}
	}
}
