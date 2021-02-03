package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.assortedutils.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSlot {
	public IInventory inventory;
	public int index;
	public int x, y;
	public Color color = new Color(255, 255, 255);
	public boolean renderSlot = false;
	public boolean renderToolTip = true;
	
	public ItemSlot(IInventory inventory, int index, int x, int y) {
		this.inventory = inventory;
		this.index = index;
		this.x = x;
		this.y = y;
	}
	
	public void set(ItemStack stack) {
		if (inventory == null) return;
		inventory.setInventorySlotContents(index, stack);
	}
	
	public ItemStack get() {
		return inventory == null ? ItemStack.EMPTY : inventory.getStackInSlot(index);
	}
	
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		ItemStack stack = inventory.getStackInSlot(index);
		
		screen.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		RenderSystem.enableTexture();
		RenderSystem.color3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		screen.blit(
				matrixStack, guiLeft + x - 1, guiTop + y - 1,
				7, 119, 18, 18
		);
		
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(
				stack, guiLeft + x, guiTop + y
		);
		
		matrixStack.push();
		Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, stack,
				guiLeft + x, guiTop + y, null
		);
		matrixStack.pop();
		
		if (
				mouseX >= guiLeft + x - 1 &&
						mouseX < guiLeft + x + 17
		) {
			if (
					mouseY >= guiTop + y - 1 &&
							mouseY < guiTop + y + 17
			) {
				RenderSystem.disableTexture();
				RenderSystem.color4f(1, 1, 1, 0.5f);
				matrixStack.push();
				matrixStack.translate(0, 0, 128);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				screen.blit(
						matrixStack,
						guiLeft + x,
						guiTop + y,
						0, 0, 16, 16
				);
				if (!stack.isEmpty() && renderToolTip) {
					matrixStack.translate(0, 0, 450);
					renderTooltip(
							matrixStack, stack, mouseX, mouseY, screen
					);
				}
				matrixStack.pop();
			}
		}
	}
	
	public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		ItemStack stack = inventory.getStackInSlot(index);
		
		if (
				mouseX >= guiLeft + x - 1 &&
						mouseX < guiLeft + x + 17
		) {
			if (
					mouseY >= guiTop + y - 1 &&
							mouseY < guiTop + y + 17
			) {
				RenderSystem.disableTexture();
				RenderSystem.color4f(
						1, 1, 1, 0.5f
				);
				matrixStack.push();
				matrixStack.translate(0, 0, 128);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				screen.blit(
						matrixStack,
						guiLeft + x,
						guiTop + y,
						0, 0, 16, 16
				);
				if (!stack.isEmpty() && renderToolTip) {
					matrixStack.translate(0, 0, 450);
					renderTooltip(
							matrixStack, stack, mouseX, mouseY, screen
					);
				}
				matrixStack.pop();
			}
		}
	}
	
	protected void renderTooltip(MatrixStack matrixStack, ItemStack itemStack, int mouseX, int mouseY, Screen screen) {
		FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
		net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(itemStack);
		screen.renderWrappedToolTip(matrixStack, screen.getTooltipFromItem(itemStack), mouseX, mouseY, (font == null ? Minecraft.getInstance().fontRenderer : font));
		net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
	}
	
	public boolean click(int mouseX, int mouseY, int guiLeft, int guiTop, Screen screen) {
		return
				mouseX >= guiLeft + x - 1 &&
						mouseX < guiLeft + x + 17 &&
						mouseY >= guiTop + y - 1 &&
						mouseY < guiTop + y + 17;
	}
}
