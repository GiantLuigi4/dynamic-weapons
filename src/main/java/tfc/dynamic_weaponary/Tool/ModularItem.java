package tfc.dynamic_weaponary.Tool;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.CalculationRegistry;
import tfc.dynamic_weaponary.EventRegistryRedo;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Optimization.StatList;
import tfc.dynamic_weaponary.Utils.Optimization.ToolStats;
import tfc.dynamic_weaponary.Utils.Tool.ToolLogicHelper;
import tfc.dynamic_weaponary.Utils.Tool.ToolShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ModularItem extends ToolItem {
	private Set<Block> effectiveBlocks;
	
	public ModularItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
	}
	
	double damage = 1;
	
	public static float getEfficiency(ItemStack stack) {
		return 5;
	}
	
	public static int getHarvestLevel(ItemStack stack) {
		return 1;
	}
	
	public static ToolShape getShape(ItemStack stack) {
		return null;
	}
	
	public static boolean canHarvest(BlockState state, ToolShape shape) {
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot);
		try {
			if (slot.equals(EquipmentSlotType.MAINHAND)) {
				ToolStats stat = StatList.get(stack.getTag().getString("image"));
				if (modifiers.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
					modifiers.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				}
				if (modifiers.containsKey(SharedMonsterAttributes.ATTACK_SPEED.getName())) {
					modifiers.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
				}
				float weight = stat.getStat("weight", Float.class);
				float attack = stat.getStat("attack", Float.class);
				double weightCalc = (double) 5 - weight;
				if (weightCalc <= -3) {
					weightCalc = -3;
				}
				
				if (stack.getTag() != null) {
//					ToolStats stat=StatList.get(stack.getTag().getString("image"));
					if (stat == null) {
						StatList.addOrReplaceImage(stack.getTag().getString("image"));
					} else {
						for (String str : (ArrayList<String>) stat.getStat("materialList", ArrayList.class)) {
							if (new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) != ItemStack.EMPTY) {
								damage = attack;
								Function func = CalculationRegistry.getEvent(new ResourceLocation(str), "AttackBoost");
								if (func != null) {
									attack += (float) func.apply(stack);
								}
							}
						}
					}
				}
				
				modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), (new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) attack - 1, AttributeModifier.Operation.ADDITION)));
				modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), (new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", weightCalc - 1, AttributeModifier.Operation.ADDITION)));
			}
		} catch (Exception err) {
		}
		return modifiers;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		//Run Hit Entity Events
		try {
			ToolStats stat = StatList.get(stack.getTag().getString("image"));
			ToolLogicHelper logicHelper = stat.getStat("logicHelper", ToolLogicHelper.class);
			for (String str : (ArrayList<String>) stat.getStat("materialList", ArrayList.class)) {
				if (new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) != ItemStack.EMPTY) {
					float materialPercent = logicHelper.getPercent(str);
					EventRegistryRedo.execute(new ResourceLocation(str), "event_hitentity", target, attacker, materialPercent);
				}
			}
		} catch (Exception err) {
		}
		
		stack.damageItem(2, attacker, (p_220039_0_) -> {
			p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
		});
		return true;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTag() != null) {
			ToolStats stat = StatList.get(stack.getTag().getString("image"));
			if (stat == null) {
				StatList.addOrReplaceImage(stack.getTag().getString("image"));
			} else {
				ToolLogicHelper logicHelper = stat.getStat("logicHelper", ToolLogicHelper.class);
				for (String str : (ArrayList<String>) stat.getStat("materialList", ArrayList.class)) {
					if (new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(str))) != ItemStack.EMPTY) {
						float materialPercent = logicHelper.getPercent(str);
						try {
							EventRegistryRedo.execute(new ResourceLocation(str), "InvTick", stack, entityIn, materialPercent);
						} catch (Exception err) {
						}
					}
				}
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
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
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) damage, AttributeModifier.Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) 0, AttributeModifier.Operation.ADDITION));
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
		return (double) stack.getDamage() / (double) stack.getMaxDamage();
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return getEfficiency(stack);
		return state.isToolEffective(ToolType.PICKAXE) ? getEfficiency(stack) : 1.0F;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		try {
			if (stack.getTag() != null) {
				ToolStats stat = StatList.get(stack.getTag().getString("image"));
				if (stat == null) {
					StatList.addOrReplaceImage(stack.getTag().getString("image"));
				} else {
					try {
						return (int) stat.getStat("durability", Float.class).doubleValue();
					} catch (Exception err) {
					}
				}
			}
		} catch (Exception err) {
		}
		return 100;
	}
	
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		return super.onEntitySwing(stack, entity);
	}
	
	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
		if (state.getHarvestLevel() <= getHarvestLevel(stack)) {
			if (state.isToolEffective(ToolType.PICKAXE)) {
				return true;
			}
		}
		return super.canHarvestBlock(stack, state);
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return false;
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
		return false;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (itemstack.getTag().getBoolean("shield")) {
			playerIn.setActiveHand(handIn);
			return ActionResult.resultConsume(itemstack);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return stack.getTag().getBoolean("shield") ? UseAction.BLOCK : UseAction.NONE;
	}
	
	@Override
	public int getUseDuration(ItemStack stack) {
		if (stack.getTag().getBoolean("shield")) {
			return 1280838939;
		}
		return super.getUseDuration(stack);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
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
