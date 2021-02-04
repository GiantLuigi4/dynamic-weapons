package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.dynamicweaponry.DynamicWeaponry;
import com.tfc.dynamicweaponry.data.DataLoader;
import com.tfc.dynamicweaponry.data.PartType;
import com.tfc.dynamicweaponry.data.ToolPart;
import com.tfc.dynamicweaponry.data.ToolType;
import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.network.ToolPacket;
import com.tfc.dynamicweaponry.registry.Registry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import java.util.ArrayList;

public class ToolComponentSlot extends ItemSlot {
	public final ResourceLocation location;
	public final ResourceLocation name;
	public final boolean isTool;
	
	public ToolComponentSlot(int x, int y, ResourceLocation location, ResourceLocation name, boolean isTool) {
		super(null, 0, x, y);
		this.location = location;
		this.name = name;
		this.isTool = isTool;
	}
	
	@Override
	public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		if (!(screen instanceof ToolCreationScreen)) return;
		
		int x = guiLeft - 128;
		int y = 0;
		x = Math.max(9, x);
		
		int num = 0;
		while (num < this.x) {
			x += 18;
			if (x >= guiLeft - 18) {
				x = guiLeft - 128;
				x = Math.max(9, x);
				y += 18;
			}
			num++;
		}
		
		boolean hovered =
				mouseX >= x - 1 &&
						mouseX < x + 17 &&
						mouseY >= guiTop + y + this.y - 34 - 1 &&
						mouseY < guiTop + y + this.y - 34 + 17;
		if (!hovered) return;
		
		ArrayList<ITextComponent> components = new ArrayList<>();
		String prefix = (isTool ? "tool_type" : "part_type") + ".";
		String name = prefix + this.name.getNamespace() + "." + this.name.getPath().replace("/", ".");
		components.add(new TranslationTextComponent(name));
		
		if (!isTool) {
			ToolType toolType = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(((ToolCreationScreen) screen).currentTool));
			
			if (toolType != null) {
				ToolPart toolPart = toolType.getPart(this.name);
				
				if (toolPart != null) {
					if (toolPart.getIncompatibilities().length != 0) {
						IFormattableTextComponent incompats = new StringTextComponent("Incompatible with: ").mergeStyle(TextFormatting.DARK_RED);
						
						components.add(incompats);
						
						for (ToolPart incompatibility : toolPart.getIncompatibilities()) {
							if (incompatibility.type == null) continue;
							String name1 = prefix + incompatibility.type.name.getNamespace() + "." + incompatibility.type.name.getPath().replace("/", ".");
							components.add(new StringTextComponent(" -").append(new TranslationTextComponent(name1).mergeStyle(TextFormatting.RED)));
						}
					}
					
					if (toolPart.getDependencies().length != 0) {
						if (toolPart.getIncompatibilities().length != 0) {
							components.add(new StringTextComponent(""));
						}
						
						IFormattableTextComponent incompats = new StringTextComponent("Depends on: ").mergeStyle(TextFormatting.GREEN);
						
						components.add(incompats);
						
						for (ToolPart incompatibility : toolPart.getDependencies()) {
							if (incompatibility.type == null) continue;
							String name1 = prefix + incompatibility.type.name.getNamespace() + "." + incompatibility.type.name.getPath().replace("/", ".");
							components.add(new StringTextComponent(" -").append(new TranslationTextComponent(name1).mergeStyle(TextFormatting.RED)));
						}
					}
					
					if (toolPart.type.getContributesTo().length != 0) {
						if (toolPart.getIncompatibilities().length != 0 || toolPart.getDependencies().length != 0) {
							components.add(new StringTextComponent(""));
						}
						
						IFormattableTextComponent incompats = new StringTextComponent("Tool Types: ").mergeStyle(TextFormatting.DARK_GREEN);
						
						components.add(incompats);
						
						for (String incompatibility : toolPart.type.getContributesTo()) {
							incompatibility = incompatibility.substring(0, 1).toUpperCase() + incompatibility.substring(1);
							components.add(new StringTextComponent(" -").append(new StringTextComponent(incompatibility).mergeStyle(TextFormatting.RED)));
						}
					}
				}
			}
		}
		
		net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(matrixStack, components, mouseX, mouseY, screen.width, screen.height, -1, screen.getMinecraft().fontRenderer);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		if (!(screen instanceof ToolCreationScreen)) {
			return;
		}
		Tool tool = ((ToolCreationScreen) screen).tool;
		
		matrixStack.push();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
		
		int x = guiLeft - 128;
		int y = 0;
		x = Math.max(9, x);
		
		int num = 0;
		while (num < this.x) {
			x += 18;
			if (x >= guiLeft - 18) {
				x = guiLeft - 128;
				x = Math.max(9, x);
				y += 18;
			}
			num++;
		}
		
		matrixStack.translate(x, guiTop + y - 34 + this.y, 0);
		
		if (isTool || tool.isPartCompatible(name)) {
			if (isTool && ((ToolCreationScreen) screen).currentTool.equals(name.toString())) {
				RenderSystem.color3f(0, 1, 0);
			} else if (!isTool && ((ToolCreationScreen) screen).currentPart.equals(name.toString())) {
				RenderSystem.color3f(0, 1, 0);
			} else if (!isTool && ((ToolCreationScreen) screen).tool.areDepsFilled(name)) {
				RenderSystem.color3f(0, 1, 1);
			} else {
				RenderSystem.color3f(1, 1, 1);
			}
		} else {
			RenderSystem.color3f(1, 0, 0);
		}
		
		screen.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		screen.blit(
				matrixStack,
				-1, -1,
				7, 119,
				18, 18
		);
		
		RenderSystem.color3f(1, 1, 1);
		
		matrixStack.push();
		matrixStack.scale(1f / 255, 1f / 255, 1);
		matrixStack.scale(16, 16, 1);
		screen.getMinecraft().getTextureManager().bindTexture(location);
		screen.blit(
				matrixStack,
				0, 0,
				0, 0,
				255, 255
		);
		matrixStack.pop();
		
		boolean hovered =
				mouseX >= x - 1 &&
						mouseX < x + 17 &&
						mouseY >= guiTop + y + this.y - 34 - 1 &&
						mouseY < guiTop + y + this.y - 34 + 17;
		if (hovered) {
			RenderSystem.disableTexture();
			RenderSystem.enableAlphaTest();
			RenderSystem.enableBlend();
			RenderSystem.color4f(1, 1, 1, 0.5f);
			screen.blit(
					matrixStack,
					-1, -1,
					0, 0,
					18, 18
			);
		}
		
		matrixStack.pop();
	}
	
	@Override
	public boolean click(int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		
		int x = guiLeft - 128;
		int y = 0;
		x = Math.max(9, x);
		
		int num = 0;
		while (num < this.x) {
			x += 18;
			if (x >= guiLeft - 18) {
				x = guiLeft - 128;
				x = Math.max(9, x);
				y += 18;
			}
			num++;
		}
		
		boolean wasClicked =
				mouseX >= x - 1 &&
						mouseX < x + 17 &&
						mouseY >= guiTop + y + this.y - 34 - 1 &&
						mouseY < guiTop + y + this.y - 34 + 17;
		
		if (wasClicked) {
			if (screen instanceof ToolCreationScreen) {
				ToolCreationScreen toolScreen = (ToolCreationScreen) screen;
				if (isTool) {
					String lastTool = toolScreen.currentTool;
					toolScreen.currentTool = name.toString();
					if (!toolScreen.currentTool.equals(lastTool)) {
						ItemStack newStack = new ItemStack(Registry.DYNAMIC_TOOL.get());
						CompoundNBT nbt = newStack.getOrCreateTag();
						CompoundNBT tool_info = new CompoundNBT();
						tool_info.putString("tool_type", toolScreen.currentTool);
						nbt.put("tool_info", tool_info);
						toolScreen.tool = new Tool(newStack);
						DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new ToolPacket(toolScreen.tool));
						
						PartType part = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(toolScreen.tool.name)).getParts()[0].type;
						toolScreen.currentPart = part.name.toString();
						toolScreen.tool.getComponent(new ResourceLocation(toolScreen.currentPart));
					}
				} else {
					Tool tool = toolScreen.tool;
					if (tool.isPartCompatible(name)) {
						toolScreen.currentPart = name.toString();
						tool.getComponent(new ResourceLocation(toolScreen.currentPart));
					}
				}
			}
		}
		
		return false;
	}
}
