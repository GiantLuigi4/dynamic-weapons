package tfc.dynamic_weaponary.Utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrawingUtils {
	public static void drawTexturedRect(double x, double y, double u, double v, double texWidth, double texHeight, double width, double height, double red, double green, double blue, double alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buffer.pos(x, (y + height), 0).tex((float) ((float) u * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos((x + width), (y + height), 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos((x + width), y, 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		buffer.pos(x, y, 0).tex((float) ((float) u * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		tessellator.draw();
	}
	
	private static ItemStack cachedTooltipStack;
	
	public static void drawTexturedRect(double x, double y, double u, double v, double texWidth, double texHeight, double width, double height, double red, double green, double blue, double alpha, IRenderTypeBuffer builder, RenderType renderType) {
		try {
			IVertexBuilder buffer = builder.getBuffer(renderType);
			buffer.pos(x, (y + height), 0).lightmap(0, 0).tex((float) ((float) u * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
			buffer.pos((x + width), (y + height), 0).lightmap(0, 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) (v + texHeight) * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
			buffer.pos((x + width), y, 0).lightmap(0, 0).tex((float) ((float) (u + texWidth) * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
			buffer.pos(x, y, 0).lightmap(0, 0).tex((float) ((float) u * 0.00390625F), (float) ((float) v * 0.00390625F)).color((float) red, (float) green, (float) blue, (float) alpha).endVertex();
		} catch (Exception err) {
		}
	}
	
	public static void drawHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
		drawHoveringText(textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, -267386864, 1347420415, 1344798847, font);
	}
	
	public static void drawHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, int backgroundColor, int borderColorStart, int borderColorEnd, FontRenderer font) {
		drawHoveringText(cachedTooltipStack, textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, backgroundColor, borderColorStart, borderColorEnd, font);
	}
	
	public static void drawHoveringText(@Nonnull ItemStack stack, List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, int backgroundColor, int borderColorStart, int borderColorEnd, FontRenderer font) {
		if (!((List) textLines).isEmpty()) {
			RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(stack, (List) textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font);
//			if (MinecraftForge.EVENT_BUS.post(event)) {
//				return;
//			}
			
			mouseX = event.getX();
			mouseY = event.getY();
			screenWidth = event.getScreenWidth();
			screenHeight = event.getScreenHeight();
			maxTextWidth = event.getMaxWidth();
			font = event.getFontRenderer();
			RenderSystem.disableRescaleNormal();
			RenderSystem.disableDepthTest();
			int tooltipTextWidth = 0;
			Iterator var13 = ((List) textLines).iterator();
			
			int tooltipX;
			while (var13.hasNext()) {
				String textLine = (String) var13.next();
				tooltipX = font.getStringWidth(textLine);
				if (tooltipX > tooltipTextWidth) {
					tooltipTextWidth = tooltipX;
				}
			}
			
			boolean needsWrap = false;
			int titleLinesCount = 1;
			tooltipX = mouseX + 12;
			if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
				tooltipX = mouseX - 16 - tooltipTextWidth;
				if (tooltipX < 4) {
					if (mouseX > screenWidth / 2) {
						tooltipTextWidth = mouseX - 12 - 8;
					} else {
						tooltipTextWidth = screenWidth - 16 - mouseX;
					}
					
					needsWrap = true;
				}
			}
			
			if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
				tooltipTextWidth = maxTextWidth;
				needsWrap = true;
			}
			
			int tooltipY;
			if (needsWrap) {
				tooltipY = 0;
				List<String> wrappedTextLines = new ArrayList();
				int i = 0;
				
				while (true) {
					if (i >= ((List) textLines).size()) {
						tooltipTextWidth = tooltipY;
						textLines = wrappedTextLines;
						if (mouseX > screenWidth / 2) {
							tooltipX = mouseX - 16 - tooltipY;
						} else {
							tooltipX = mouseX + 12;
						}
						break;
					}
					
					String textLine = (String) ((List) textLines).get(i);
					List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
					if (i == 0) {
						titleLinesCount = wrappedLine.size();
					}
					
					String line;
					for (Iterator var21 = wrappedLine.iterator(); var21.hasNext(); wrappedTextLines.add(line)) {
						line = (String) var21.next();
						int lineWidth = font.getStringWidth(line);
						if (lineWidth > tooltipY) {
							tooltipY = lineWidth;
						}
					}
					
					++i;
				}
			}
			
			tooltipY = mouseY - 12;
			int tooltipHeight = 8;
			if (((List) textLines).size() > 1) {
				tooltipHeight += (((List) textLines).size() - 1) * 10;
				if (((List) textLines).size() > titleLinesCount) {
					tooltipHeight += 2;
				}
			}
			
			if (tooltipY < 4) {
				tooltipY = 4;
			} else if (tooltipY + tooltipHeight + 4 > screenHeight) {
				tooltipY = screenHeight - tooltipHeight - 4;
			}

//			int zLevel = true;
//			RenderTooltipEvent.Color colorEvent = new RenderTooltipEvent.Color(stack, (List) textLines, tooltipX, tooltipY, font, backgroundColor, borderColorStart, borderColorEnd);
//			MinecraftForge.EVENT_BUS.post(colorEvent);
//			backgroundColor = colorEvent.getBackground();
//			borderColorStart = colorEvent.getBorderStart();
//			borderColorEnd = colorEvent.getBorderEnd();
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			GuiUtils.drawGradientRect(300, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
			GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
//			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, (List) textLines, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));
			IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			MatrixStack textStack = new MatrixStack();
			textStack.translate(0.0D, 0.0D, 300.0D);
			Matrix4f textLocation = textStack.getLast().getMatrix();
			
			for (int lineNumber = 0; lineNumber < ((List) textLines).size(); ++lineNumber) {
				String line = (String) ((List) textLines).get(lineNumber);
				if (line != null) {
					int color = -1;
					if (line.startsWith("Color:")) {
						String c = line.substring(("Color:").length());
						try {
							color = Integer.parseInt(c);
						} catch (Exception err) {
						}
					}
					font.renderString(line, (float) tooltipX, (float) tooltipY, color, true, textLocation, renderType, false, 0, 15728880);
				}
				
				if (lineNumber + 1 == titleLinesCount) {
					tooltipY += 2;
				}
				
				tooltipY += 10;
			}
			
			renderType.finish();
//			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, (List) textLines, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));
			RenderSystem.enableDepthTest();
			RenderSystem.enableRescaleNormal();
		}
		
	}
	
	public static class ColorHelper {
		public static ColorHelper BLUE = new ColorHelper(0, 0, 255);
		int value;
		
		public ColorHelper(int r, int g, int b) {
			this(r, g, b, 255);
		}
		
		public ColorHelper(int r, int g, int b, int a) {
			value = ((a & 0xFF) << 24) |
					((r & 0xFF) << 16) |
					((g & 0xFF) << 8) |
					((b & 0xFF));
//			testColorValueRange(r,g,b,a);
		}
		
		public ColorHelper(int rgb) {
			value = 0xff000000 | rgb;
		}
		
		public ColorHelper darker() {
			double FACTOR = 0.7;
			return new ColorHelper(Math.max((int) (getRed() * FACTOR), 0),
					Math.max((int) (getGreen() * FACTOR), 0),
					Math.max((int) (getBlue() * FACTOR), 0),
					getAlpha());
		}
		
		public ColorHelper darker(double FACTOR) {
			return new ColorHelper(Math.max((int) (getRed() * FACTOR), 0),
					Math.max((int) (getGreen() * FACTOR), 0),
					Math.max((int) (getBlue() * FACTOR), 0),
					getAlpha());
		}
		
		public int getRGB() {
			return value;
		}
		
		public int getRed() {
			return (getRGB() >> 16) & 0xFF;
		}
		
		public int getGreen() {
			return (getRGB() >> 8) & 0xFF;
		}
		
		public int getBlue() {
			return (getRGB() >> 0) & 0xFF;
		}
		
		public int getAlpha() {
			return (getRGB() >> 24) & 0xff;
		}
		
		public boolean equals(Object obj) {
			return obj instanceof ColorHelper && ((ColorHelper) obj).getRGB() == this.getRGB();
		}
		
		public String toString() {
			return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
		}
	}
}
