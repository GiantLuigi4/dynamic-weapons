package com.tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ToolCreationScreen extends Screen {
	private static final ResourceLocation Background = new ResourceLocation("dynamic_weaponry:textures/gui/tool_editor.png");
	
	public ToolCreationScreen(ITextComponent titleIn) {
		super(titleIn);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.minecraft.getTextureManager().bindTexture(Background);
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		this.blit(matrixStack, i, j, 0, 0, 248, 166);
	}
}
