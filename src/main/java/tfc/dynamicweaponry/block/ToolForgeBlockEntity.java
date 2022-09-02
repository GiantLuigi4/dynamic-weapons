package tfc.dynamicweaponry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.tag = nbt.getCompound("tool");
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		if (this.tag != null) pTag.put("tool", this.tag);
		else if (level != null) pTag.put("tool", getTool(level).toTag());
		else if (heldTool != null) pTag.put("tool", heldTool.toTag());
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		heldTool = null;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag);
		return tag;
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		heldTool = null;
	}
}
