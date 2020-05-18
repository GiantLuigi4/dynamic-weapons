package tfc.dynamic_weaponary.block.ToolForge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import tfc.dynamic_weaponary.Deffered_Registry.Items;
import tfc.dynamic_weaponary.MaterialList;
import tfc.dynamic_weaponary.Utils.DrawingUtils;
import tfc.dynamic_weaponary.Utils.Material;
import tfc.dynamic_weaponary.Utils.PixelStorage;
import tfc.dynamic_weaponary.Utils.VectorImage;

import java.util.ArrayList;
import java.util.List;

public class ToolForgeScreen extends ContainerScreen<ToolForgeContainer> {
	private static final ResourceLocation GUI_BG = new ResourceLocation("dynamic_weaponry", "textures/gui/tool_forge.png");
	PixelStorage.Pixel start = null;
	PixelStorage.Pixel offsetStart = null;
	PixelStorage.Pixel end = null;
	ArrayList<VectorImage.line> lines = new ArrayList<>();
	VectorImage image = new VectorImage(16, 16, PixelStorage.fromString("0,0,-16712192;1,0,-16712192;2,0,-16712192;3,0,-16712192;4,0,-16712192;5,0,-16712192;6,0,-16712192;7,0,-16712192;8,0,-16712192;9,0,-16712192;10,0,-16712192;11,0,-16712192;12,0,-16712192;13,0,-16712192;14,0,-16712192;15,0,-16448002;0,1,-16712192;1,1,-16712192;2,1,-16712192;3,1,-16712192;4,1,-16712192;5,1,-16712192;6,1,-16712192;7,1,-16712192;8,1,-16712192;9,1,-16712192;10,1,-16712192;11,1,-16712192;12,1,-16712192;13,1,-16712192;14,1,-16712192;15,1,-16776962;0,2,-16712192;1,2,-16712192;2,2,-16712192;3,2,-16712192;4,2,-16712192;5,2,-16712192;6,2,-16712192;7,2,-16712192;8,2,-16712192;9,2,-16712192;10,2,-16712192;11,2,-16712192;12,2,-16712192;13,2,-16712192;14,2,-16712192;15,2,-16776962;0,3,-16712192;1,3,-16712192;2,3,-16712192;3,3,-16712192;4,3,-16712192;5,3,-16712192;6,3,-16712192;7,3,-16712192;8,3,-16712192;9,3,-16712192;10,3,-16712192;11,3,-16712192;12,3,-16712192;13,3,-16712192;14,3,-16712192;15,3,-16776962;0,4,-16712192;1,4,-16712192;2,4,-16712192;3,4,-16712192;4,4,-16712192;5,4,-16712192;6,4,-16712192;7,4,-16712192;8,4,-16712192;9,4,-16712192;10,4,-16712192;11,4,-16712192;12,4,-16712192;13,4,-16712192;14,4,-16712192;15,4,-16776962;0,5,-16712192;1,5,-16712192;2,5,-16712192;3,5,-16712192;4,5,-16712192;5,5,-16712192;6,5,-16712192;7,5,-16712192;8,5,-16712192;9,5,-16712192;10,5,-16712192;11,5,-16712192;12,5,-16712192;13,5,-16712192;14,5,-16712192;15,5,-16776962;0,6,-16712192;1,6,-16712192;2,6,-16712192;3,6,-16712192;4,6,-16712192;5,6,-16712192;6,6,-16712192;7,6,-16712192;8,6,-16712192;9,6,-16712192;10,6,-16712192;11,6,-16712192;12,6,-16712192;13,6,-16712192;14,6,-16712192;15,6,-16776962;0,7,-16712192;1,7,-16712192;2,7,-16712192;3,7,-16712192;4,7,-16712192;5,7,-16712192;6,7,-16712192;7,7,-16712192;8,7,-16712192;9,7,-16712192;10,7,-16712192;11,7,-16712192;12,7,-16712192;13,7,-16712192;14,7,-16712192;15,7,-16776962;0,8,-16712192;1,8,-16712192;2,8,-16712192;3,8,-16712192;4,8,-16712192;5,8,-16712192;6,8,-16712192;7,8,-16712192;8,8,-16712192;9,8,-16712192;10,8,-16712192;11,8,-16712192;12,8,-16712192;13,8,-16712192;14,8,-16712192;15,8,-16776962;0,9,-16712192;1,9,-16712192;2,9,-16712192;3,9,-16712192;4,9,-16712192;5,9,-16712192;6,9,-16712192;7,9,-16712192;8,9,-16712192;9,9,-16712192;10,9,-16712192;11,9,-16712192;12,9,-16712192;13,9,-16712192;14,9,-16712192;15,9,-16776962;0,10,-16712192;1,10,-16712192;2,10,-16712192;3,10,-16712192;4,10,-16712192;5,10,-16712192;6,10,-16712192;7,10,-16712192;8,10,-16712192;9,10,-16712192;10,10,-16712192;11,10,-16712192;12,10,-16712192;13,10,-16712192;14,10,-16712192;15,10,-16776962;0,11,-16712192;1,11,-16712192;2,11,-16712192;3,11,-16712192;4,11,-16712192;5,11,-16712192;6,11,-16712192;7,11,-16712192;8,11,-16712192;9,11,-16712192;10,11,-16712192;11,11,-16712192;12,11,-16712192;13,11,-16712192;14,11,-16712192;15,11,-16776962;0,12,-16712192;1,12,-16712192;2,12,-16712192;3,12,-16712192;4,12,-16712192;5,12,-16712192;6,12,-16712192;7,12,-16712192;8,12,-16712192;9,12,-16712192;10,12,-16712192;11,12,-16712192;12,12,-16712192;13,12,-16712192;14,12,-16712192;15,12,-16776962;0,13,-16712192;1,13,-16712192;2,13,-16712192;3,13,-16712192;4,13,-16712192;5,13,-16712192;6,13,-16712192;7,13,-16712192;8,13,-16712192;9,13,-16712192;10,13,-16712192;11,13,-16712192;12,13,-16712192;13,13,-16712192;14,13,-16712192;15,13,-16776962;0,14,-16712192;1,14,-16712192;2,14,-16712192;3,14,-16712192;4,14,-16712192;5,14,-16712192;6,14,-16712192;7,14,-16712192;8,14,-16712192;9,14,-16712192;10,14,-16712192;11,14,-16712192;12,14,-16712192;13,14,-16712192;14,14,-16712192;15,14,-16776962;0,15,-16712192;1,15,-16712192;2,15,-16712192;3,15,-16712192;4,15,-16712192;5,15,-16712192;6,15,-16712192;7,15,-16712192;8,15,-16712192;9,15,-16712192;10,15,-16712192;11,15,-16712192;12,15,-16712192;13,15,-16712192;14,15,-16712192;15,15,-16776962;"));
	boolean isLeftDown = false;
	
	public ToolForgeScreen(ToolForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
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
		for (VectorImage.line ln : lines) {
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
						mouseY <= this.guiTop + 14 + (y * pixelSize) + pixelSize &&
						!materialStack.equals(ItemStack.EMPTY)
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
						offsetStart = new PixelStorage.Pixel(start.x - Math.round(offX2), start.y - Math.round(offY2), new DrawingUtils.ColorHelper(mat.color));
						if (offX <= 0) {
							offX = 0;
						}
						if (offY <= 0) {
							offY = 0;
						}
						end = new PixelStorage.Pixel(x + Math.round(offX), y + Math.round(offY), new DrawingUtils.ColorHelper(mat.color));
					}
					if (!isLeftDown && start != null) {
						lines.add(
								new VectorImage.line(
										offsetStart,
										end,
										materialStack.getItem(),
										materialStack.getItem()
								)
						);
						start = null;
					}
				}
				try {
					DrawingUtils.ColorHelper px = image.getPixel(x, y);
					if (px.getAlpha() != 0) {
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
		ItemStack stack = new ItemStack(Items.TOOL.get());
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putString("image", image.toString());
		this.container.tile.image = image;
		this.container.player.setInventorySlotContents(17, stack);
		super.onClose();
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
		if (MaterialList.materialHashMap.containsKey(stack.getItem().getRegistryName().toString())) {
			tooltip.add("Weight:" + MaterialList.materialHashMap.get(stack.getItem().getRegistryName().toString()).weight);
			tooltip.add("Strength:" + MaterialList.materialHashMap.get(stack.getItem().getRegistryName().toString()).strength);
			tooltip.add("Durability:" + MaterialList.materialHashMap.get(stack.getItem().getRegistryName().toString()).durability);
			tooltip.add("Color:" + MaterialList.materialHashMap.get(stack.getItem().getRegistryName().toString()).color);
//			tooltip.add("Durability:"+MaterialList.materialHashMap.get(stack.getItem().getRegistryName().toString()).durability);
		}
		this.renderTooltip(tooltip, x, y, (font == null ? this.font : font));
		net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
	}
	
	public void renderTooltip(List<String> p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_, FontRenderer font) {
		DrawingUtils.drawHoveringText(p_renderTooltip_1_, p_renderTooltip_2_, p_renderTooltip_3_, width, height, -1, font);
//		if (false && !p_renderTooltip_1_.isEmpty()) {
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
				ToolForgeContainer.tile.image = image;
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
