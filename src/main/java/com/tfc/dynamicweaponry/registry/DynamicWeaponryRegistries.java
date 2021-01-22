package com.tfc.dynamicweaponry.registry;

import com.tfc.dynamicweaponry.material_effects.condtions.AndCondition;
import com.tfc.dynamicweaponry.material_effects.condtions.DistributionCondition;
import com.tfc.dynamicweaponry.material_effects.condtions.EffectCondition;
import com.tfc.dynamicweaponry.material_effects.condtions.ModulusTickCondition;
import com.tfc.dynamicweaponry.material_effects.effects.SelfRepairEffect;
import com.tfc.dynamicweaponry.material_effects.effects.ToolEffect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class DynamicWeaponryRegistries {
	public static final DeferredRegister<ToolEffect> EFFECTS = DeferredRegister.create(ToolEffect.class, "dynamic_weaponry");
	public static final DeferredRegister<EffectCondition> CONDITIONS = DeferredRegister.create(EffectCondition.class, "dynamic_weaponry");
	
	public static final RegistryObject<ToolEffect> SELF_REPAIR = EFFECTS.register("self_heal", SelfRepairEffect::new);
	
	public static final RegistryObject<EffectCondition> MODULUS_TICK = CONDITIONS.register("tick_mod", ModulusTickCondition::new);
	public static final RegistryObject<EffectCondition> AND = CONDITIONS.register("and", AndCondition::new);
	public static final RegistryObject<EffectCondition> DISTRO = CONDITIONS.register("dist", DistributionCondition::new);
}
