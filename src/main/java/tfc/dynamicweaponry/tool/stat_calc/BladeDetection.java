package tfc.dynamicweaponry.tool.stat_calc;

import tfc.dynamicweaponry.tool.ToolLayer;

public class BladeDetection {
	public static void findAxe(boolean[] blade, boolean[] edges, ToolLayer[] layers) {
		int[] ints = ToolFlattener.prep2D(layers)[1];
		int maxV = -1;
		int max = Integer.MAX_VALUE;
		
		for (int i = 0; i < ints.length / 2; i++) {
			if (ints[i + 16] != -1) {
				if (i == 0 || i == 15)
					continue;
				
				int dl = Math.abs(ints[i + 15] - ints[i + 16]);
				if (ints[i + 15] == -1) dl = 0;
				int dr = Math.abs(ints[i + 17] - ints[i + 16]);
				if (ints[i + 17] == -1) dl = 0;
				int dx = Math.max(dl, dr);
				
				if (15 - ints[i + 16] > maxV) {
					maxV = 15 - ints[i + 16];
					max = i;
				}
				
				edges[i] = dx > 1;
			}
		}
		
		for (int i = max; i > 0; i--) {
			blade[i] = true;
			if (edges[i]) break;
		}
		
		for (int i = max; i < 16; i++) {
			if (edges[i]) break;
			blade[i] = true;
		}
	}
}
