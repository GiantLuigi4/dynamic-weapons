package com.tfc.dynamicweaponry.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tfc.dynamicweaponry.client.ToolRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
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
	
	/**
	 * ItemStack sensitive version of getItemAttributeModifiers
	 *
	 * @param slot
	 * @param stack
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		if (slot.equals(EquipmentSlotType.MAINHAND)) {
			Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
			Tool tool = new Tool(stack);
			modifiers.put(
					Attributes.ATTACK_DAMAGE,
					new AttributeModifier(ATTACK_MODIFIER_UUID, "dynamic_weaponry:attack", tool.getDamage(), AttributeModifier.Operation.ADDITION)
			);
			modifiers.put(
					Attributes.ATTACK_SPEED,
					new AttributeModifier(COOLDOWN_MODIFIER_UUID, "dynamic_weaponry:cooldown", -tool.getAttackSpeed(), AttributeModifier.Operation.ADDITION)
			);
			return modifiers;
		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param stack
	 * @param worldIn
	 * @param list
	 * @param flagIn
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, list, flagIn);
		Tool tool = new Tool(stack);
		list.add(new StringTextComponent(
				"Weight: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getWeight() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
		list.add(new StringTextComponent(
				"Efficiency: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getEfficiency() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
		list.add(new StringTextComponent(
				"Durability: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
				String.valueOf(Math.round(tool.getDurability() * 100) / 100f)).mergeStyle(TextFormatting.RED))
		);
//		list.add(new StringTextComponent(
//				"Attack Power: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
//				String.valueOf(tool.getDamage())).mergeStyle(TextFormatting.RED))
//		);
	}
}
