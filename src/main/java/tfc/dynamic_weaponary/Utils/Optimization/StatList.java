package tfc.dynamic_weaponary.Utils.Optimization;

import tfc.dynamic_weaponary.Utils.Image.MaterialBasedPixelStorage;

import java.util.HashMap;

public class StatList {
	private static HashMap<String, ToolStats> Stats = new HashMap<>();
	
	public static ToolStats addOrReplaceImage(String data) {
		if (Stats.containsKey(data)) {
			Stats.replace(data, new ToolStats(MaterialBasedPixelStorage.fromString(data)));
		} else {
			Stats.put(data, new ToolStats(MaterialBasedPixelStorage.fromString(data)));
		}
		return Stats.get(data);
	}
	
	public static ToolStats get(String data) {
		if (Stats.containsKey(data)) {
			return Stats.get(data);
		} else {
			return null;
		}
	}
}
