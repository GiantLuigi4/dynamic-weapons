package com.tfc.dynamicweaponry.material_effects.condtions;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;

public class ConditionRegistryBuilder extends RegistryBuilder<EffectCondition> {
	public ConditionRegistryBuilder() {
		this.onAdd((owner, stage, id, obj, oldObj) -> {
			EffectCondition.conditions.put(obj.getRegistryName(), obj);
		});
		this.onClear((owner, stage) -> {
			EffectCondition.conditions.clear();
		});
		this.onCreate((owner, state) -> {
		});
		this.onBake((owner, stage) -> {
		});
		this.onValidate((owner, stage, id, key, obj) -> {
		});
		this.setType(EffectCondition.class);
		this.allowModification();
		this.tagFolder("effect_conditions");
		this.setDefaultKey(new ResourceLocation("unknown:null"));
		this.missing((name, isNetwork) -> null);
		this.setName(new ResourceLocation("dynamic_weaponry:effect_conditions"));
		this.disableSync();
	}
}
