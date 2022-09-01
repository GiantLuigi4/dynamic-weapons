package tfc.dynamicweaponry.mixin.data;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.loading.MaterialLoader;

@Mixin(Level.class)
public class LevelMixin implements IHoldADataLoader {
	@Unique
	MaterialLoader loader = null;
	
	@Override
	public MaterialLoader myLoader() {
		return loader;
	}
	
	@Override
	public void setLoader(MaterialLoader loader) {
		this.loader = loader;
	}
}
