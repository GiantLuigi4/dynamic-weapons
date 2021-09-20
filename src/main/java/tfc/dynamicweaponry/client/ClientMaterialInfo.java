package tfc.dynamicweaponry.client;

import net.minecraft.util.ResourceLocation;
import tfc.assortedutils.utils.Color;

public class ClientMaterialInfo {
	public final ResourceLocation item;
	protected int[][] pattern = null;
	protected int color = 0;
	protected int colorBorder = 0;
	private boolean isLocked = false;
	
	public ClientMaterialInfo(ResourceLocation item) {
		this.item = item;
	}
	
	public int[][] getPattern() {
		return pattern;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
		this.colorBorder = new Color(color).darker(0.5f, -12).getRGB();
	}
	
	public int getColorBorder() {
		return colorBorder;
	}
}
