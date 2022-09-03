package tfc.dynamicweaponry.screens.tool;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.access.IHoldADataLoader;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.loading.MaterialLoader;
import tfc.dynamicweaponry.network.DynamicWeaponryNetworkRegistry;
import tfc.dynamicweaponry.network.gui.SendBlockPosPacket;
import tfc.dynamicweaponry.network.gui.SetPixelsPacket;
import tfc.dynamicweaponry.tool.Tool;
import tfc.dynamicweaponry.tool.ToolLayer;
import tfc.dynamicweaponry.tool.requirements.HandleRequirement;
import tfc.dynamicweaponry.tool.requirements.Requirement;
import tfc.dynamicweaponry.util.ExpandedColor;
import tfc.dynamicweaponry.util.Point;

public class ToolForgeScreen extends AbstractContainerScreen<ToolForgeContainer> {
	private final ResourceLocation ASSET = new ResourceLocation("dynamic_weaponry:textures/gui/gui.png");
	private final ResourceLocation ASSET_ALT = new ResourceLocation("dynamic_weaponry:textures/gui/gui_alt.png");
	
	private static final Requirement[] renderableRequirements = new Requirement[]{
			HandleRequirement.INSTANCE
	};
	
	public ToolForgeScreen(ToolForgeContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		DynamicWeaponryNetworkRegistry.NETWORK_INSTANCE.sendToServer(new SendBlockPosPacket(true, null));
	}
	
	@Override
	protected void init() {
		this.imageHeight = 222;
		this.imageWidth = 176;
		this.inventoryLabelY = this.imageHeight - 94;
		super.init();
	}
	
	int selectedPixel = -1;
	int selectedLayer = 0;
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBackground(pPoseStack, 0);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		RenderSystem.setShaderTexture(0, ASSET_ALT);
		RenderSystem.setShaderTexture(0, ASSET);
		int i = (this.width - 176) / 2;
		int j = (this.height - 222) / 2;
		this.blit(pPoseStack, i, j, 0, 0, 176, 222);
		RenderSystem.enableBlend();
		for (Slot slot : menu.slots) {
			boolean isMaterial = slot.getItem() != ItemStack.EMPTY;
			
			Material material = null;
			if (isMaterial) {
				for (ResourceLocation key : DynamicWeaponry.clientAssetLoader.getMaterialHolder().keys()) {
					material = DynamicWeaponry.clientAssetLoader.getMaterialHolder().get(key);
					if (material.isValid(slot.getItem()))
						break;
					material = null;
				}
				if (material == null) isMaterial = false;
			}
			
			if (isMaterial || selectedSlot == slot.index) {
				float[] c = getSlotColor(material, isMaterial, slot);
				RenderSystem.setShaderColor(c[0], c[1], c[2], c[3]);
				this.blit(pPoseStack, i + slot.x - 1, j + slot.y - 1, 176, 0, 18, 18);
			}
		}
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		int sx = i + 38;
		int sy = j + 28;
//		int width = 16 * 4 + 2;
//		int sx = (i + imageWidth / 2) - width / 2 + 1;
//		int sy = (j + inventoryLabelY / 2) - width / 2 + 1;
		this.blit(pPoseStack, sx - 1, sy - 1, 176, 18, 16 * 4 + 2, 16 * 4 + 2);
		if (menu.blockEntity != null) {
			Tool tool = menu.blockEntity.getTool(Minecraft.getInstance().level);
			int fontHeight = font.lineHeight + 2;
			// TODO: clean this up and decide upon how to do this
			for (int i1 = 0; i1 < tool.getLayers().length; i1++) {
				int left = sx + 16 * 4 + 30;
				int top = sy + i1 * fontHeight;
				
				int width = fontHeight;
				
				if (pMouseY > top - 3 && pMouseY < top + fontHeight - 2) {
					if (pMouseX >= left - 1 && pMouseX < left + width) {
						selectedLayer = i1;
					}
				}
				
				float v = 0.5f / (selectedLayer == i1 ? 1 : 2);
				RenderSystem.setShaderColor(v, v, v, 1);
				blit(
						pPoseStack, left - 1, top - 2,
						width + 1, fontHeight,
						18, 9, 4, 4,
						256, 256
				);
				v = 0.75f / (selectedLayer == i1 ? 1 : 2);
				RenderSystem.setShaderColor(v, v, v, 1);
				blit(
						pPoseStack, left, top - 1,
						width - 1, fontHeight - 2,
						18, 9, 4, 4,
						256, 256
				);
				ExpandedColor color = new ExpandedColor(64, 64, 64);
				if (selectedLayer != i1) color = new ExpandedColor(192, 192, 192);
				
				this.font.draw(
						pPoseStack, "" + i1,
						left + 1, top,
						color.getRGB()
				);
				
				RenderSystem.setShaderTexture(0, ASSET);
			}
			RenderSystem.enableBlend();
			
			selectedPixel = -1;
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					int rgb = tool.getImage().getRGB(x, y);
					ExpandedColor color = new ExpandedColor(rgb, true);
					int left = sx + x * 4;
					int top = sy + y * 4;
					
					if (color.getAlpha() != 0) {
						ToolLayer layer = tool.getLayers()[selectedLayer];
						Material m = layer.get(x, 15 - y);
						float scl = 0.25f;
						if (m != null) {
							if (m.clientMaterial != null) color = new ExpandedColor(m.clientMaterial.color);
							else color = new ExpandedColor(0, 0, 0);
							scl = 1;
						}
						RenderSystem.setShaderColor(color.getRed() / 255f * scl, color.getGreen() / 255f * scl, color.getBlue() / 255f * scl, color.getAlpha() / 255f);
						this.blit(pPoseStack, left, top, 176 + 18, 14, 4, 4);
					}
					
					if (false) {
						Requirement requirement = renderableRequirements[0];
						float r = 0, g = 0, b = 0;
						boolean inverse = true;
						if (!requirement.test(tool)) {
							r = 1;
							inverse = false;
						} else g = 1;
						for (Point point : requirement.requiredPoints(tool)) {
							if (point.x == x && point.y == y) {
								boolean foundAny = false;
								for (ToolLayer toolLayer : tool.getLayers())
									if (foundAny = toolLayer.get(x, 15 - y) != null)
										break;
								RenderSystem.setShaderColor(r, g, b, 0.5f);
								if (inverse) foundAny = !foundAny;
								if (!foundAny)
									this.blit(pPoseStack, left, top, 176 + 18, 14, 4, 4);
							}
						}
					}
					
					if (left - 1 < pMouseX && top - 1 < pMouseY) {
						if (left + 4 > pMouseX && top + 4 > pMouseY) {
							int b = Math.max(color.getRed(), Math.max(color.getBlue(), color.getGreen()));
							if (b < 128) RenderSystem.setShaderColor(1, 1, 1, 0.5f);
							else RenderSystem.setShaderColor(0, 0, 0, 0.5f);
							selectedPixel = x * 16 + y;
							this.blit(pPoseStack, left, top, 176 + 18, 14, 4, 4);
						}
					}
				}
			}
		}
	}
	
	protected float[] getSlotColor(Material material, boolean isMaterial, Slot slot) {
		float[] c;
		if (!isMaterial)
			return new float[]{1, 0, 0, 1};
		if (selectedSlot == slot.index)
			return new float[]{0, 1, 0, 1};
		ExpandedColor color;
		if (material.clientMaterial == null) color = new ExpandedColor(0, 0, 0);
		else color = new ExpandedColor(material.clientMaterial.color);
		return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
	}
	
	private int selectedSlot = -1;
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (selectedPixel == -1) return super.mouseClicked(pMouseX, pMouseY, pButton);
		try {
			Tool tool = menu.blockEntity.getTool(Minecraft.getInstance().level);
			int x = selectedPixel / 16;
			int y = selectedPixel - (x * 16);
			MaterialLoader loader = ((IHoldADataLoader) menu.blockEntity.getLevel()).myLoader();
			ItemStack stack = menu.slots.get(selectedSlot).getItem();
			Material material = null;
			for (ResourceLocation key : loader.getMaterialHolder().keys()) {
				material = loader.getMaterialHolder().get(key);
				if (material.isValid(stack)) break;
				material = null;
			}
			ResourceLocation name = material == null ? null : material.regName;
			DynamicWeaponryNetworkRegistry.NETWORK_INSTANCE.sendToServer(
					new SetPixelsPacket(
							selectedLayer, new int[]{selectedPixel}, name
					)
			);
			tool.getLayers()[selectedLayer].set(x, 15 - y, material);
			tool.markDirty();
		} catch (Throwable ignored) {
			ignored.printStackTrace();
		}
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	
	@Override
	protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
		if (pSlotId == -999) return;
		if (pMouseButton == 0)
			selectedSlot = pSlotId;
	}
	
	public void renderBackground(PoseStack pPoseStack, int pVOffset) {
		assert this.minecraft != null;
		if (this.minecraft.level != null) {
			this.fillGradient(pPoseStack, 0, 0, this.width, this.height, -1072689136, -804253680);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundDrawnEvent(this, pPoseStack));
		} else {
			this.renderDirtBackground(pVOffset);
		}
	}
}
