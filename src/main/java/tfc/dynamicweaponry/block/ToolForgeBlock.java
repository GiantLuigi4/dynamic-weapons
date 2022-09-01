package tfc.dynamicweaponry.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

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
}
