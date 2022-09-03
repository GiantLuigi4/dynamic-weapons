package tfc.dynamicweaponry;

import tfc.dynamicweaponry.tool.ToolLayer;

public class Temp {
	public static ToolLayer[] layers;
	
	static {
		try {
			layers = new ToolLayer[4];
			for (int i = 0; i < layers.length; i++) {
				layers[i] = new ToolLayer();
			}
		} catch (Throwable err) {
			throw new RuntimeException();
		}
	}
}
