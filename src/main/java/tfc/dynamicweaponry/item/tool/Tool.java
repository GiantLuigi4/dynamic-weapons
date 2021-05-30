package tfc.dynamicweaponry.item.tool;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import tfc.dynamicweaponry.data.DataLoader;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.data.ToolPart;
import tfc.dynamicweaponry.data.ToolType;
import tfc.dynamicweaponry.material_effects.effects.EffectInstance;
import tfc.dynamicweaponry.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Tool {
	public final ArrayList<ToolComponent> components = new ArrayList<>();
	public String name;
	
	public Tool(ItemStack item) {
		CompoundNBT nbt = item.getOrCreateTag();
		
		if (item.getOrCreateTag().contains("tool_info")) nbt = item.getOrCreateTag().getCompound("tool_info");
		
		if (nbt.contains("tool_type")) name = nbt.getString("tool_type");
		else name = "dynamic_weaponry:single_head";
		
		ListNBT palletNBT;
		if (nbt.contains("pallet")) palletNBT = nbt.getList("pallet", Constants.NBT.TAG_COMPOUND);
		else palletNBT = new ListNBT();
		ResourcePallet pallet = new ResourcePallet(palletNBT);
		
		if (nbt.contains("parts")) {
			CompoundNBT parts = nbt.getCompound("parts");
			for (String s : parts.keySet()) {
				CompoundNBT part = parts.getCompound(s);
				try {
					components.add(new ToolComponent(part, pallet));
				} catch (Throwable ignored) {
				}
			}
		}
	}
	
	public double getDamage() {
		double amt = 0;
		
		sort();
		
		int divisor = 0;
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			ToolComponent component = components.get(i);
			
			if (component.points.isEmpty()) continue;
			
			divisor++;
			
			int count = 0;
			HashMap<Material, Integer> counts = new HashMap<>();
			
			for (MaterialPoint point : component.points) {
				Material mat = DataLoader.INSTANCE.getMaterial(point.material);
				if (mat == null || mat.item == null || mat.item.toString().equals("minecraft:air")) continue;
				count++;
				if (!counts.containsKey(mat)) counts.put(mat, 1);
				else counts.replace(mat, counts.get(mat) + 1);
			}
			
			for (Material mat : counts.keySet()) {
				Integer matAmt = counts.get(mat);
				float matPercent = ((float) matAmt) / count;
				if (mat != null) amt += matPercent * mat.attack;
//				amt /= 1.3f;
			}
		}
		
		if (divisor != 0)
			amt /= (divisor);
		
		amt += (getWeight() / 40f) - 1;
		if (getAttackSpeed() >= 3.99) {
			double speed = MathHelper.lerp(0.25, ((getWeight()) * (0.1 / getEfficiency())), 1.6) / 2f;
			double overflow = (3f / (speed - 3.99));
			if (overflow > 3) overflow = 3f / overflow;
			if (overflow > 2) overflow = 2f / overflow;
			if (overflow > 1) overflow = 1f / overflow;
			amt *= overflow;
		}
		if (isBow()) {
			float f = (1f / (getDrawSpeed()));
			f /= 4;
			f = Math.min(f, 3);
			f *= amt / 3f;
			return f;
		}
		return amt;
	}
	
	public double getWeight() {
		double amt = 0;
		for (ToolComponent component : this.components) {
			for (MaterialPoint point : component.points) {
				Material mat = DataLoader.INSTANCE.getMaterial(point.material);
				if (mat != null)
					amt += mat.weight;
			}
		}
		return amt;
	}
	
	public double getEfficiency() {
		double amt = 0;
		
		sort();
		
		int divisor = 0;
		
		for (ToolComponent component : this.components) {
			if (component.points.isEmpty())
				continue;
			
			divisor++;
			
			int count = 0;
			HashMap<Material, Integer> counts = new HashMap<>();
			
			for (MaterialPoint point : component.points) {
				Material mat = DataLoader.INSTANCE.getMaterial(point.material);
				count++;
				if (point.material == null) continue;
				if (!counts.containsKey(mat)) counts.put(mat, 1);
				else counts.replace(mat, counts.get(mat) + 1);
			}
			
			for (Material mat : counts.keySet()) {
				Integer matAmt = counts.get(mat);
				float matPercent = ((float) matAmt) / count;
				if (mat != null) amt += matPercent * mat.efficiency;
//				amt /= 1.3;
			}
		}
		
		if (divisor != 0)
			amt /= (divisor);
		
		return Math.max(amt, 1);
	}
	
	public double getAttackSpeed() {
		return Math.min(MathHelper.lerp(0.25, ((getWeight()) * (0.1 / getEfficiency())), 1.6) / 2, 3.99);
	}
	
	public double getDurability() {
		double amt = 0;
		
		float scl = 0.01f;

//		int count = 0;
//		HashMap<Material,Integer> counts = new HashMap<>();
		
		for (ToolComponent component : this.components) {
			for (MaterialPoint point : component.points) {
				Material mat = DataLoader.INSTANCE.getMaterial(point.material);
//				count++;
//				if (!counts.containsKey(mat)) counts.put(mat, 1);
//				else counts.replace(mat, counts.get(mat) + 1);
				
				if (mat != null) amt += mat.durability * scl;
			}
		}

//		for (Material mat : counts.keySet()) {
//			Integer matAmt = counts.get(mat);
//			float matPercent = ((float) matAmt) / count;
//			amt += matPercent * mat.efficiency;
//		}
		
		return Math.max(amt, 1);
	}
	
	public Tool(String name) {
		this.name = name;
	}
	
	public void sort() {
		components.sort(ToolComponent::compareTo);
	}
	
	public CompoundNBT serialize() {
		CompoundNBT thisNBT = new CompoundNBT();
		thisNBT.putString("tool_type", name);
		CompoundNBT parts = new CompoundNBT();
		ResourcePallet pallet = new ResourcePallet();
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				if (!pallet.contains(point.material)) {
					pallet.add(pallet.size(), point.material);
				}
			}
		}
		for (ToolComponent component : components) {
			parts.put(component.name, component.serialize(pallet));
		}
		thisNBT.put("parts", parts);
		thisNBT.put("pallet", pallet.serialize());
		return thisNBT;
	}
	
	public Tool copy() {
		sort();
		CompoundNBT nbt = this.serialize();
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt1.put("tool_info", nbt);
		Tool tool = new Tool(stack);
		tool.sort();
		return tool;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tool tool = (Tool) o;
		return Objects.equals(components, tool.components) &&
				Objects.equals(name, tool.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(components, name);
	}
	
	public ToolComponent getComponent(ResourceLocation location) {
		for (ToolComponent component : components) {
			if (component.name.equals(location.toString()))
				return component;
		}
		
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("name", location.toString());
		ToolComponent component = new ToolComponent(nbt);
		components.add(component);
		return component;
	}
	
	public boolean isPartCompatible(ResourceLocation type) {
		ToolType toolType = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(name));
		
		if (toolType == null) return true;
		
		ToolPart toolPart = toolType.getPart(type);
		
		if (toolPart == null) return true;
		
		if (hasIncompatibility(toolPart)) return false;
		for (ToolPart dependency : toolPart.getDependencies())
			if (dependency.type != null && !hasIncompatibility(dependency)) return true;
		
		return true;
	}
	
	public boolean hasIncompatibility(ToolPart toolPart) {
		for (ToolComponent component : components) {
			if (toolPart.toolType == null) return true;
			try {
				ToolPart componentPart = toolPart.toolType.getPart(component.type.name);
				if (
						toolPart.type != null &&
								component.type != null &&
								componentPart != null &&
								toolPart.listIndex ==
										componentPart.listIndex &&
								!component.points.isEmpty() &&
								!toolPart.type.name.equals(component.type.name)
//								(toolPart.listIndex == componentPart.listIndex && !toolPart.type.name.equals(componentPart.type.name))
				) {
					return true;
				} else if (
						toolPart.type != null &&
								component.type != null &&
								componentPart != null &&
								!component.points.isEmpty()
				) {
					for (ToolPart incompat : componentPart.getIncompatibilities()) {
						if (incompat.type != null && incompat.type.name.equals(toolPart.type.name))
							return true;
					}
				}
			} catch (Throwable err) {
				err.printStackTrace();
				return false;
			}
		}
		if (toolPart.getIncompatibilities() != null) {
			for (ToolPart incompatibility : toolPart.getIncompatibilities()) {
				for (ToolComponent component : components) {
					if (component.type != null &&
							component.name.equals(incompatibility.type.name.toString()) &&
							!component.points.isEmpty()
					) return true;
				}
			}
		}
		return false;
	}
	
	public MaterialPoint getPoint(int x, int y) {
		for (ToolComponent component : components) {
			if (!component.points.isEmpty()) {
				MaterialPoint point = component.getPoint(x, y);
				if (point != null) return point;
			}
		}
		return null;
	}
	
	public float getMaterialCost(Material material) {
		float amt = 0;
		float matPerPixel = 0.03f;
		
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				if (point.material != null && point.material.equals(material.item)) {
					amt += matPerPixel;
				}
			}
		}
		
		return amt;
	}
	
	public static boolean checkCanHarvest(Item item, BlockState state, boolean speed) {
		return (state != null && (item.canHarvestBlock(new ItemStack(item), state) || item.canHarvestBlock(state) || (speed && item.getDestroySpeed(new ItemStack(item), state) != 1.0F)));
	}
	
	public static boolean checkCanHarvest(Item item, BlockState state) {
		return checkCanHarvest(item, state, true);
	}
	
	public float calcHarvestLevel(@Nonnull String type, @Nullable BlockState state) {
		int toolCount = 0;
		
		float harvestLevel = -1;
		
		for (ToolComponent component : this.components) {
			if (component.type != null && component.type.getContributesTo() != null && component.type.getContributesTo().length >= 1) {
				
				for (String s : component.type.getContributesTo()) {
					if (
							s.equals(type) || (state != null && (
									state.isToolEffective(net.minecraftforge.common.ToolType.get(s)) ||
											(s.equals("pickaxe") && checkCanHarvest(Items.NETHERITE_PICKAXE, state)) ||
											(s.equals("axe") && checkCanHarvest(Items.NETHERITE_AXE, state)) ||
											(s.equals("shovel") && checkCanHarvest(Items.NETHERITE_SHOVEL, state)) ||
											(s.equals("hoe") && checkCanHarvest(Items.NETHERITE_HOE, state)) ||
											(s.equals("sword") && checkCanHarvest(Items.NETHERITE_SWORD, state))
							))) {
						harvestLevel = Math.max(harvestLevel, 0);
						toolCount++;
						harvestLevel += component.getHarvestLevel();
					}
				}
			}
		}
		
		harvestLevel /= toolCount;
		
		return Math.max(-1, harvestLevel);
	}
	
	public boolean isBow() {
		for (ToolComponent component : components) {
			if (component.type != null && Arrays.asList(component.type.getContributesTo()).contains("bow")) {
				return true;
			}
		}
		return false;
	}
	
	public float getDrawSpeed() {
		float amt = 0;
		int count = 0;
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				Material material = DataLoader.INSTANCE.getMaterial(point.material);
				if (material != null) {
					amt += material.getDrawSpeed();
					count++;
				}
			}
		}
		amt /= count;
		return (float) (amt / Math.max(1, getEfficiency() / 2f)) / 25f;
	}
	
	public float calcPercent(Material material) {
		int count = 0;
		int totalCount = 0;
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				if (point.material != null) {
					if (material.item.equals(point.material)) {
						count++;
					}
					totalCount++;
				}
			}
		}
		return (float) count / (float) totalCount;
	}
	
	public ArrayList<EffectInstance> collectEffects() {
		ArrayList<ResourceLocation> materialsFound = new ArrayList<>();
		for (ToolComponent component : components) {
			for (MaterialPoint point : component.points) {
				if (point.material != null && !materialsFound.contains(point.material)) {
					materialsFound.add(point.material);
				}
			}
		}
		ArrayList<EffectInstance> effectInstances = new ArrayList<>();
		try {
			for (ResourceLocation location : materialsFound) {
				effectInstances.addAll(Arrays.asList(DataLoader.INSTANCE.getMaterial(location).getEffectInstances()));
			}
		} catch (Throwable ignored) {
		}
		return effectInstances;
	}
	
	public boolean areDepsFilled(ResourceLocation type) {
		ToolType toolType = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(name));
		
		if (toolType == null) return true;
		
		ToolPart toolPart = toolType.getPart(type);
		
		if (toolPart == null) return true;
		
		int numFilled = 0;
		for (ToolPart dependency : toolPart.getDependencies()) {
			for (ToolComponent component : this.components) {
				if (component.type != null && dependency.type != null && !component.points.isEmpty() && component.name.equals(dependency.type.name.toString())) {
					numFilled++;
				}
			}
		}
		
		return numFilled == toolPart.getDependencies().length;
	}
}
