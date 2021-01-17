package com.tfc.dynamicweaponry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Config {
	public static final Config CLIENT;
	static final ForgeConfigSpec clientSpec;
	
	static {
		final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
	
	public final ForgeConfigSpec.BooleanValue useShading;
	public final ForgeConfigSpec.BooleanValue useOutlines;
	
	public Config(ForgeConfigSpec.Builder builder) {
		builder.comment("Rendering Settings").push("Client");
		
		useShading = builder
				.comment("Whether or not to apply shading to custom tools. (Laggier, but looks better)")
				.translation("config.dynamic_weaponry.shading")
				.define("UseShading", true);
		
		useOutlines = builder
				.comment("Whether or not to apply outlines to custom tools. (Laggier, but looks better)")
				.translation("config.dynamic_weaponry.outlines")
				.define("UseOutlines", true);
		
		builder.pop();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		LogManager.getLogger().log(Level.INFO, "Loaded dynamic weaponry config file {}", configEvent.getConfig().getFileName());
	}
}
