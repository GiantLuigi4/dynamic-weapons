package com.tfc.dynamicweaponry.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tfc.dynamicweaponry.client.ToolRenderer;
import com.tfc.dynamicweaponry.material_effects.effects.EffectInstance;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DynamicTool extends Item {
	public DynamicTool() {
		super(getProperties());
	}
	
	public static Properties getProperties() {
		Properties properties = new Properties().maxStackSize(1);
		
		if (FMLEnvironment.dist.isClient()) {
			properties.setISTER(() -> ToolRenderer::getInstance);
		}
		
		return properties;
	}
	
	private static final UUID ATTACK_MODIFIER_UUID = new UUID(92389120L, 4379323L);
	private static final UUID COOLDOWN_MODIFIER_UUID = new UUID(743827923L, 347823123L);
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		if (slot.equals(EquipmentSlotType.MAINHAND)) {
			Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
			Tool tool = new Tool(stack);
			
			if (!(stack.getMaxDamage() <= stack.getDamage())) {
				modifiers.put(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(ATTACK_MODIFIER_UUID, "dynamic_weaponry:attack", tool.getDamage(), AttributeModifier.Operation.ADDITION)
				);
				
				modifiers.put(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(COOLDOWN_MODIFIER_UUID, "dynamic_weaponry:cooldown", -tool.getAttackSpeed(), AttributeModifier.Operation.ADDITION)
				);
			}
			
			return modifiers;
		}
		
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		super.onUse(worldIn, livingEntityIn, stack, count);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
//		return super.onItemUse(context);
//		return ActionResultType.SUCCESS;
		return super.onItemUse(context);
	}
	
	/**
	 * Return whether this item is repairable in an anvil.
	 *
	 * @param toRepair
	 * @param repair
	 */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		Tool tool = new Tool(toRepair);
		HashMap<ResourceLocation, Integer> counts = new HashMap<>();
		for (ToolComponent component : tool.components) {
			for (MaterialPoint point : component.points) {
				int amt = counts.getOrDefault(point.material, 0).intValue() + 1;
				if (counts.containsKey(point.material)) counts.replace(point.material, amt);
				else counts.put(point.material, amt);
			}
		}
		AtomicReference<ResourceLocation> material1 = new AtomicReference<>(new ResourceLocation("minecraft:bedrock"));
		AtomicInteger maxCount = new AtomicInteger(-1);
		counts.forEach((material, amt) -> {
			if (maxCount.get() < amt) {
				maxCount.set(amt);
				material1.set(material);
			}
		});
		return (material1.get().equals(repair.getItem().getRegistryName())) || super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public boolean isRepairable(ItemStack stack) {
		return super.isRepairable(stack);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		Tool tool = new Tool(playerIn.getHeldItem(handIn));
		
		if (tool.isBow()) {
			playerIn.setActiveHand(handIn);
			return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		Tool tool = new Tool(stack);
		if (tool.isBow() || true) {
			if (stack.getTag().contains("pull_time")) {
				float time = stack.getOrCreateTag().getFloat("pull_time");
				stack.getOrCreateTag().putFloat("pull_time", Math.min(1, time + (tool.getDrawSpeed() / 2f)));
			} else {
				stack.getOrCreateTag().putFloat("pull_time", tool.getDrawSpeed() / 2f);
			}
		}
		super.onUsingTick(stack, player, count);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		stack.getOrCreateTag().remove("pull_time");
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		Tool tool = new Tool(stack);
		
		if (tool.isBow()) return UseAction.BOW;
		else return UseAction.NONE;
	}
	
	@Override
	public int getUseDuration(ItemStack stack) {
		Tool tool = new Tool(stack);
		if (tool.isBow()) {
			return (int) (72000 * (1f / tool.getDrawSpeed()));
		} else {
			return 0;
		}
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
//		onUsingTick(stack, context.getPlayer(), 0);
		PlayerEntity playerIn = context.getPlayer();
		Tool tool = new Tool(playerIn.getHeldItem(context.getHand()));
		if (tool.isBow()) {
			if (!playerIn.isHandActive()) playerIn.setActiveHand(context.getHand());
			return ActionResultType.CONSUME;
		}
		return super.onItemUseFirst(stack, context);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, list, flagIn);
		Tool tool = new Tool(stack);
		
		if (stack.getMaxDamage() <= stack.getDamage()) {
			list.add(new TranslationTextComponent("tooltip.dynamic_weaponry.broken").mergeStyle(TextFormatting.DARK_RED));
			list.add(new StringTextComponent(""));
		}
		
		list.add(new StringTextComponent("General Stats:").mergeStyle(TextFormatting.GRAY));
		list.add(new StringTextComponent(
				" Weight: ").mergeStyle(TextFormatting.DARK_GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getWeight() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
		list.add(new StringTextComponent(
				" Efficiency: ").mergeStyle(TextFormatting.DARK_GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getEfficiency() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
		list.add(new StringTextComponent(
				" Durability: ").mergeStyle(TextFormatting.DARK_GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getDurability() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
		
		list.add(new StringTextComponent(""));
		list.add(new StringTextComponent("Mining Stats:").mergeStyle(TextFormatting.GRAY));
		
		{
			int count = 0;
			HashMap<String, Integer> toolCounts = new HashMap<>();
			
			for (ToolComponent component : tool.components) {
				if (component.type != null) {
					if (component.type.getContributesTo().length >= 1) {
						count++;
						
						for (String s : component.type.getContributesTo()) {
							if (toolCounts.containsKey(s)) {
								int toolCount = toolCounts.get(s);
								toolCount++;
								toolCounts.replace(s, toolCount);
							} else {
								toolCounts.put(s, 1);
							}
						}
					}
				}
			}
			
			int finalCount = count;
			final double efficiency = (tool.getEfficiency() * 3);
			toolCounts.forEach((type, toolCount) -> {
				float percent = toolCount / (float) finalCount;
				double toolPower = percent * efficiency;
				String typeText = type.substring(0, 1).toUpperCase() + type.substring(1);
				
				list.add(new StringTextComponent(
						" " + typeText + " Speed: ").mergeStyle(TextFormatting.DARK_GREEN).append(new StringTextComponent(
						String.valueOf(Math.round(toolPower * 100) / 100f)).mergeStyle(TextFormatting.RED))
				);
				
				BlockState state = type.equals("pickaxe") ? Blocks.STONE.getDefaultState() : type.equals("axe") ? Blocks.OAK_PLANKS.getDefaultState() : type.equals("shovel") ? Blocks.DIRT.getDefaultState() : type.equals("hoe") ? Blocks.OAK_LEAVES.getDefaultState() : type.equals("sword") ? Blocks.COBWEB.getDefaultState() : Blocks.BEDROCK.getDefaultState();
				float lvl = tool.calcHarvestLevel(type, state);
				list.add(new StringTextComponent(
						" " + typeText + " Level: ").mergeStyle(TextFormatting.DARK_GREEN).append(new StringTextComponent(
						String.valueOf(Math.round(lvl * 100) / 100f)).mergeStyle(TextFormatting.RED))
				);
			});
		}
		
		list.add(new StringTextComponent(""));
		list.add(new StringTextComponent("Combat Stats:").mergeStyle(TextFormatting.GRAY));
		list.add(new StringTextComponent(" " + Math.abs(Math.round((tool.getDamage()) * 100) / 100f) + " Attack Damage").mergeStyle(TextFormatting.DARK_GREEN));
		list.add(new StringTextComponent(" " + Math.abs(Math.round((4 - tool.getAttackSpeed()) * 100) / 100f) + " Attack Speed").mergeStyle(TextFormatting.DARK_GREEN));
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.getOrCreateTag().contains("HideFlags")) stack.getOrCreateTag().putInt("HideFlags", 2);
		if (!isSelected && (entityIn instanceof PlayerEntity && ((PlayerEntity) entityIn).getHeldItem(Hand.OFF_HAND) != stack))
			stack.getOrCreateTag().remove("pull_time");
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		
		Tool tool = new Tool(stack);
		
		if (entityIn instanceof LivingEntity)
			for (EffectInstance instance : tool.collectEffects()) {
				if (instance.test((LivingEntity) entityIn, worldIn, null, null, tool, instance.owner)) {
					instance.toolEffect.onInventoryTick(stack, tool, tool.calcPercent(instance.owner), instance, isSelected, (LivingEntity) entityIn);
				}
			}
	}
	
	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
		String type = "";
		
		if (
				stack.getMaxDamage() <= stack.getDamage() ||
						getHarvestLevel(stack, state.getHarvestTool(), null, state) < state.getHarvestLevel()
		) {
			return false;
		}
		
		if (state.getBlock().getHarvestTool(state) != null)
			type = state.getBlock().getHarvestTool(state).getName();
		
		Tool tool = new Tool(stack);
		boolean isSword = false;
		
		for (ToolComponent component : tool.components) {
			if (component.type != null && component.type.getContributesTo() != null && component.type.getContributesTo().length >= 1) {
				for (String s : component.type.getContributesTo()) {
					if (s.equals(type) ||
							state.isToolEffective(ToolType.get(s)) ||
							(s.equals("pickaxe") && Items.NETHERITE_PICKAXE.canHarvestBlock(stack, state)) ||
							(s.equals("axe") && Items.NETHERITE_AXE.canHarvestBlock(stack, state)) ||
							(s.equals("shovel") && Items.NETHERITE_SHOVEL.canHarvestBlock(stack, state)) ||
							(s.equals("hoe") && Items.NETHERITE_HOE.canHarvestBlock(stack, state)) ||
							(s.equals("sword") && Items.NETHERITE_SWORD.canHarvestBlock(stack, state))
					) {
						return true;
					} else if (s.equals("sword")) {
						isSword = true;
					}
				}
			}
		}
		
		return isSword && (getDestroySpeedMultiplierSword(state) != 1.0F);
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		String type = "";
		
		if (state.getBlock().getHarvestTool(state) != null)
			type = state.getBlock().getHarvestTool(state).getName();
		
		if (stack.getMaxDamage() <= stack.getDamage()) {
			return super.getDestroySpeed(stack, state);
		}
		
		Tool tool = new Tool(stack);
		int count = 0;
		int toolCount = 0;
		int swordCount = 0;
		
		for (ToolComponent component : tool.components) {
			if (component.type != null && component.type.getContributesTo() != null && component.type.getContributesTo().length >= 1) {
				count++;
				
				for (String s : component.type.getContributesTo()) {
					if (
							s.equals(type) ||
									state.isToolEffective(ToolType.get(s)) ||
									(s.equals("pickaxe") && Tool.checkCanHarvest(Items.NETHERITE_PICKAXE, state, true)) ||
									(s.equals("axe") && Tool.checkCanHarvest(Items.NETHERITE_AXE, state, true)) ||
									(s.equals("shovel") && Tool.checkCanHarvest(Items.NETHERITE_SHOVEL, state, true)) ||
									(s.equals("hoe") && Tool.checkCanHarvest(Items.NETHERITE_HOE, state, true)) ||
									(s.equals("sword") && Tool.checkCanHarvest(Items.NETHERITE_SWORD, state, true))
					) {
						toolCount++;
					} else if (s.equals("sword")) {
						swordCount++;
					}
				}
			}
		}
		
		float harvestLvl = getHarvestLevel(stack, state.getHarvestTool(), null, state);
		int requiredHarvestLvl = state.getHarvestLevel();
		
		float efficiency = 1f / (Math.max(1, requiredHarvestLvl) / Math.max(1, harvestLvl));
		
		float toolPercent = toolCount / (float) count;
		
		if (toolPercent == 0) {
			float swordPercent = swordCount / (float) count;
			
			if (getDestroySpeedMultiplierSword(state) != 1.0F)
				return (float) (swordPercent * (tool.getEfficiency() * getDestroySpeedMultiplierSword(state))) * efficiency;
			
			if (harvestLvl != 0) {
				return (float) (toolPercent * (tool.getEfficiency() * 3)) * efficiency;
			}
			return super.getDestroySpeed(stack, state) * efficiency;
		}
		
		return (float) (toolPercent * (tool.getEfficiency() * 3)) * efficiency;
	}
	
	private float getDestroySpeedMultiplierSword(BlockState state) {
		if (state.isIn(Blocks.COBWEB)) {
			return 15.0F;
		} else if (state.getBlock().equals(Blocks.BAMBOO) || state.getBlock().equals(Blocks.BAMBOO_SAPLING)) {
			return 100000.0F;
		} else {
			net.minecraft.block.material.Material material = state.getMaterial();
			return material != net.minecraft.block.material.Material.PLANTS && material != net.minecraft.block.material.Material.TALL_PLANTS && material != net.minecraft.block.material.Material.CORAL && !state.isIn(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 1.5F;
		}
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!attacker.world.isRemote && !((attacker instanceof PlayerEntity) && ((PlayerEntity) attacker).isCreative())) {
			if (stack.getDamage() >= stack.getMaxDamage()) {
				stack.setDamage(stack.getMaxDamage());
			} else {
				if (stack.getOrCreateTag().contains("Damage"))
					stack.getOrCreateTag().putInt("Damage", stack.getOrCreateTag().getInt("Damage") + 1);
				else stack.getOrCreateTag().putInt("Damage", 1);
			}
		}
		
		Tool tool = new Tool(stack);
		for (EffectInstance instance : tool.collectEffects()) {
			if (instance.test(attacker, attacker.world, null, null, tool, instance.owner)) {
				instance.toolEffect.onStrike(stack, tool, tool.calcPercent(instance.owner), instance, attacker, target);
			}
		}
		
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDamage(stack) != 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return super.getDurabilityForDisplay(stack);
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public boolean isDamaged(ItemStack stack) {
		return stack.getOrCreateTag().contains("Damage") || stack.getOrCreateTag().getInt("Damage") != 0;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!entityLiving.world.isRemote && !((entityLiving instanceof PlayerEntity) && ((PlayerEntity) entityLiving).isCreative())) {
			if (stack.getDamage() >= stack.getMaxDamage()) {
				stack.setDamage(stack.getMaxDamage());
			} else {
				if (stack.getOrCreateTag().contains("Damage"))
					stack.getOrCreateTag().putInt("Damage", stack.getOrCreateTag().getInt("Damage") + 1);
				else stack.getOrCreateTag().putInt("Damage", 1);
			}
		}
		
		Tool tool = new Tool(stack);
		for (EffectInstance instance : tool.collectEffects()) {
			if (instance.test(entityLiving, worldIn, state, pos, tool, instance.owner)) {
				instance.toolEffect.onBlockBreak(stack, tool, tool.calcPercent(instance.owner), instance, state, pos, worldIn, entityLiving);
			}
		}
		
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		if (oldStack.hasTag()) {
			CompoundNBT nbtOld = oldStack.getOrCreateTag().copy();
			nbtOld.remove("Durability");
			if (newStack.hasTag()) {
				CompoundNBT nbtNew = newStack.getOrCreateTag().copy();
				nbtNew.remove("Durability");
				return !nbtOld.equals(nbtNew);
			} else {
				return true;
			}
		} else {
			return !oldStack.equals(newStack);
		}
	}
	
	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
		if (oldStack.hasTag()) {
			CompoundNBT nbtOld = oldStack.getOrCreateTag().copy();
			nbtOld.remove("Durability");
			if (newStack.hasTag()) {
				CompoundNBT nbtNew = newStack.getOrCreateTag().copy();
				nbtNew.remove("Durability");
				return !nbtOld.equals(nbtNew);
			} else {
				return true;
			}
		} else {
			return !oldStack.equals(newStack);
		}
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return super.getRGBDurabilityForDisplay(stack);
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		return (int) Math.round(new Tool(stack).getDurability());
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState state) {
		if (tool == null) return 0;
		
		String type = tool.getName();
		
		Tool tool1 = new Tool(stack);
		
		if (stack.getMaxDamage() <= stack.getDamage()) return -1;
		
		return (int) tool1.calcHarvestLevel(type, state);
	}
}
