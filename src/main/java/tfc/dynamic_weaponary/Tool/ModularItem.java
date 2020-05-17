package tfc.dynamic_weaponary.Tool;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import tfc.dynamic_weaponary.Utils.DrawingUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ModularItem extends ToolItem {
	private Set<Block> effectiveBlocks;
	
	public ModularItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
	}
	
	public float getEfficiency(ItemStack stack) {
		return 5;
	}
	
	public ToolShape getShape(ItemStack stack) {
		return null;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return efficiency;
		return this.effectiveBlocks.contains(state.getBlock()) ? this.efficiency : 1.0F;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(2, attacker, (p_220039_0_) -> {
			p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
		});
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
			stack.damageItem(1, entityLiving, (p_220038_0_) -> {
				p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});
		}
		return true;
	}
	
	@Override
	public IItemTier getTier() {
		return super.getTier();
	}
	
	@Override
	public int getItemEnchantability() {
		return super.getItemEnchantability();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}
		return multimap;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		if (stack.getDamage() == stack.getMaxDamage()) {
			return new DrawingUtils.ColorHelper(255, 0, 0).getRGB();
		}
		return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getDamage() == stack.getMaxDamage()) {
			return 1;
		}
		return (double) stack.getDamage() / (double) stack.getMaxDamage();
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		try {
			tooltip.add(new StringTextComponent("§aShield:" + stack.getTag().getBoolean("shield")));
		} catch (Exception err) {
			tooltip.add(new StringTextComponent("§aShield:" + "false"));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
		try {
			return stack.getTag().getBoolean("shield");
		} catch (Exception err) {
			return false;
		}
	}
}
