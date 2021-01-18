package com.tfc.dynamicweaponry;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

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
	public final ForgeConfigSpec.BooleanValue cacheBuffers;
	
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
		
		cacheBuffers = builder
				.comment("Whether or not to cache vertex buffers of custom tools. (Higher RAM usage, doesn't affect visuals, but does increase performance. If this is on, shading and outlines will be calculated once, and thus won't really affect performance)")
				.translation("config.dynamic_weaponry.cache_buffers")
				.define("CacheBuffers", true);
		
		builder.pop();
	}
}
