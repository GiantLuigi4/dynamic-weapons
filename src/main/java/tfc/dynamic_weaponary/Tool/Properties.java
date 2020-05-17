package tfc.dynamic_weaponary.Tool;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Properties extends Item.Properties {
	
	public Properties() {
		this.maxDamage(5).addToolType(ToolType.PICKAXE, 5).group(ItemGroup.TOOLS).setISTER(() -> ModularItemRenderer::new);
	}
	
	@Override
	public Item.Properties maxStackSize(int maxStackSizeIn) {
		return super.maxStackSize(maxStackSizeIn);
	}
	
	@Override
	public Item.Properties defaultMaxDamage(int maxDamageIn) {
		return super.defaultMaxDamage(maxDamageIn);
	}
	
	@Override
	public Item.Properties maxDamage(int maxDamageIn) {
		return super.maxDamage(maxDamageIn);
	}
	
	@Override
	public Item.Properties containerItem(Item containerItemIn) {
		return super.containerItem(containerItemIn);
	}
	
	@Override
	public Item.Properties group(ItemGroup groupIn) {
		return super.group(groupIn);
	}
	
	@Override
	public Item.Properties rarity(Rarity rarityIn) {
		return super.rarity(rarityIn);
	}
	
	@Override
	public Item.Properties setNoRepair() {
		return super.setNoRepair();
	}
	
	@Override
	public Item.Properties addToolType(ToolType type, int level) {
		return super.addToolType(type, level);
	}
	
	@Override
	public Item.Properties setISTER(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
		return super.setISTER(ister);
	}
}
