package tfc.dynamic_weaponary.Utils.Tool;

import java.util.HashMap;

public class ToolLogicHelper {
	HashMap<String, Float> values = new HashMap<>();
	int number = 0;
	
	public ToolLogicHelper() {
	}
	
	public float add(String item, float amount) {
		try {
			values.replace(item, values.get(item) + amount);
		} catch (Exception err) {
			values.put(item, amount);
		}
		number += amount;
		return values.get(item) + amount;
	}
	
	public float get(String item) {
		return values.get(item);
	}
	
	public float set(String item, float amount) {
		try {
			values.replace(item, amount);
		} catch (Exception err) {
			values.put(item, amount);
		}
		return amount;
	}
	
	public float getPercent(String item) {
		return values.get(item) / number;
	}
}
