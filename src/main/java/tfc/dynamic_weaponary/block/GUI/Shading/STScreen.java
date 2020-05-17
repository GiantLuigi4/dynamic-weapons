package tfc.dynamic_weaponary.block.GUI.Shading;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import tfc.dynamic_weaponary.Utils.DrawingUtils;

//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
public class STScreen extends ContainerScreen<STContainer> {
	private static final ResourceLocation GUI_BG = new ResourceLocation("dynamic_weaponry", "textures/gui/shader_screen.png");
	
	public STScreen(STContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableTexture();
		DrawingUtils.drawTexturedRect(this.guiLeft + 4, this.guiTop + 4, 1, 1, 1, 1, 128, 128, 0.5, 0.5, 0.5, 1);
		RenderSystem.enableTexture();
		this.minecraft.getTextureManager().bindTexture(GUI_BG);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(this.getTitle().getFormattedText(), 10, 10, 0x404040);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
	}
}
