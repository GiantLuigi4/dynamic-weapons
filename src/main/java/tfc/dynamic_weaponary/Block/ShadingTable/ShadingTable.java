package tfc.dynamic_weaponary.Block.ShadingTable;

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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import tfc.dynamic_weaponary.Deffered_Registry.TileEntities;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;

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
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile);
			return ActionResultType.SUCCESS;
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
	
	@Override
	public boolean hasTileEntity() {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ShadingTE();
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ShadingTE();
	}
	
	public static class ShadingTE extends TileEntity implements INamedContainerProvider {
		public boolean hasItem = false;
		public PixelStorage image = new PixelStorage(16, 16);
		
		@Override
		public ITextComponent getDisplayName() {
			return new StringTextComponent("none");
		}
		
		@Nullable
		@Override
		public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
			STContainer container = new STContainer(id, playerInv, this);
			container.setTile(this);
			return container;
		}
		
		public ShadingTE() {
			super(TileEntities.SHADING_TABLE.get());
		}
		
		@Override
		public TileEntity getTileEntity() {
			return new ShadingTE();
		}
		
		@Override
		public CompoundNBT write(CompoundNBT compound) {
			compound.putBoolean("HasOrb", hasItem);
			compound.putString("Image", image.toString());
			return super.write(compound);
		}
		
		@Override
		public void read(CompoundNBT compound) {
			super.read(compound);
			hasItem = compound.getBoolean("HasOrb");
			image = PixelStorage.fromString(compound.getString("Image"));
		}
		
		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			read(nbt);
		}
		
		@Override
		public CompoundNBT serializeNBT() {
			return write(new CompoundNBT());
		}
		
		@Override
		public CompoundNBT getUpdateTag() {
			return write(new CompoundNBT());
		}
	}
}
