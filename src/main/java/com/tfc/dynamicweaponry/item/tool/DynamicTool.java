package com.tfc.dynamicweaponry.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tfc.dynamicweaponry.client.ToolRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, list, flagIn);
		Tool tool = new Tool(stack);
		
		if (stack.getMaxDamage() <= stack.getDamage()) {
			list.add(new TranslationTextComponent("tooltip.dynamic_weaponry.broken").mergeStyle(TextFormatting.DARK_RED));
			list.add(new StringTextComponent(""));
		}
		
		list.add(new StringTextComponent("Stats:").mergeStyle(TextFormatting.GRAY));
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
			});
		}
		
		list.add(new StringTextComponent(""));
		list.add(new StringTextComponent("When in Main Hand:").mergeStyle(TextFormatting.GRAY));
		list.add(new StringTextComponent(" " + Math.abs(Math.round((tool.getDamage()) * 100) / 100f) + " Attack Damage").mergeStyle(TextFormatting.DARK_GREEN));
		list.add(new StringTextComponent(" " + Math.abs(Math.round((4 - tool.getAttackSpeed()) * 100) / 100f) + " Attack Speed").mergeStyle(TextFormatting.DARK_GREEN));
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.getOrCreateTag().contains("HideFlags")) stack.getOrCreateTag().putInt("HideFlags", 2);
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
		String type = "";
		
		if (stack.getMaxDamage() <= stack.getDamage()) {
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
									(s.equals("pickaxe") && Items.NETHERITE_PICKAXE.canHarvestBlock(stack, state)) ||
									(s.equals("axe") && Items.NETHERITE_AXE.canHarvestBlock(stack, state)) ||
									(s.equals("shovel") && Items.NETHERITE_SHOVEL.canHarvestBlock(stack, state)) ||
									(s.equals("hoe") && Items.NETHERITE_HOE.canHarvestBlock(stack, state)) ||
									(s.equals("sword") && Items.NETHERITE_SWORD.canHarvestBlock(stack, state))
					) {
						toolCount++;
					} else if (s.equals("sword")) {
						swordCount++;
					}
				}
			}
		}
		
		float toolPercent = toolCount / (float) count;
		
		if (toolPercent == 0) {
			float swordPercent = swordCount / (float) count;
			
			if (getDestroySpeedMultiplierSword(state) != 1.0F)
				return (float) (swordPercent * (tool.getEfficiency() * getDestroySpeedMultiplierSword(state)));
			
			return super.getDestroySpeed(stack, state);
		}
		
		return (float) (toolPercent * (tool.getEfficiency() * 3));
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
		if (!attacker.world.isRemote) {
			if (stack.getDamage() >= stack.getMaxDamage()) {
				stack.setDamage(stack.getMaxDamage());
			} else {
				if (stack.getOrCreateTag().contains("Damage"))
					stack.getOrCreateTag().putInt("Damage", stack.getOrCreateTag().getInt("Damage") + 1);
				else stack.getOrCreateTag().putInt("Damage", 1);
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
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return super.getRGBDurabilityForDisplay(stack);
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		return (int) Math.round(new Tool(stack).getDurability());
	}
}
