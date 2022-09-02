package tfc.dynamicweaponry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tfc.dynamicweaponry.screens.tool.ToolForgeContainer;

public class ToolForgeBlock extends BaseEntityBlock {
	public ToolForgeBlock(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ToolForgeBlockEntity(pPos, pState);
	}
	
	private static final VoxelShape shape;
	
	// don't ask
	static {
		VoxelShape leg;
		VoxelShape shape1 = leg = Shapes.create(0, 0, 0, 4f / 16, 1, 4f / 16);
		shape1 = Shapes.joinUnoptimized(shape1, leg.move(12f / 16, 0, 0), BooleanOp.OR);
		shape1 = Shapes.joinUnoptimized(shape1, leg.move(0, 0, 12f / 16), BooleanOp.OR);
		shape1 = Shapes.joinUnoptimized(shape1, leg.move(12f / 16, 0, 12f / 16), BooleanOp.OR);
		VoxelShape top = Shapes.create(0, 11 / 16f, 0, 1, 1, 1);
		VoxelShape pot = Shapes.create(5f / 16, 15 / 16f, 5f / 16, 11f / 16, 1, 11f / 16);
		top = Shapes.joinUnoptimized(top, pot, BooleanOp.ONLY_FIRST);
		shape = Shapes.joinUnoptimized(shape1, top, BooleanOp.OR);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return shape;
	}
	
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide) {
			MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
			pPlayer.openMenu(menuprovider);
		}
//		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
		return InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
		return new MenuProvider() {
			@Override
			public Component getDisplayName() {
				// TODO: allow named blocks
				return new TranslatableComponent("dynamic_weaponry.tool_forge");
			}
			
			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
				BlockEntity be = pLevel.getBlockEntity(pPos);
				if (be instanceof ToolForgeBlockEntity forge)
					return new ToolForgeContainer(ToolForgeContainer.MENU_TYPE, pContainerId, forge, pInventory);
				return null;
			}
		};
	}
}
