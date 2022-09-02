package tfc.dynamicweaponry.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.loading.JsonAssetLoader;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.loading.MaterialLoader;
import tfc.dynamicweaponry.loading.Materials;
import tfc.dynamicweaponry.network.Packet;
import tfc.dynamicweaponry.tool.Tool;

import java.util.Set;

public class SendMaterialsPacket extends Packet {
	Materials materials;
	
	public SendMaterialsPacket(Materials materials) {
		this.materials = materials;
	}
	
	public SendMaterialsPacket(FriendlyByteBuf buf) {
		super(buf);
		materials = new Materials();
		int count = buf.readIntLE();
		for (int i = 0; i < count; i++) {
			CompoundTag tag = buf.readNbt();
			Material m = Material.fromTag(tag);
			materials.add(m);
		}
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		Set<ResourceLocation> keys = materials.keys();
		buf.writeIntLE(keys.size());
		for (ResourceLocation key : keys) {
			Material m = materials.get(key);
			buf.writeNbt(m.toTag());
		}
	}
	
	@Override
	public void handle(NetworkEvent.Context ctx) {
		if (checkClient(ctx)) {
			MaterialLoader dataLoader = ((IHoldADataLoader) Minecraft.getInstance().level).myLoader();
			if (dataLoader instanceof JsonAssetLoader assetLoader) {
				assetLoader.pair(materials);
			}
			Tool.clearImages();
		}
	}
}
