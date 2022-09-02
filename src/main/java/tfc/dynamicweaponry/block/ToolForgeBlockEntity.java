package tfc.dynamicweaponry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfc.dynamicweaponry.Register;
import tfc.dynamicweaponry.Temp;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.tool.Tool;

public class ToolForgeBlockEntity extends BlockEntity {
	public ToolForgeBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(Register.TOOL_FORGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
	}
	
	protected Tool heldTool;
	
	public Tool getHeldTool() {
		return heldTool;
	}
	
	CompoundTag tag;
	
	public Tool getTool(Level level) {
		if (tag != null) {
			heldTool = Tool.fromTag(((IHoldADataLoader) level).myLoader(), tag);
			tag = null;
		} else if (heldTool == null)
			heldTool = new Tool(Temp.layers, ((IHoldADataLoader) level).myLoader());
		return heldTool;
	}
	
	// TODO:
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		this.tag = nbt;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = super.serializeNBT();
		if (this.tag != null) tag.put("tool", this.tag);
		else tag.put("tool", heldTool.toTag());
		return tag;
	}
}
