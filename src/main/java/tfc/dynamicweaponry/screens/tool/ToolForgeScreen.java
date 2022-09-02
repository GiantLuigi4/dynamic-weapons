package tfc.dynamicweaponry.screens.tool;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.loading.Material;
import tfc.dynamicweaponry.network.DynamicWeaponryNetworkRegistry;
import tfc.dynamicweaponry.network.gui.SendBlockPosPacket;
import tfc.dynamicweaponry.util.ExpandedColor;

public class ToolForgeScreen extends AbstractContainerScreen<ToolForgeContainer> {
	private final ResourceLocation ASSET = new ResourceLocation("dynamic_weaponry:textures/gui/gui.png");
	private final ResourceLocation ASSET_ALT = new ResourceLocation("dynamic_weaponry:textures/gui/gui_alt.png");
	
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
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBackground(pPoseStack, 0);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		RenderSystem.setShaderTexture(0, ASSET_ALT);
		RenderSystem.setShaderTexture(0, ASSET);
		int i = (this.width - 176) / 2;
		int j = (this.height - 222) / 2;
		this.blit(pPoseStack, i, j, 0, 0, 176, 222);
		for (Slot slot : menu.slots) {
			boolean isMaterial = slot.getItem() != ItemStack.EMPTY;
			
			Material material = null;
			if (isMaterial) {
				for (ResourceLocation key : DynamicWeaponry.clientAssetLoader.getMaterialHolder().keys()) {
					material = DynamicWeaponry.clientAssetLoader.getMaterialHolder().get(key);
					if (material.isValid(slot.getItem())) {
						break;
					}
					material = null;
				}
				if (material == null) isMaterial = false;
			}
			
			if (isMaterial || selectedSlot == slot.index) {
				if (!isMaterial)
					RenderSystem.setShaderColor(1, 0, 0, 1);
				else if (selectedSlot == slot.index)
					RenderSystem.setShaderColor(0, 1, 0, 1);
				else {
					ExpandedColor color = new ExpandedColor(material.clientMaterial.color);
					RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
				}
				RenderSystem.enableBlend();
				this.blit(pPoseStack, i + slot.x - 1, j + slot.y - 1, 176, 0, 18, 18);
			}
		}
	}
	
	private int selectedSlot = -1;
	
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
