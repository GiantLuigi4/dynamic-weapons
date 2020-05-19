package tfc.dynamic_weaponary.block.ShadingTable;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import tfc.dynamic_weaponary.ShaderOrb.ShaderItem;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;

//https://github.com/3TUSK/SRA/blob/bleeding/src/main/java/info/tritusk/anchor/AnchorScreen.java
public class STScreen extends ContainerScreen<STContainer> {
	private static final ResourceLocation GUI_BG = new ResourceLocation("dynamic_weaponry", "textures/gui/shader_screen.png");
	int slider1 = 0;
	int slider2 = 0;
	int slider3 = 0;
	boolean isLeftDown = false;
	
	public STScreen(STContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public boolean mouseReleased(double x, double y, int button) {
		if (button == 0) {
			isLeftDown = false;
		}
		return super.mouseReleased(x, y, button);
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int button) {
		if (button == 0) {
			isLeftDown = true;
		}
		return super.mouseClicked(x, y, button);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableTexture();
		DrawingUtils.drawTexturedRect(this.guiLeft + 4, this.guiTop + 4, 1, 1, 1, 1, 128, 128, 0.5, 0.5, 0.5, 1);
		RenderSystem.enableTexture();
		this.minecraft.getTextureManager().bindTexture(GUI_BG);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		if (container.inventorySlots.get(0).getStack().getItem() instanceof ShaderItem) {
			RenderSystem.disableTexture();
			for (int i = 0; i < 255; i++) {
				if (isLeftDown) {
					if (
							mouseX == (int) (this.guiLeft + 64 + i / 5.3f) &&
									mouseY >= this.guiTop + 25 &&
									mouseY <= this.guiTop + 30
					) {
						slider1 = i;
					}
					if (
							mouseX == (int) (this.guiLeft + 64 + i / 5.3f) &&
									mouseY >= this.guiTop + 31 &&
									mouseY <= this.guiTop + 36
					) {
						slider2 = i;
					}
					if (
							mouseX == (int) (this.guiLeft + 64 + i / 5.3f) &&
									mouseY >= this.guiTop + 37 &&
									mouseY <= this.guiTop + 42
					) {
						slider3 = i;
					}
				}
				DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) i / 5.3f, this.guiTop + 25, 1, 1, 1, 1, 1, 5, (float) i / 255, 0, 0, 1);
				DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) i / 5.3f, this.guiTop + 31, 1, 1, 1, 1, 1, 5, 0, (float) i / 255, 0, 1);
				DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) i / 5.3f, this.guiTop + 37, 1, 1, 1, 1, 1, 5, 0, 0, (float) i / 255, 1);
			}
			
			//render slider bars
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider1 / 5.3f - 1, this.guiTop + 25, 1, 1, 1, 1, 3, 5, 0.5f, 0.5f, 0.5f, 0.5f);
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider1 / 5.3f, this.guiTop + 25, 1, 1, 1, 1, 1, 5, (float) slider1 / 255, 0, 0, 1);
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider2 / 5.3f - 1, this.guiTop + 31, 1, 1, 1, 1, 3, 5, 0.5f, 0.5f, 0.5f, 0.5f);
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider2 / 5.3f, this.guiTop + 31, 1, 1, 1, 1, 1, 5, 0, (float) slider2 / 255, 0, 1);
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider3 / 5.3f - 1, this.guiTop + 37, 1, 1, 1, 1, 3, 5, 0.5f, 0.5f, 0.5f, 0.5f);
			DrawingUtils.drawTexturedRect(this.guiLeft + 64 + (float) slider3 / 5.3f, this.guiTop + 37, 1, 1, 1, 1, 1, 5, 0, 0, (float) slider3 / 255, 1);
			DrawingUtils.drawTexturedRect(this.guiLeft + 128, this.guiTop + 26, 1, 1, 1, 1, 16, 16, (float) slider1 / 255, (float) slider2 / 255, (float) slider3 / 255, 1);
			
			
			for (PixelStorage.Pixel px : STContainer.handler.tile.image.image) {
				if (
						mouseX >= (int) (this.guiLeft + 16 + px.x * 1.7) &&
								mouseX <= (int) (this.guiLeft + 17 + px.x * 1.7) &&
								mouseY >= (int) (this.guiTop + 29 + px.y * 1.7) &&
								mouseY <= (int) (this.guiTop + 30 + px.y * 1.7) &&
								isLeftDown
				) {
					px.color = new DrawingUtils.ColorHelper(slider1, slider2, slider3);
				}
				DrawingUtils.drawTexturedRect(
						this.guiLeft + 16 + px.x * 1.7,
						this.guiTop + 29 + px.y * 1.7,
						1,
						1,
						1,
						1,
						1.7f,
						1.7f,
						px.color.getRed() / 255f,
						px.color.getGreen() / 255f,
						px.color.getBlue() / 255f,
						1);
			}
			RenderSystem.enableTexture();
		}
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
