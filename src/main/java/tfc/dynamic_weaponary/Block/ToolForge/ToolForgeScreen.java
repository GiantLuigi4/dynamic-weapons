package tfc.dynamic_weaponary.Block.ToolForge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Packet.ImagePacket;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Image.MaterialBasedPixelStorage;
import tfc.dynamic_weaponary.Utils.Image.MaterialVectorImage;
import tfc.dynamic_weaponary.Utils.Image.PixelStorage;
import tfc.dynamic_weaponary.Utils.Tool.Material;

import java.util.ArrayList;
import java.util.List;

public class ToolForgeScreen extends ContainerScreen<ToolForgeContainer> {
	private static final ResourceLocation GUI_BG = new ResourceLocation("dynamic_weaponry", "textures/gui/tool_forge.png");
	PixelStorage.Pixel start = null;
	MaterialBasedPixelStorage.MaterialPixel offsetStart = null;
	MaterialBasedPixelStorage.MaterialPixel end = null;
	ArrayList<MaterialVectorImage.MaterialLine> lines = new ArrayList<>();
	ArrayList<MaterialBasedPixelStorage.MaterialPixel> pixels = new ArrayList<>();
	MaterialVectorImage image = new MaterialVectorImage(16, 16);
	boolean isLeftDown = false;
	
	public ToolForgeScreen(ToolForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, new StringTextComponent("Tool Forge"));

//		System.out.println(titleIn.getString());
		try {
			MaterialBasedPixelStorage pixels = MaterialBasedPixelStorage.fromString(titleIn.getString());
			this.pixels.addAll(pixels.image);
		} catch (Throwable err) {
		}
	}
	
	@Override
	protected void init() {
		super.init();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.minecraft.getTextureManager().bindTexture(GUI_BG);
		
		
		//RenderMaterialListBG
		this.blit(this.guiLeft + 86, this.guiTop + 12, 0, 166 + 2, 84, 58);
		try {
			ItemStack materialStack = this.container.player.getStackInSlot(17);
			if (!materialStack.equals(ItemStack.EMPTY)) {
				this.blit(this.guiLeft + 88, this.guiTop + 12, 84, 173, 74, 18);
				this.itemRenderer.renderItemIntoGUI(materialStack, this.guiLeft + 89, this.guiTop + 13);
			}
			this.minecraft.getTextureManager().bindTexture(GUI_BG);
		} catch (Exception err) {
		}
		
		//RenderNeededMaterials
		
		//RenderCropping
		this.blit(this.guiLeft + 86, this.guiTop + 10, 0, 166, 84, 2);
		this.blit(this.guiLeft + 86, this.guiTop + 10 + 60, 0, 166 + 60, 84, 2);
		
		//RenderMainGUI
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		ItemStack materialStack = this.container.player.getStackInSlot(17);
		Material mat = MaterialList.lookupMaterial(materialStack);
		boolean isValidMaterial = mat.color == 0 && mat.durability == 0 && mat.strength == 0 && mat.weight == 0;
		try {
			if (materialStack.equals(ItemStack.EMPTY)) {
				this.blit(this.guiLeft + 151, this.guiTop + 83, 84 + 18, 173 + 18, 18, 18);
			} else if (isValidMaterial) {
				this.blit(this.guiLeft + 151, this.guiTop + 83, 84, 173 + 18, 18, 18);
			}
		} catch (Exception err) {
			if (isValidMaterial) {
				this.blit(this.guiLeft + 151, this.guiTop + 83, 84, 173 + 18, 18, 18);
			}
		}
		
		//SETUP IMAGE
		image.clear();
		for (MaterialBasedPixelStorage.MaterialPixel px : pixels) {
			image.setPixel(px.x, px.y, px.stack);
		}
		for (MaterialVectorImage.MaterialLine ln : lines) {
			image.addLine(ln.px1, ln.px2);
		}
		try {
			if (start != null) {
				if (end != null) {
					image.addLine(offsetStart, end);
				}
			}
		} catch (Exception err) {
		}
		image.prep();
		
		RenderSystem.disableTexture();
		float pixelSize = 3.36f;
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				if (((x / 2) * 2) == x) {
					if (((y / 2) * 2) == y) {
						DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, 0.5, 0.5, 0.5, 1);
					} else {
						DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, 0.75, 0.75, 0.75, 1);
					}
				} else {
					if (((y / 2) * 2) != y) {
						DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, 0.5, 0.5, 0.5, 1);
					} else {
						DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, 0.75, 0.75, 0.75, 1);
					}
				}
				if (mouseX >= this.guiLeft + 25 + (x * pixelSize) &&
						mouseX <= this.guiLeft + 25 + (x * pixelSize) + pixelSize &&
						mouseY >= this.guiTop + 14 + (y * pixelSize) &&
						mouseY <= this.guiTop + 14 + (y * pixelSize) + pixelSize /*&&
						!materialStack.equals(ItemStack.EMPTY)*/
				) {
					DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, 0.25, 0.25, 0.25, 1);
					if (start == null && isLeftDown) {
						start = new PixelStorage.Pixel(x, y, new DrawingUtils.ColorHelper(mat.color));
					}
					if (isLeftDown) {
						float offX = 1;
						if ((x - start.x) != 0) {
							offX = (x - start.x);
							offX /= Math.abs(offX);
						}
						float offY = 1;
						if ((y - start.y) != 0) {
							offY = (y - start.y);
							offY /= Math.abs(offY);
						}
						float offX2 = offX;
						float offY2 = offY;
						if (offX2 >= 0) {
							offX2 = 0;
						}
						if (offY2 >= 0) {
							offY2 = 0;
						}
						offsetStart = new MaterialBasedPixelStorage.MaterialPixel(start.x - Math.round(offX2), start.y - Math.round(offY2), materialStack);
						if (offX <= 0) {
							offX = 0;
						}
						if (offY <= 0) {
							offY = 0;
						}
						end = new MaterialBasedPixelStorage.MaterialPixel(x + Math.round(offX), y + Math.round(offY), materialStack);
					}
					if (!isLeftDown && start != null) {
						lines.add(
								new MaterialVectorImage.MaterialLine(
										offsetStart,
										end
								)
						);
						start = null;
					}
				}
				try {
					DrawingUtils.ColorHelper px = image.getPixel(x, y);
					if (px.getAlpha() != 0 && (px.getRed() != 0 || px.getGreen() != 0 || px.getBlue() != 0)) {
						DrawingUtils.drawTexturedRect(this.guiLeft + 25 + (x * pixelSize), this.guiTop + 14 + (y * pixelSize), 0, 0, 1, 1, pixelSize, pixelSize, px.getRed() / 255f, px.getGreen() / 255f, px.getBlue() / 255f, 1);
					}
				} catch (Exception err) {
				}
//				try {
//					if (x==start.x&&y==start.y) {
//						DrawingUtils.drawTexturedRect(this.guiLeft+25+(x*pixelSize),this.guiTop+14+(y*pixelSize),0,0,1,1,pixelSize,pixelSize,1,0,0,1);
//					}
//					if (x==end.x&&y==end.y) {
//						DrawingUtils.drawTexturedRect(this.guiLeft+25+(x*pixelSize),this.guiTop+14+(y*pixelSize),0,0,1,1,pixelSize,pixelSize,0,0,1,1);
//					}
//				} catch (Exception err) {}
			}
		}
		RenderSystem.enableTexture();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	public void onClose() {
		super.onClose();
		this.container.tile.image = image.toString();
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
		}
	}
	
	@Override
	protected void renderTooltip(ItemStack stack, int x, int y) {
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(stack);
		List<String> tooltip = this.getTooltipFromItem(stack);
		Material mat = MaterialList.lookupMaterial(stack);
		boolean isValidMaterial = !(mat.color == 0 || mat.durability == 0 && mat.strength == 0 && mat.weight == 0);
		if (isValidMaterial) {
			tooltip.add("Weight:" + (mat.weight + 3));
			tooltip.add("Strength:" + mat.strength);
			tooltip.add("Durability:" + mat.durability);
			tooltip.add("Color:" + mat.color);
		}
		this.renderTooltip(tooltip, x, y, (font == null ? this.font : font));
		net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
	}
	
	public void renderTooltip(List<String> p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_, FontRenderer font) {
		DrawingUtils.drawHoveringText(p_renderTooltip_1_, p_renderTooltip_2_, p_renderTooltip_3_, width, height, -1, font);
//		if (true && !p_renderTooltip_1_.isEmpty()) {
//			RenderSystem.disableRescaleNormal();
//			RenderSystem.disableDepthTest();
//			int i = 0;
//
//			for(String s : p_renderTooltip_1_) {
//				int j = this.font.getStringWidth(s);
//				if (j > i) {
//					i = j;
//				}
//			}
//
//			int l1 = p_renderTooltip_2_ + 12;
//			int i2 = p_renderTooltip_3_ - 12;
//			int k = 8;
//			if (p_renderTooltip_1_.size() > 1) {
//				k += 2 + (p_renderTooltip_1_.size() - 1) * 10;
//			}
//
//			if (l1 + i > this.width) {
//				l1 -= 28 + i;
//			}
//
//			if (i2 + k + 6 > this.height) {
//				i2 = this.height - k - 6;
//			}
//
//			this.setBlitOffset(300);
//			this.itemRenderer.zLevel = 300.0F;
//			int l = -267386864;
//			this.fillGradient(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
//			this.fillGradient(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
//			this.fillGradient(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
//			this.fillGradient(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
//			this.fillGradient(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
//			int i1 = 1347420415;
//			int j1 = 1344798847;
//			this.fillGradient(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
//			this.fillGradient(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
//			this.fillGradient(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
//			this.fillGradient(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);
//			MatrixStack matrixstack = new MatrixStack();
//			IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
//			matrixstack.translate(0.0D, 0.0D, (double)this.itemRenderer.zLevel);
//			Matrix4f matrix4f = matrixstack.getLast().getMatrix();
//
//			for(int k1 = 0; k1 < p_renderTooltip_1_.size(); ++k1) {
//				String s1 = p_renderTooltip_1_.get(k1);
//				if (s1 != null) {
//					int color=0;
//					if (s1.startsWith("Color:")) {
//						String c=s1.substring(("Color:").length()+1);
//						try {
//							color=Integer.parseInt(c);
//						} catch (Exception err) {
//							DynamicWeapons.LOGGER.log(Level.INFO,"H");
//						}
//					}
//					this.font.renderString(s1, (float)l1, (float)i2, color, true, matrix4f, irendertypebuffer$impl, false, 0, 15728880);
//				}
//
//				if (k1 == 0) {
//					i2 += 2;
//				}
//
//				i2 += 10;
//			}
//
//			irendertypebuffer$impl.finish();
//			this.setBlitOffset(0);
//			this.itemRenderer.zLevel = 0.0F;
//			RenderSystem.enableDepthTest();
//			RenderSystem.enableRescaleNormal();
//		}
	}
	
	@Override
	public boolean mouseReleased(double x, double y, int button) {
		if (button == 0) {
			isLeftDown = false;
			try {
				DynamicWeapons.INSTANCE.sendToServer(new ImagePacket(image.toString()));
				container.tile.image = image.toString();
			} catch (Exception ignored) {
			}
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
}
