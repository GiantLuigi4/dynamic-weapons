package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.StringReader;
import com.tfc.assortedutils.utils.Color;
import com.tfc.dynamicweaponry.data.*;
import com.tfc.dynamicweaponry.registry.Items;
import com.tfc.dynamicweaponry.tool.MaterialPoint;
import com.tfc.dynamicweaponry.tool.Tool;
import com.tfc.dynamicweaponry.tool.ToolComponent;
import com.tfc.dynamicweaponry.utils.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.command.arguments.ItemParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class ToolCreationScreen extends Screen {
	private static final ResourceLocation Background = new ResourceLocation("dynamic_weaponry:textures/gui/tool_editor.png");
	
	private static final ItemStack defaultTool = new ItemStack(Items.DYNAMIC_TOOL.get());
	
	static {
		try {
			ItemParser parser = new ItemParser(new StringReader(
					"dynamic_weaponry:dynamic_tool{tool_info:{tool_type:\"dynamic_weaponry:single_head\",parts:{\"dynamic_weaponry:short_stick\":{name:\"dynamic_weaponry:short_stick\",points:[{material:\"minecraft:diamond\",x:0,y:2},{material:\"minecraft:diamond\",x:0,y:1},{material:\"minecraft:diamond\",x:0,y:0},{material:\"minecraft:diamond\",x:1,y:2},{material:\"minecraft:diamond\",x:1,y:1},{material:\"minecraft:diamond\",x:1,y:0},{material:\"minecraft:oak_planks\",x:2,y:3},{material:\"minecraft:oak_planks\",x:2,y:2},{material:\"minecraft:diamond\",x:2,y:1},{material:\"minecraft:diamond\",x:2,y:0},{material:\"minecraft:oak_planks\",x:3,y:4},{material:\"minecraft:oak_planks\",x:3,y:3},{material:\"minecraft:oak_planks\",x:3,y:2},{material:\"minecraft:oak_planks\",x:4,y:4},{material:\"minecraft:oak_planks\",x:4,y:3}]},\"dynamic_weaponry:sword_blade\":{name:\"dynamic_weaponry:sword_blade\",points:[{material:\"minecraft:diamond\",x:6,y:8},{material:\"minecraft:diamond\",x:6,y:7},{material:\"minecraft:diamond\",x:6,y:6},{material:\"minecraft:diamond\",x:7,y:9},{material:\"minecraft:diamond\",x:7,y:8},{material:\"minecraft:diamond\",x:7,y:7},{material:\"minecraft:diamond\",x:7,y:6},{material:\"minecraft:diamond\",x:8,y:10},{material:\"minecraft:diamond\",x:8,y:9},{material:\"minecraft:diamond\",x:8,y:8},{material:\"minecraft:diamond\",x:8,y:7},{material:\"minecraft:diamond\",x:8,y:6},{material:\"minecraft:diamond\",x:9,y:11},{material:\"minecraft:diamond\",x:9,y:10},{material:\"minecraft:diamond\",x:9,y:9},{material:\"minecraft:diamond\",x:9,y:8},{material:\"minecraft:diamond\",x:9,y:7},{material:\"minecraft:diamond\",x:10,y:12},{material:\"minecraft:diamond\",x:10,y:11},{material:\"minecraft:diamond\",x:10,y:10},{material:\"minecraft:diamond\",x:10,y:9},{material:\"minecraft:diamond\",x:10,y:8},{material:\"minecraft:diamond\",x:11,y:13},{material:\"minecraft:diamond\",x:11,y:12},{material:\"minecraft:diamond\",x:11,y:11},{material:\"minecraft:diamond\",x:11,y:10},{material:\"minecraft:diamond\",x:11,y:9},{material:\"minecraft:diamond\",x:12,y:14},{material:\"minecraft:diamond\",x:12,y:13},{material:\"minecraft:diamond\",x:12,y:12},{material:\"minecraft:diamond\",x:12,y:11},{material:\"minecraft:diamond\",x:12,y:10},{material:\"minecraft:diamond\",x:13,y:15},{material:\"minecraft:diamond\",x:13,y:14},{material:\"minecraft:diamond\",x:13,y:13},{material:\"minecraft:diamond\",x:13,y:12},{material:\"minecraft:diamond\",x:13,y:11},{material:\"minecraft:diamond\",x:14,y:15},{material:\"minecraft:diamond\",x:14,y:14},{material:\"minecraft:diamond\",x:14,y:13},{material:\"minecraft:diamond\",x:14,y:12},{material:\"minecraft:diamond\",x:15,y:15},{material:\"minecraft:diamond\",x:15,y:14},{material:\"minecraft:diamond\",x:15,y:13}]},\"dynamic_weaponry:sword_guard\":{name:\"dynamic_weaponry:sword_guard\",points:[{material:\"minecraft:gold_ingot\",x:2,y:9},{material:\"minecraft:gold_ingot\",x:2,y:8},{material:\"minecraft:gold_ingot\",x:3,y:9},{material:\"minecraft:gold_ingot\",x:3,y:8},{material:\"minecraft:gold_ingot\",x:3,y:7},{material:\"minecraft:gold_ingot\",x:3,y:6},{material:\"minecraft:gold_ingot\",x:4,y:8},{material:\"minecraft:gold_ingot\",x:4,y:7},{material:\"minecraft:gold_ingot\",x:4,y:6},{material:\"minecraft:gold_ingot\",x:4,y:5},{material:\"minecraft:gold_ingot\",x:5,y:7},{material:\"minecraft:gold_ingot\",x:5,y:6},{material:\"minecraft:gold_ingot\",x:5,y:5},{material:\"minecraft:gold_ingot\",x:5,y:4},{material:\"minecraft:gold_ingot\",x:6,y:5},{material:\"minecraft:gold_ingot\",x:6,y:4},{material:\"minecraft:gold_ingot\",x:6,y:3},{material:\"minecraft:gold_ingot\",x:7,y:5},{material:\"minecraft:gold_ingot\",x:7,y:4},{material:\"minecraft:gold_ingot\",x:7,y:3},{material:\"minecraft:gold_ingot\",x:8,y:4},{material:\"minecraft:gold_ingot\",x:8,y:3},{material:\"minecraft:gold_ingot\",x:8,y:2},{material:\"minecraft:gold_ingot\",x:9,y:3},{material:\"minecraft:gold_ingot\",x:9,y:2}]}}}}"
			), true);
			parser.readItem();
			parser.readNBT();
			defaultTool.getOrCreateTag().merge(parser.getNbt());
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
	
	private final ArrayList<ItemSlot> slots = new ArrayList<>();
	public String currentTool = "";
	public String currentPart = "";
	public Tool tool = new Tool(defaultTool);
	
	public boolean lMouseDown = false;
	public boolean rMouseDown = false;
	public ItemSlot selectedSlot = null;
	
	public ToolCreationScreen(ITextComponent titleIn, Minecraft instance) {
		super(titleIn);
		
		if (instance != null && instance.player != null) {
			for (int i = 0; i < 9; i++) {
				ItemSlot slot = new ItemSlot(instance.player.inventory, i, i * 18 + 8, 182);
				Material material = Loader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
				if (material != null) slot.color = new Color(material.color);
				else slot.color = new Color(255, 255, 255);
				slots.add(slot);
			}
			for (int i = 9; i < 36; i++) {
				ItemSlot slot = new ItemSlot(instance.player.inventory, i, (i % 9) * 18 + 8, 106 + (i / 9) * 18);
				Material material = Loader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
				if (material != null) slot.color = new Color(material.color);
				else slot.color = new Color(255, 255, 255);
				slots.add(slot);
			}
		}
		
		this.minecraft = instance;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		matrixStack.push();
		super.renderBackground(matrixStack);
		matrixStack.translate(0, -40, 0);
		this.minecraft.getTextureManager().bindTexture(Background);
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		this.blit(matrixStack, i, j, 0, 0, (248 - 90), 166);
		this.blit(matrixStack, i + 90, j, 79, 0, (248 - 79), 166);
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		this.blit(matrixStack, i, j + 160, 0, 80, 248, (166 - 80));
		matrixStack.pop();
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		matrixStack.push();
		matrixStack.translate(0, -40, 0);
		this.minecraft.getTextureManager().bindTexture(Background);
		
		if (this.buttons.isEmpty()) {
			this.buttons.add(
					new Button(
							this.width / 2 + 63, j - 30,
							60, 20,
							new StringTextComponent(tool.name),
							this::cycleTool
					)
			);
			this.buttons.add(
					new Button(
							this.width / 2 + 63, j - 10,
							60, 20,
							new StringTextComponent(currentPart),
							this::cyclePart
					)
			);
		}
		
		int minX = 0;
		int minY = 0;
		int maxX = -1;
		int maxY = -1;
		if (!currentPart.equals("")) {
			PartType type = Loader.INSTANCE.partTypes.get(new ResourceLocation(currentPart));
			minX = type.min.x;
			minY = type.min.y;
			maxX = type.max.x;
			maxY = type.max.y;
		}
		
		boolean alternator = false;
		int gridSize = 16;
		matrixStack.translate(i + 18, j + 18, 0);
		matrixStack.scale(1f / gridSize, 1f / gridSize, 1);
		matrixStack.scale(16, 16, 1);
		
		ToolComponent selectedComponent = null;
		
		for (ToolComponent component : tool.components) {
			boolean selected = component.name.equals(currentPart);
			if (selected) {
				selectedComponent = component;
				break;
			}
		}
		
		tool.sort();
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				Color color = new Color(
						alternator ? (128 + 64) : 128,
						alternator ? (128 + 64) : 128,
						alternator ? (128 + 64) : 128
				);
				matrixStack.push();
				matrixStack.scale(8, 8, 1);
				matrixStack.translate(x, ((gridSize - 1) - y), 0);
				RenderSystem.disableTexture();
				
				float rgb = color.getRed() / 255f;
				
				boolean hovered = false;
				
				boolean inBounds =
						x >= minX && x <= maxX &&
								(15 - y) >= minY && (15 - y) <= maxY;
				
				if (
						mouseX >= i + 18 + x * 8 &&
								mouseX < i + 18 + x * 8 + 8 &&
								mouseY >= j + 18 - 40 + ((gridSize - 1) - y) * 8 &&
								mouseY < j + 18 - 40 + ((gridSize - 1) - y) * 8 + 8
				) {
					rgb = rgb * 2;
					hovered = true;
				}
				
				if (
						hovered &&
								selectedComponent != null &&
								lMouseDown &&
								selectedSlot != null &&
								inBounds
				) {
					selectedComponent.setPoint(new Point(x, y), selectedSlot.get().getItem().getRegistryName());
				} else if (
						hovered &&
								selectedComponent != null &&
								rMouseDown &&
								selectedSlot != null &&
								inBounds
				) {
					selectedComponent.setPoint(new Point(x, y), null);
				}
				
				RenderSystem.color3f(
						(rgb) / (inBounds ? 2 : 4),
						(rgb) / (inBounds ? 2 : 4),
						(rgb) / (inBounds ? 2 : 4)
				);
				blit(matrixStack, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0);
				matrixStack.pop();
				alternator = !alternator;
			}
			alternator = !alternator;
		}
		
		for (ToolComponent component : tool.components) {
			boolean selected = component.name.equals(currentPart);
			for (MaterialPoint point : component.points) {
				Material material = Loader.INSTANCE.getMaterial(point.material);
				if (material != null) {
					Color color = Shading.shade(point, tool, component);
					matrixStack.push();
					matrixStack.scale(8, 8, 1);
					matrixStack.translate(point.x, ((gridSize - 1) - point.y), 0);
					RenderSystem.disableTexture();
					float alpha = 1;
					if (!currentPart.equals(component.name))
						alpha = 0.75f;
					
					int x = point.x;
					int y = point.y;
					
					boolean inBounds =
							x >= minX && x <= maxX &&
									(15 - y) >= minY && (15 - y) <= maxY;
					
					boolean isSelected = false;
					if (
							inBounds &&
									mouseX >= i + 18 + x * 8 &&
									mouseX < i + 18 + x * 8 + 8 &&
									mouseY >= j + 18 - 40 + ((gridSize - 1) - y) * 8 &&
									mouseY < j + 18 - 40 + ((gridSize - 1) - y) * 8 + 8
					) {
						isSelected = true;
					}
					
					RenderSystem.color4f(
							(color.getRed() / 255f) / (selected ? 1 : 2),
							(color.getGreen() / 255f) / (selected ? 1 : 2),
							(color.getBlue() / 255f) / (selected ? 1 : 2),
							alpha
					);
					
					RenderSystem.enableAlphaTest();
					RenderSystem.enableBlend();
					blit(matrixStack, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0);
					
					RenderSystem.color4f(
							1, 1, 1,
							isSelected ? 1 : 0
					);
					blit(matrixStack, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0);
					
					matrixStack.pop();
				}
			}
		}
		matrixStack.pop();
		
		matrixStack.push();
		for (ItemSlot slot : slots) slot.render(matrixStack, mouseX, mouseY, i, j, this);
		matrixStack.pop();
	}
	
	private void cyclePart(Button button) {
		try {
			int index = 0;
			ResourceLocation[] typeNames = Loader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
			ToolType[] types = Loader.INSTANCE.toolTypes.values().toArray(new ToolType[0]);
			for (ResourceLocation location : typeNames) {
				if (location.toString().equals(tool.name)) {
					break;
				}
				index++;
			}
			ToolType type = types[index];
			ToolPart[] parts = type.getParts();
			index = 0;
			for (ToolPart part : parts) {
				if (part.type != null) {
					if (part.type.name.toString().equals(currentPart)) {
						break;
					}
				}
				index++;
			}
			if (index + 1 >= parts.length) index = -1;
			ToolPart part = parts[index + 1];
			if (part.type == null) {
				index++;
				if (index + 1 >= parts.length) index = -1;
				part = parts[index + 1];
			}
			ResourceLocation name = part.type.name;
			currentPart = name.toString();
			boolean containsComponent = false;
			for (ToolComponent component : tool.components) {
				if (component.name.equals(name.toString())) {
					containsComponent = true;
					break;
				}
			}
			if (!containsComponent) {
				CompoundNBT nbt = new CompoundNBT();
				nbt.putString("name", currentPart);
				tool.components.add(new ToolComponent(nbt));
			}
			button.setMessage(new StringTextComponent(name.toString()));
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
	
	public void cycleTool(Button button) {
		int index = 0;
		ResourceLocation[] types = Loader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
		for (ResourceLocation location : types) {
			if (location.toString().equals(tool.name)) {
				break;
			}
			index++;
		}
		if (index + 1 >= types.length) index = -1;
		currentTool = types[index + 1].toString();
		tool.name = currentTool;
		button.setMessage(new StringTextComponent(tool.name));
		
		ItemStack newStack = new ItemStack(Items.DYNAMIC_TOOL.get());
		CompoundNBT nbt = newStack.getOrCreateTag();
		CompoundNBT tool_info = new CompoundNBT();
		cyclePart((Button) (buttons.get(1)));
		tool_info.putString("tool_type", currentTool);
		nbt.put("tool_info", tool_info);
		this.tool = new Tool(newStack);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) lMouseDown = true;
		else if (button == 1) rMouseDown = true;
		
		for (Widget button1 : buttons) {
			boolean clicked = button1.mouseClicked(mouseX, mouseY, button);
			if (clicked) return true;
		}
		
		if (button == 0) {
			int i = (this.width - 248) / 2;
			int j = (this.height - 166) / 2;
			for (ItemSlot slot : slots) {
				boolean clicked = slot.click((int) mouseX, (int) mouseY, i, j, this);
				if (clicked) {
					Material material = Loader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
					if (material != null) {
						for (ItemSlot slot1 : slots) {
							material = Loader.INSTANCE.getMaterial(slot1.get().getItem().getRegistryName());
							if (material != null) slot1.color = new Color(material.color);
							else slot1.color = new Color(255, 255, 255);
						}
						selectedSlot = slot;
						slot.color = new Color(128, 255, 128);
					}
					return true;
				}
			}
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) lMouseDown = false;
		else if (button == 1) rMouseDown = false;
		
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return super.charTyped(codePoint, modifiers);
	}
	
	@Override
	public boolean changeFocus(boolean focus) {
		System.out.println(focus);
		return super.changeFocus(false);
	}
}
