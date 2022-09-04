package tfc.dynamicweaponry.tool.stat_calc;

import tfc.dynamicweaponry.tool.ToolLayer;

public class ToolFlattener {
	public static int[][] prep2D(ToolLayer[] layers) {
		int[] ints = prep(layers);
		int count = ints.length / 32;
		int[][] ints1 = new int[count][32];
		for (int i = 0; i < ints1.length; i++) {
			for (int x = 0; x < 32; x++) {
				ints1[i][x] = ints[x + i * 32];
			}
		}
		return ints1;
	}
	
	public static int[] prep(ToolLayer[] layers) {
		int[] ints = new int[64];
		for (int i = 0; i < 32; i++)
			ints[i] = runRay(i - 15, layers);
		for (int i = 0; i < 16; i++) {
			ints[i + 32] = runFrom(0, 15 - i, 1, 0, layers);
			ints[i + (32 + 16)] = runFrom(i, 0, 0, 1, layers);
		}
		return ints;
	}
	
	private static int runFrom(int sx, int sy, int dx, int dy, ToolLayer[] layers) {
		int i = 0;
		while (sx <= 15 && sy <= 15 && sx >= 0 && sy >= 0) {
			for (ToolLayer layer : layers)
				if (layer.get(sx, 15 - sy) != null)
					return i;
			sx += dx;
			sy += dy;
			i++;
		}
		return -1;
	}
	
	private static int runRay(int x, ToolLayer[] layers) {
		int y = 0;
		while (true) {
			y++;
			if (y > 15) {
				break;
			}
			
			if (x + y > 15) return -1;
			
			for (ToolLayer layer : layers) {
				if (x + y >= 0)
					if (layer.get(x + y, 15 - y) != null)
						return y;
			}
		}
		return -1;
	}
}
