package tfc.dynamic_weaponary.Utils.Optimization;

import tfc.dynamic_weaponary.Utils.Image.MaterialBasedPixelStorage;

import java.util.HashMap;

public class ImageList {
	private static HashMap<String, MaterialBasedPixelStorage> images = new HashMap<>();
	
	public static MaterialBasedPixelStorage addOrReplaceImage(String data) {
		if (images.containsKey(data)) {
			images.replace(data, MaterialBasedPixelStorage.fromString(data));
		} else {
			images.put(data, MaterialBasedPixelStorage.fromString(data));
		}
		return images.get(data);
	}
	
	public static MaterialBasedPixelStorage get(String data) {
		if (images.containsKey(data)) {
			return images.get(data);
		} else {
			return null;
		}
	}
}
