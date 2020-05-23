package tfc.dynamic_weaponary.Utils.Optimization;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.Image.MaterialBasedPixelStorage;
import tfc.dynamic_weaponary.Utils.Tool.Material;
import tfc.dynamic_weaponary.Utils.Tool.ToolLogicHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ToolStats {
	public MaterialBasedPixelStorage image;
	
	HashMap<String, Object> stats = new HashMap<>();
	
	public ToolStats(MaterialBasedPixelStorage image) {
		this.image = image;
		ToolLogicHelper logicHelper = new ToolLogicHelper();
		ArrayList<String> items = new ArrayList<>();
		int i = 0;
		for (MaterialBasedPixelStorage.MaterialPixel px : image.image) {
			if (!px.stack.equals(ItemStack.EMPTY)) {
				if (!items.contains(px.stack.getItem().getRegistryName().toString())) {
					items.add(px.stack.getItem().getRegistryName().toString());
				}
				i++;
				logicHelper.add(px.stack.getItem().getRegistryName().toString(), 1);
			}
		}
		float attack = 0;
		float weight = 0;
		float durability = 0;
		for (String str : items) {
			ItemStack checkStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)));
			if (checkStack != ItemStack.EMPTY) {
				Material mat = MaterialList.lookupMaterial(checkStack);
				float materialPercent = logicHelper.getPercent(str);
				attack += materialPercent * mat.strength;
				weight += materialPercent * mat.weight;
				durability += materialPercent * mat.durability;
			}
		}
		if (i >= 1) {
			stats.put("attack", attack);
			stats.put("weight", weight);
			stats.put("durability", durability);
			stats.put("logicHelper", logicHelper);
			stats.put("materialList", items);
		}
	}
	
	public <T> T getStat(String stat, Class<T> clazz) {
		return (T) stats.get(stat);
	}
}
