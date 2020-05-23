package tfc.dynamic_weaponary.Utils;

import net.minecraft.item.Item;

public class CFGHelper {
	public static class matString {
		public String id;
		public int durability;
		public double strength;
		public double weight;
		public int color;
		public String hitEntityMethod = "no method";
		public String killEntityMethod = "no method";
		public String inInventoryMethod = "no method";
		
		public matString(Item item, int durability, double strength, double weight, int color) {
			this.id = item.getRegistryName().toString();
			this.durability = durability;
			this.strength = strength;
			this.weight = weight;
			this.color = color;
		}
		
		public matString(String id, int durability, double strength, double weight, int color) {
			this.id = id;
			this.durability = durability;
			this.strength = strength;
			this.weight = weight;
			this.color = color;
		}
		
		public matString(Item item, int durability, double strength, double weight, int color, String hitEntityMethod, String killEntityMethod, String inInventoryMethod) {
			this.id = item.getRegistryName().toString();
			this.durability = durability;
			this.strength = strength;
			this.weight = weight;
			this.color = color;
			this.hitEntityMethod = hitEntityMethod;
			this.killEntityMethod = killEntityMethod;
			this.inInventoryMethod = inInventoryMethod;
		}
		
		public matString(String id, int durability, double strength, double weight, int color, String hitEntityMethod, String killEntityMethod, String inInventoryMethod) {
			this.id = id;
			this.durability = durability;
			this.strength = strength;
			this.weight = weight;
			this.color = color;
			this.hitEntityMethod = hitEntityMethod;
			this.killEntityMethod = killEntityMethod;
			this.inInventoryMethod = inInventoryMethod;
		}
		
		@Override
		public String toString() {
			String methods = "";
			if (!hitEntityMethod.equals("no method")) {
				methods += "event_hitentity=" + hitEntityMethod + "\n";
			}
			if (!killEntityMethod.equals("no method")) {
				methods += "event_killentit=" + killEntityMethod + "\n";
			}
			if (!inInventoryMethod.equals("no method")) {
				methods += "InvTick=" + inInventoryMethod + "\n";
			}
			return "" +
					"id=" + id + "\n" +
					"durability=" + durability + "\n" +
					"strength=" + strength + "\n" +
					"weight=" + weight + "\n" +
					"color=" + color + "\n" +
					methods +
					"-----------------------------------\n";
		}
	}
}
