package com.tfc.dynamicweaponry.material_effects.effects;

import net.minecraft.util.ResourceLocation;

public class ToolEffectRegistryBuilder extends net.minecraftforge.registries.RegistryBuilder<ToolEffect> {
	public ToolEffectRegistryBuilder() {
		this.onAdd((owner, stage, id, obj, oldObj) -> {
			ToolEffect.effects.put(obj.getRegistryName(), obj);
		});
		this.onClear((owner, stage) -> {
			ToolEffect.effects.clear();
		});
		this.onCreate((owner, state) -> {
		});
		this.onBake((owner, stage) -> {
		});
		this.onValidate((owner, stage, id, key, obj) -> {
		});
		this.setType(ToolEffect.class);
		this.allowModification();
		this.tagFolder("tool_effects");
		this.setDefaultKey(new ResourceLocation("unknown:null"));
		this.missing((name, isNetwork) -> null);
		this.setName(new ResourceLocation("dynamic_weaponry:tool_effects"));
		this.disableSync();
	}
}
