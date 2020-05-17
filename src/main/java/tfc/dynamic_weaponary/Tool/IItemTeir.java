package tfc.dynamic_weaponary.Tool;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class IItemTeir implements IItemTier {
	@Override
	public int getMaxUses() {
		return 2000;
	}
	
	@Override
	public float getEfficiency() {
		return 200;
	}
	
	@Override
	public float getAttackDamage() {
		return 2;
	}
	
	@Override
	public int getHarvestLevel() {
		return 200;
	}
	
	@Override
	public int getEnchantability() {
		return 0;
	}
	
	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.EMPTY;
	}
}
