package tfc.dynamic_weaponary.Other;

import net.minecraft.util.ResourceLocation;
import tfc.dynamic_weaponary.DynamicWeapons;
import tfc.dynamic_weaponary.EventRegistryRedo;
import tfc.dynamic_weaponary.Utils.Tool.Material;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Config {
	public static void createAndWrite(File path, String ModID, String text) {
		File fi = new File(path + "\\" + ModID + ".cfg");
		try {
			if (!fi.exists()) {
				fi.getParentFile().mkdirs();
				FileWriter writer = new FileWriter(fi);
				writer.write(text);
				writer.close();
			}
		} catch (Exception err) {
		}
	}
	
	public static void readConfig(File path, String ModID) {
		try {
			Scanner sc = new Scanner(new File(path + "\\" + ModID + ".cfg"));
			String id = "";
			int durability = 0;
			double strength = 0;
			double weight = 0;
			int color = 0;
			String event_hitentity = "";
			String event_killentit = "";
			String _event_invtick_ = "";
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.startsWith("id=")) {
					id = line.substring("id=".length());
				} else if (line.startsWith("durability=")) {
					durability = Integer.parseInt(line.substring("durability=".length()));
				} else if (line.startsWith("strength=")) {
					strength = Double.parseDouble(line.substring("strength=".length()));
				} else if (line.startsWith("weight=")) {
					weight = Double.parseDouble(line.substring("weight=".length()));
				} else if (line.startsWith("color=")) {
					color = Integer.parseInt(line.substring("color=".length()));
				} else if (line.equals("-----------------------------------")) {
					DynamicWeapons.tryRegisterModdedMaterial(new ResourceLocation(id), new Material(durability, strength, weight, color));
//					DynamicWeapons.LOGGER.log(Level.INFO, "test:" + event_hitentity);
					if (!event_hitentity.equals("")) {
//						DynamicWeapons.LOGGER.log(Level.INFO, "test:" + event_hitentity);
						EventRegistryRedo.RegisterEvent(id + ".event_hitentity", event_hitentity);
					}
					if (!_event_invtick_.equals("")) {
//						DynamicWeapons.LOGGER.log(Level.INFO, "test:" + _event_invtick_);
						EventRegistryRedo.RegisterEvent(id + ".InvTick", _event_invtick_);
					}
					event_hitentity = "";
					event_killentit = "";
					_event_invtick_ = "";
				} else {
					if (line.startsWith("event_hitentity=")) {
						event_hitentity = line.substring("event_hitentity=".length());
					} else if (line.startsWith("event_killentit=")) {
						event_killentit = line.substring("event_killentit=".length());
					} else if (line.startsWith("InvTick=")) {
						_event_invtick_ = line.substring("InvTick=".length());
					}
				}
			}
			sc.close();
		} catch (Exception err) {
		}
	}
}
