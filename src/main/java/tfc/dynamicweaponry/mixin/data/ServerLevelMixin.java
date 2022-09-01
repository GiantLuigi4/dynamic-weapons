package tfc.dynamicweaponry.mixin.data;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.access.IHoldADataLoader;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Inject(at = @At("TAIL"), method = "<init>")
	public void postInit(MinecraftServer p_203762_, Executor p_203763_, LevelStorageSource.LevelStorageAccess p_203764_, ServerLevelData p_203765_, ResourceKey p_203766_, Holder p_203767_, ChunkProgressListener p_203768_, ChunkGenerator p_203769_, boolean p_203770_, long p_203771_, List p_203772_, boolean p_203773_, CallbackInfo ci) {
		// TODO: swap this for a server asset loader
		((IHoldADataLoader) this).setLoader(DynamicWeaponry.clientAssetLoader);
	}
}
