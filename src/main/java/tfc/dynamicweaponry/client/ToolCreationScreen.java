package tfc.dynamicweaponry.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.assortedutils.API.gui.screen.SimpleContainerScreen;
import com.tfc.assortedutils.utils.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import tfc.dynamicweaponry.DynamicWeaponry;
import tfc.dynamicweaponry.block.ToolForgeContainer;
import tfc.dynamicweaponry.client.renderer.Shading;
import tfc.dynamicweaponry.data.*;
import tfc.dynamicweaponry.item.tool.DynamicTool;
import tfc.dynamicweaponry.item.tool.MaterialPoint;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.item.tool.ToolComponent;
import tfc.dynamicweaponry.network.PaintPixelPacket;
import tfc.dynamicweaponry.network.PaintToolPacket;
import tfc.dynamicweaponry.network.ToolPacket;
import tfc.dynamicweaponry.registry.Registry;
import tfc.dynamicweaponry.utils.Line;
import tfc.dynamicweaponry.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class ToolCreationScreen extends SimpleContainerScreen<ToolForgeContainer> {
	public static final ItemStack defaultTool = new ItemStack(Registry.DYNAMIC_TOOL.get());
	
	private final ArrayList<ItemSlot> slots = new ArrayList<>();
	public String currentTool = "";
	public String currentPart = "";
	public Tool tool = new Tool(defaultTool);
	
	public final TextFieldWidget output;
	
	public int drawTool = -1;
	
	public Point start;
	
	public boolean lMouseDown = false;
	public boolean rMouseDown = false;
	public ItemSlot selectedSlot = null;
	
	public ToolCreationScreen(ITextComponent titleIn, Minecraft minecraft, ContainerType<?> type) {
		super(titleIn, minecraft, (ContainerType<ToolForgeContainer>) type);
		
		output =
				new TextFieldWidget(
						font == null ? minecraft.fontRenderer : font,
						this.width / 2 + 277, (this.height / 2) + 48,
						58, 18,
						new StringTextComponent(tool.serialize().toString())
				);
	}
	
	public ToolCreationScreen(ITextComponent titleIn, ContainerType<ToolForgeContainer> type) {
		super(titleIn, type);
		
		output =
				new TextFieldWidget(
						font == null ? minecraft.fontRenderer : font,
						this.width / 2 + 277, (this.height / 2) + 48,
						58, 18,
						new StringTextComponent(tool.serialize().toString())
				);
	}
	
	@Override
	public void deseralize(CompoundNBT nbt) {
		this.slots.clear();
		
		super.deseralize(nbt);
		
		if (minecraft != null && minecraft.player != null) {
			for (int i = 0; i < 9; i++) {
				ItemSlot slot = new ItemSlot(minecraft.player.inventory, i, i * 18 + 8, 182);
				Material material = DataLoader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
				if (material != null) {
					ClientMaterialInfo materialInfo = AssetLoader.INSTANCE.getMaterial(material.item);
					if (materialInfo != null) slot.color = new Color(materialInfo.color);
					else slot.color = new Color(255, 255, 255);
				}
				slots.add(slot);
			}
			
			for (int i = 9; i < 36; i++) {
				ItemSlot slot = new ItemSlot(minecraft.player.inventory, i, (i % 9) * 18 + 8, 106 + (i / 9) * 18);
				Material material = DataLoader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
				if (material != null) {
					ClientMaterialInfo materialInfo = AssetLoader.INSTANCE.getMaterial(material.item);
					if (materialInfo != null) slot.color = new Color(materialInfo.color);
					else slot.color = new Color(255, 255, 255);
				}
				slots.add(slot);
			}
		}

//		int x = -32;
//		int y = 0;
		int index = 0;
		for (ResourceLocation location : DataLoader.INSTANCE.toolTypes.keySet()) {
			ResourceLocation typeImage = new ResourceLocation(
					location.getNamespace(),
					"textures/gui/tool_types/" + location.getPath() + ".png"
			);
			minecraft.getTextureManager().bindTexture(typeImage);
			slots.add(
					new ToolComponentSlot(index++, 0, typeImage, location, true)
			);
		}
		
		ItemStack stack = new ItemStack(Registry.DYNAMIC_TOOL.get());
		CompoundNBT nbt1 = stack.getOrCreateTag();
		nbt1.put("tool_info", nbt.getCompound("tool"));
		tool = new Tool(stack);
		currentTool = tool.name;
		
		index = 0;
		{
			ToolType type = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(tool.name));
			ToolPart[] parts = type.getParts();
			
			ArrayList<ResourceLocation> locations1 = new ArrayList<>();
			
			for (ToolPart part : parts) {
				if (part.type != null) {
					locations1.add(part.type.name);
				}
			}
			
			for (ResourceLocation location : locations1) {
				ResourceLocation typeImage = new ResourceLocation(
						location.getNamespace(),
						"textures/gui/part_types/" + location.getPath() + ".png"
				);
				
				minecraft.getTextureManager().bindTexture(typeImage);
				slots.add(
						new ToolComponentSlot(index++, 128, typeImage, location, false)
				);
			}
		}
	}
	
	public boolean isSwitcherOpen = false;
	public boolean isToolSwitcher = false;
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	public int switcherIndex = 0;
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (selectedSlot != null)
			slots.get(selectedSlot.index).color = selectedSlot.color;
		
		matrixStack.push();
		super.renderBackground(matrixStack);
		matrixStack.translate(0, -40, 0);
		int i = (this.width - 248) / 2;
//		int i1 = i;
//		i += (i-Math.max(9, i - 128)) / 2;
		int j = (this.height - 166) / 2;
		
		drawBackground(matrixStack,
				Math.max(9, i - 128) - 9, j,
				i - Math.max(9, i - 128) + 9, 246
		);
//		drawBackground(matrixStack,
//				Math.max(9, i1 - 128) - 9 + i-i1, j,
//				i1-Math.max(9, i1 - 128) + 9, 246
//		);
		drawBackground(matrixStack,
				i, j,
				(248 - 72), 166
		);
		drawBackground(matrixStack,
				i + (248 - 73), j,
				100, 246
		);
		
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		this.blit(matrixStack, i, j + 160, 0, 80, 248, (166 - 80));
		matrixStack.pop();
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		matrixStack.push();
		matrixStack.translate(0, -40, 0);
		
		drawString(matrixStack, font, "Draw Tool: " + (drawTool == -1 ? "pixel" : drawTool == 0 ? "rectangle" : "line"),
				i + 180, j + 60, new Color(255, 255, 255).getRGB());
		
		{
			int r = 128;
			int g = 255;
			int b = 128;
			try {
				drawString(matrixStack, font, "Weight: " + Math.round(tool.getWeight() * 100) / 100f,
						i + 180, j + 70, new Color(r, g, b).getRGB());
				drawString(matrixStack, font, "Attack: " + Math.round(tool.getDamage() * 100) / 100f,
						i + 180, j + 80, new Color(r, g, b).getRGB());
				drawString(matrixStack, font, "Efficiency: " + Math.round(tool.getEfficiency() * 10) / 10f,
						i + 180, j + 90, new Color(r, g, b).getRGB());
				if (!tool.isBow()) {
					if (tool.getAttackSpeed() < 0) {
						drawString(matrixStack, font, "Cooldown: Infinite",
								i + 180, j + 100, new Color(r, g, b).getRGB());
						drawString(matrixStack, font, "Speed: None",
								i + 180, j + 110, new Color(r, g, b).getRGB());
					} else {
						drawString(matrixStack, font, "Cooldown: " + (DynamicWeaponry.maxAttackSpeed - (Math.round(tool.getAttackSpeed() * 100) / 100f)),
								i + 180, j + 100, new Color(r, g, b).getRGB());
						drawString(matrixStack, font, "Speed: " + Math.round((tool.getAttackSpeed()) * 100) / 100f,
								i + 180, j + 110, new Color(r, g, b).getRGB());
					}
				}
				drawString(matrixStack, font, "Durability: " + Math.round(tool.getDurability()),
						i + 180, j + 120, new Color(r, g, b).getRGB());
				
				if (tool.isBow()) {
					drawString(matrixStack, font, "Draw Speed: " + Math.abs(Math.round(((1 - tool.getDrawSpeed()) * 4) * 100) / 100f),
							i + 180, j + 100, new Color(r, g, b).getRGB());
					float f = (1f / (1 - (tool.getDrawSpeed())));
					f *= Math.max(0.1, 1 - (1f / tool.getEfficiency()));
					f *= 4;
					f = Math.min(f, 3);
					drawString(matrixStack, font, "Shoot Force: " + Math.abs(Math.round(((f)) * 100) / 100f),
							i + 180, j + 110, new Color(r, g, b).getRGB());
				} else {
					String[] types = new String[]{
							net.minecraftforge.common.ToolType.PICKAXE.getName(),
							net.minecraftforge.common.ToolType.AXE.getName(),
							net.minecraftforge.common.ToolType.SHOVEL.getName(),
							net.minecraftforge.common.ToolType.HOE.getName(),
							"sword"
					};
					int index = 0;
					for (String type : types) {
						BlockState state = type.equals("pickaxe") ? Blocks.STONE.getDefaultState() : type.equals("axe") ? Blocks.OAK_PLANKS.getDefaultState() : type.equals("shovel") ? Blocks.DIRT.getDefaultState() : type.equals("hoe") ? Blocks.OAK_LEAVES.getDefaultState() : type.equals("sword") ? Blocks.COBWEB.getDefaultState() : Blocks.BEDROCK.getDefaultState();
						String typeName = type.substring(0, 1).toUpperCase() + type.substring(1);
						float lvl = Math.round(tool.calcHarvestLevel(type, state) * 100f) / 100f;
						if (lvl >= 0.01f) {
							drawString(matrixStack, font, typeName + " Level: " + lvl,
									i + 180, j + 130 + (index++ * 10), new Color(r, g, b).getRGB());
//						drawString(matrixStack, font, typeName + " Speed: " + lvl,
//								i + 180, j + 130 + (index++ * 10), new Color(r, g, b).getRGB());
						}
					}
				}
			} catch (Throwable ignored) {
				System.out.println(tool);
			}
		}
		
		if (this.buttons.isEmpty()) {
//			this.buttons.add(
//					new Button(
//							this.width / 2 + 63, j - 30,
//							60, 20,
//							(new TranslationTextComponent("tool_type." + new ResourceLocation(tool.name).getNamespace() + "." + new ResourceLocation(tool.name).getPath().replace("/", "."))),
//							this::cycleTool
//					)
//			);

//			this.buttons.add(
//					new Button(
//							this.width / 2 + 63, j - 30,
//							60, 20,
//							(new TranslationTextComponent("button.dynamic_weaponry.toggle_tool_switcher")),
//							(button) -> {
//								isSwitcherOpen = !isSwitcherOpen;
//								isToolSwitcher = true;
//								ResourceLocation[] locations = DataLoader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
//								for (int i1 = 0; i1 < locations.length; i1++) {
//									if (locations[i1].toString().equals(tool.name)) {
//										switcherIndex = i1;
//									}
//								}
//							}
//					)
//			);
//
//			Button button = new Button(
//					this.width / 2 + 63, j - 10,
//					60, 20,
//					(new TranslationTextComponent("button.dynamic_weaponry.toggle_part_switcher")),
////					this::cyclePart
//					(button1) -> {
//						ToolType type = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(tool.name));
//						if (type == null) return;
//						ToolPart[] parts = type.getParts();
//
//						ArrayList<ResourceLocation> locations1 = new ArrayList<>();
//
//						for (ToolPart part : parts) {
//							if (part.type != null) {
//								locations1.add(part.type.name);
//							}
//						}
//
//						isSwitcherOpen = !isSwitcherOpen;
//						isToolSwitcher = false;
//						switcherIndex = locations1.indexOf(new ResourceLocation(currentPart));
////						ResourceLocation[] locations = Loader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
////						for (int i1 = 0; i1 < locations.length; i1++) {
////							if (locations[i1].toString().equals(tool.name)) {
////								switcherIndex = i1;
////							}
////						}
//					}
//			);

//			cyclePart(button);
//			this.buttons.add(button);
			
			this.buttons.add(
					new Button(
							this.width / 2 + 63, j - 30,
							60, 20,
							(new TranslationTextComponent("button.dynamic_weaponry.cycle_draw_tool")),
							(button1) -> {
								drawTool = drawTool + 1;
								if (drawTool > 1) {
									drawTool = -1;
								}
							}
					)
			);

//			this.buttons.add(output);
		}
		
		int minX = 0;
		int minY = 0;
		int maxX = -1;
		int maxY = -1;
		
		if (!currentPart.equals("")) {
			PartType type = DataLoader.INSTANCE.partTypes.get(new ResourceLocation(currentPart));
			
			if (type != null) {
				minX = type.min.x;
				minY = type.min.y;
				maxX = type.max.x;
				maxY = type.max.y;
			}
		}
		
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
		
		renderTool(
				matrixStack,
				i, j, gridSize,
				minX, minY, maxX, maxY,
				mouseX, mouseY,
				selectedComponent,
				!isSwitcherOpen && tool.isPartCompatible(new ResourceLocation(currentPart))
//				!isSwitcherOpen
		);
		
		matrixStack.pop();
		
		matrixStack.push();
		
		for (ItemSlot slot : slots) slot.render(matrixStack, mouseX, mouseY, i, j, this);
		
		for (ItemSlot slot : slots) {
			slot.renderToolTip = !isSwitcherOpen;
			slot.renderTooltip(matrixStack, mouseX, mouseY, i, j, this);
		}
		
		matrixStack.pop();
		
		if (isSwitcherOpen) {
			matrixStack.push();
			matrixStack.translate(0, 0, 200);
			List<ResourceLocation> locations;
			
			if (!isToolSwitcher) {
				ToolType type = DataLoader.INSTANCE.toolTypes.get(new ResourceLocation(tool.name));
				ToolPart[] parts = type.getParts();
				
				ArrayList<ResourceLocation> locations1 = new ArrayList<>();
				
				for (ToolPart part : parts) {
					if (part.type != null) {
						locations1.add(part.type.name);
					}
				}
				
				locations = locations1;
				
				currentPart = handleSwitcher(
						matrixStack,
						isToolSwitcher, locations,
						currentTool, mouseX, mouseY,
						tool
				);
				
				tool.getComponent(new ResourceLocation(currentPart));
			} else {
				locations = new ArrayList<>(DataLoader.INSTANCE.toolTypes.keySet());
				if (currentTool.equals("")) currentTool = tool.name;
				String lastTool = currentTool;
				currentTool = handleSwitcher(
						matrixStack,
						isToolSwitcher, locations,
						currentTool, mouseX, mouseY,
						tool
				);
				if (!currentTool.equals(lastTool)) {
					ItemStack newStack = new ItemStack(Registry.DYNAMIC_TOOL.get());
					CompoundNBT nbt = newStack.getOrCreateTag();
					CompoundNBT tool_info = new CompoundNBT();
					tool_info.putString("tool_type", currentTool);
					nbt.put("tool_info", tool_info);
					this.tool = new Tool(newStack);
					DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new ToolPacket(tool));
				}
			}
			matrixStack.pop();
		}
		
		tool.sort();
		
		String text = "give @p dynamic_weaponry:dynamic_tool" + tool.serialize().toString();
		output.setMaxStringLength(text.length());
		
		if (!output.getText().equals(text)) {
//			DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new ToolPacket(tool));
			output.setText(text);
		}
	}
	
	public String handleSwitcher(MatrixStack matrixStackIn, boolean isTools, List<ResourceLocation> locations, String prev, int mouseX, int mouseY, Tool tool) {
		ArrayList<ResourceLocation> textures = new ArrayList<>();
		
		int switcherIndexNew = this.switcherIndex;
		
		if (isTools) {
			for (ResourceLocation location : locations)
				textures.add(new ResourceLocation(
						location.getNamespace(),
						"textures/gui/tool_types/" + location.getPath() + ".png"
				));
		} else {
			for (ResourceLocation location : locations)
				textures.add(new ResourceLocation(
						location.getNamespace(),
						"textures/gui/part_types/" + location.getPath() + ".png"
				));
		}
		
		matrixStackIn.push();
		
		int i = (this.width) / 2;
		int j = (this.height) / 2;
		matrixStackIn.translate(i, j, 0);
		
		matrixStackIn.scale(2, 2, 1);
		for (int index = 0; index < textures.size(); index++) {
			ResourceLocation texture = textures.get(index);
			
			matrixStackIn.push();
			
			matrixStackIn.translate(((index - switcherIndex) * 68), 0, 0);
			
			int indexRelative = Math.abs(index - switcherIndex);
			
			ResourceLocation toolOrPart = locations.get(index);
			
			if (indexRelative <= 3) {
				if (
						mouseX >= (((((index - switcherIndex)) * 68) - 32) * 2) + i &&
								mouseX <= (((((index - switcherIndex)) * 68) + 32) * 2) + i
				) {
					if (mouseY >= j - 64 && mouseY <= j + 64) {
						RenderSystem.color3f(0.5f, 0.5f, 0.5f);
						if (lMouseDown) {
							switcherIndexNew = index;
							lMouseDown = false;
						}
					}
				}
				
				if (!isTools && !tool.isPartCompatible(toolOrPart))
					RenderSystem.color3f(1, 0, 0);
				
				drawBackground(
						matrixStackIn,
						-32, -32, 64, 64
				);
				RenderSystem.color3f(1, 1, 1);
			}
			
			int textureSize = 40;
			
			this.minecraft.getTextureManager().bindTexture(texture);
			matrixStackIn.push();
			matrixStackIn.translate(-(textureSize / 2f), ((-textureSize / 2f)) - (textureSize / 6f), 0);
			matrixStackIn.scale(1f / 256, 1f / 256, 1);
			matrixStackIn.scale(textureSize, textureSize, 1);
			
			if (indexRelative <= 3) {
				blit(matrixStackIn, 0, 0, 0, 0, 256, 256);
			}
			
			matrixStackIn.scale(3, 3, 0);
			matrixStackIn.translate(-18, 110, 0);
			String prefix = (isTools ? "tool_type" : "part_type") + ".";
			String name = prefix + toolOrPart.getNamespace() + "." + toolOrPart.getPath().replace("/", ".");
			
			if (indexRelative <= 3) {
				drawString(
						matrixStackIn, font == null ? minecraft.fontRenderer : font,
						new TranslationTextComponent(name), 0, 0, new Color(255, 255, 255).getRGB()
				);
			}
			
			matrixStackIn.pop();
			
			if (index == textures.size() - 1 && indexRelative <= 3 || (textures.size() + index - switcherIndex) >= 2) {
				matrixStackIn.push();
				matrixStackIn.translate((((textures.size()) * -68)), 0, 0);

//				if (indexRelative <= 3) {
				int pos = (((((index - switcherIndex)) * 68) - (32 + (textures.size() * 68))) * 2) + i;
				
				if (
						mouseX >= pos &&
								mouseX <= pos + 128
				) {
					if (mouseY >= j - 64 && mouseY <= j + 64) {
						RenderSystem.color3f(0.5f, 0.5f, 0.5f);
						if (lMouseDown) {
							switcherIndexNew = index;
							lMouseDown = false;
						}
					}
				}
				
				if (!isTools && !tool.isPartCompatible(toolOrPart))
					RenderSystem.color3f(1, 0, 0);
				
				drawBackground(
						matrixStackIn,
						-32, -32, 64, 64
				);
				RenderSystem.color3f(1, 1, 1);
//				}
				
				this.minecraft.getTextureManager().bindTexture(texture);
				matrixStackIn.push();
				matrixStackIn.translate((-textureSize / 2f), ((-textureSize / 2f)) - (textureSize / 6f), 0);
				matrixStackIn.scale(1f / 256, 1f / 256, 1);
				matrixStackIn.scale(textureSize, textureSize, 1);
				blit(matrixStackIn, 0, 0, 0, 0, 256, 256);
				
				matrixStackIn.scale(3, 3, 0);
				matrixStackIn.translate(-18, 110, 0);
				drawString(
						matrixStackIn, font == null ? minecraft.fontRenderer : font,
						new TranslationTextComponent(name), 0, 0, new Color(255, 255, 255).getRGB()
				);
				
				matrixStackIn.pop();
				matrixStackIn.pop();
			}
//			if (Math.abs(((textures.size() - (index + switcherIndex)))) <= 3) {
			if (index - switcherIndex <= textures.size() - 4 || (indexRelative - 1 == 0 && ((index - 1) - switcherIndex <= textures.size() - 4))) {
				matrixStackIn.push();
				matrixStackIn.translate((((textures.size()) * 68)), 0, 0);
				
				int pos = (((((index - switcherIndex)) * 68) - (32 + (textures.size() * -68))) * 2) + i;
				
				if (
						mouseX >= pos &&
								mouseX <= pos + 128
				) {
					if (mouseY >= j - 64 && mouseY <= j + 64) {
						RenderSystem.color3f(0.5f, 0.5f, 0.5f);
						
						if (lMouseDown) {
							switcherIndexNew = index;
							lMouseDown = false;
						}
					}
				}
				
				if (!isTools && !tool.isPartCompatible(toolOrPart))
					RenderSystem.color3f(1, 0, 0);
				
				drawBackground(
						matrixStackIn,
						-32, -32, 64, 64
				);
				
				this.minecraft.getTextureManager().bindTexture(texture);
				matrixStackIn.push();
				matrixStackIn.translate((-textureSize / 2f), ((-textureSize / 2f)) - (textureSize / 6f), 0);
				matrixStackIn.scale(1f / 256, 1f / 256, 1);
				matrixStackIn.scale(textureSize, textureSize, 1);
				blit(matrixStackIn, 0, 0, 0, 0, 256, 256);
				
				matrixStackIn.scale(3, 3, 0);
				matrixStackIn.translate(-18, 110, 0);
				drawString(
						matrixStackIn, font == null ? minecraft.fontRenderer : font,
						new TranslationTextComponent(name), 0, 0, new Color(255, 255, 255).getRGB()
				);
				
				matrixStackIn.pop();
				matrixStackIn.pop();
			}
			
			matrixStackIn.pop();
		}
		matrixStackIn.pop();
		
		switcherIndex = switcherIndexNew;
		
		if (switcherIndex == -1) {
			switcherIndex = 0;
		}
		
		return locations.get(switcherIndex).toString();
	}
	
	public void renderTool(MatrixStack matrixStack, int i, int j, int gridSize, int minX, int minY, int maxX, int maxY, int mouseX, int mouseY, ToolComponent selectedComponent, boolean allowEdits) {
		boolean alternator = false;
		
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
								mouseY < j + 18 - 40 + ((gridSize - 1) - y) * 8 + 8 &&
								allowEdits
				) {
					rgb = rgb * 2;
					hovered = true;
				}
				
				ResourceLocation location = null;
				if (
						hovered &&
								selectedComponent != null &&
//								lMouseDown &&
								!rMouseDown &&
								selectedSlot != null &&
								inBounds
				) {
//					selectedComponent.setPoint(new Point(x, y), selectedSlot.get().getItem().getRegistryName());
//					DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new PaintPixelPacket(x, y, selectedSlot.get().getItem().getRegistryName().toString(), currentPart));
					location = selectedSlot.get().getItem().getRegistryName();
				} else if (
						hovered &&
								selectedComponent != null &&
								rMouseDown &&
								selectedSlot != null &&
								inBounds
				) {
//					selectedComponent.setPoint(new Point(x, y), null);
//					DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new PaintPixelPacket(x, y, null, currentPart));
				}
				
				if (
						hovered &&
								selectedComponent != null &&
								selectedSlot != null &&
								inBounds
				) {
					if ((lMouseDown || rMouseDown)) {
						if (drawTool != -1) {
							if (start == null) {
								start = new MaterialPoint(x, y, location);
							}
						} else {
							DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new PaintPixelPacket(x, y, location != null ? location.toString() : null, currentPart));
						}
					} else if (
							!(lMouseDown || rMouseDown) &&
									start != null
					) {
						if (drawTool != -1) {
							DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new PaintToolPacket(
									start.x, start.y, x, y, drawTool, ((MaterialPoint) start).material != null ? ((MaterialPoint) start).material.toString() : null, currentPart));
							start = null;
						}
					}
				}
				
				RenderSystem.color3f(
						(rgb) / (inBounds ? 2 : 4),
						(rgb) / (inBounds ? 2 : 4),
						(rgb) / (inBounds ? 2 : 4)
				);
				
				if (selectedComponent != null && selectedComponent.type != null) {
					for (Point requiredPoint : selectedComponent.type.getRequiredPoints()) {
						if (requiredPoint.x == x && requiredPoint.y == y) {
							if (checkPoint(selectedComponent, requiredPoint)) {
								RenderSystem.color3f(0, 255, 0);
							} else {
								RenderSystem.color3f(255, 0, 0);
							}
						}
					}
				}
				
				blit(matrixStack, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0);
				
				Point vec = new Point(x, y);
				Line l = new Line(
						new Point(-6, 8 + 6),
						new Point(8 + 6, -6)
				);
				float r = 0, g = 0;
				boolean isR = false;
				for (int iter = 0; iter < 32; iter++) {
					Point p = l.getInterp(iter / 32f);
					if (vec.x <= p.x && vec.y <= p.y) isR = true;
				}
				if (isR) {
					r = 0;
					g = ((float) l.dist(vec) / 16f) * 2;
				} else {
					r = (float) l.dist(vec) / 16f;
					g = 0;
				}
//				RenderSystem.color4f(
//						r, g, 0,
//						0.2f
//				);
//				blit(matrixStack, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0);
				
				matrixStack.pop();
				alternator = !alternator;
			}
			
			alternator = !alternator;
		}
		
		for (ToolComponent component : tool.components) {
			boolean selected = component.name.equals(currentPart);
			
			for (MaterialPoint point : component.points) {
				Material material = DataLoader.INSTANCE.getMaterial(point.material);
				
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
									mouseY < j + 18 - 40 + ((gridSize - 1) - y) * 8 + 8 &&
									allowEdits
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
					
					if (selectedComponent != null) {
						for (Point requiredPoint : selectedComponent.type.getRequiredPoints()) {
							if (requiredPoint.x == x && requiredPoint.y == y) {
								if (checkPoint(selectedComponent, requiredPoint)) {
									RenderSystem.color4f(0, 255, 0, 0.15f);
								} else {
									RenderSystem.color4f(255, 0, 0, 0.15f);
								}
							}
						}
					}
					
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
		
		RenderSystem.color3f(1, 1, 1);
		RenderSystem.enableTexture();
	}
	
	private boolean checkPoint(ToolComponent selectedComponent, Point requiredPoint) {
		return selectedComponent.checkPoint(requiredPoint);
	}
	
	private void cyclePart(Button button) {
		try {
			int index = 0;
			ResourceLocation[] typeNames = DataLoader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
			ToolType[] types = DataLoader.INSTANCE.toolTypes.values().toArray(new ToolType[0]);
			
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

//			button.setMessage(new TranslationTextComponent("part_type." + name.getNamespace() + "." + name.getPath().replace("/", ".")));
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

//	public void cycleTool(Button button) {
//		int index = 0;
//		ResourceLocation[] types = Loader.INSTANCE.toolTypes.keySet().toArray(new ResourceLocation[0]);
//
//		for (ResourceLocation location : types) {
//			if (location.toString().equals(tool.name)) {
//				break;
//			}
//
//			index++;
//		}
//
//		if (index + 1 >= types.length) index = -1;
//		currentTool = types[index + 1].toString();
//		tool.name = currentTool;
//
//		ResourceLocation name = new ResourceLocation(tool.name);
//		button.setMessage(new TranslationTextComponent("tool_type." + name.getNamespace() + "." + name.getPath().replace("/", ".")));
//
//		ItemStack newStack = new ItemStack(Registry.DYNAMIC_TOOL.get());
//		CompoundNBT nbt = newStack.getOrCreateTag();
//		CompoundNBT tool_info = new CompoundNBT();
//		cyclePart((Button) (buttons.get(1)));
//		tool_info.putString("tool_type", currentTool);
//		nbt.put("tool_info", tool_info);
//		this.tool = new Tool(newStack);
//
//		DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new ToolPacket(tool));
//	}
	
	public boolean hasNeighbor(ToolComponent component, Point point) {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (Math.abs(x) != Math.abs(y)) {
					MaterialPoint point1 = tool.getPoint(point.x + x, point.y + y);
					if (point1 != null) return true;
					break;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) lMouseDown = true;
		else if (button == 1) rMouseDown = true;
		
		for (Widget button1 : buttons) {
			boolean clicked = button1.mouseClicked(mouseX, mouseY, button);
			if (clicked) return true;
		}
		
		if (isSwitcherOpen) return false;
		
		if (button == 0) {
			int i = (this.width - 248) / 2;
			int j = (this.height - 166) / 2;
			
			for (ItemSlot slot : slots) {
				boolean clicked = slot.click((int) mouseX, (int) mouseY, i, j, this);
				
				if (clicked) {
					Material material = DataLoader.INSTANCE.getMaterial(slot.get().getItem().getRegistryName());
					
					if (material != null) {
						for (ItemSlot slot1 : slots) {
							material = DataLoader.INSTANCE.getMaterial(slot1.get().getItem().getRegistryName());
							if (material != null) {
								ClientMaterialInfo materialInfo = AssetLoader.INSTANCE.getMaterial(material.item);
								if (materialInfo != null) slot.color = new Color(materialInfo.color);
								else slot.color = new Color(255, 255, 255);
							}
						}
						
						selectedSlot = slot;
						slot.color = new Color(128, 255, 128);
					} else {
						if (slot.get().getItem() instanceof DynamicTool) {
							tool = new Tool(slot.get());
							DynamicWeaponry.NETWORK_INSTANCE.sendToServer(new ToolPacket(tool));
						}
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
		
		for (Widget button1 : buttons) {
			if (button1.isFocused()) {
				boolean clicked = button1.mouseReleased(mouseX, mouseY, button);
				if (clicked) return true;
			}
		}
		
		if (isSwitcherOpen) return false;
		
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (Widget button1 : buttons) {
			if (button1.isFocused()) {
				boolean clicked = button1.keyReleased(keyCode, scanCode, modifiers);
				if (clicked) return true;
			}
		}
		
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		for (Widget button1 : buttons) {
			if (button1.isFocused()) {
				boolean clicked = button1.charTyped(codePoint, modifiers);
				if (clicked) return true;
			}
		}
		
		return super.charTyped(codePoint, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (Widget button1 : buttons) {
			if (button1.isFocused()) {
				boolean clicked = button1.keyPressed(keyCode, scanCode, modifiers);
				if (clicked) return true;
			}
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean changeFocus(boolean focus) {
		System.out.println(focus);
		return super.changeFocus(false);
	}
	
	@Override
	public List<ITextComponent> getTooltipFromItem(ItemStack itemStack) {
		ArrayList<ITextComponent> list = new ArrayList(super.getTooltipFromItem(itemStack));
		Material material = DataLoader.INSTANCE.getMaterial(itemStack.getItem().getRegistryName());
		
		if (material != null) {
			list.add(new StringTextComponent(
					"Weight: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(material.weight)).mergeStyle(TextFormatting.RED))
			);
			list.add(new StringTextComponent(
					"Efficiency: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(material.efficiency)).mergeStyle(TextFormatting.RED))
			);
			list.add(new StringTextComponent(
					"Attack Power: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(material.attack)).mergeStyle(TextFormatting.RED))
			);
			list.add(new StringTextComponent(
					"Durability: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(material.durability)).mergeStyle(TextFormatting.RED))
			);
			list.add(new StringTextComponent(
					"Harvest Level: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(material.getHarvestLevel())).mergeStyle(TextFormatting.RED))
			);
			ClientMaterialInfo materialInfo = AssetLoader.INSTANCE.getMaterial(material.item);
			list.add(new StringTextComponent(
					"Color: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(materialInfo.color)).mergeStyle(Style.EMPTY.setColor(net.minecraft.util.text.Color.fromInt(materialInfo.color))))
			);
			list.add(new StringTextComponent(
					"Amount Needed: ").mergeStyle(TextFormatting.GREEN).append(new StringTextComponent(
					String.valueOf(tool.getMaterialCost(material))).mergeStyle(TextFormatting.RED))
			);
		}
		
		return list;
	}
}
