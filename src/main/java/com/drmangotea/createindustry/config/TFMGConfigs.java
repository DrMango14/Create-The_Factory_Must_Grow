package com.drmangotea.createindustry.config;

import com.drmangotea.createindustry.CreateTFMG;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;


public class TFMGConfigs {

	private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

	private static ServerConfig server;

	public static ServerConfig server() {
		return server;
	}

	public static ConfigBase byType(ModConfig.Type type) {
		return CONFIGS.get(type);
	}

	private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
		Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});

		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		CONFIGS.put(side, config);
		return config;
	}

	public static void register() {
		server = register(ServerConfig::new, ModConfig.Type.SERVER);

		for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			ModLoadingContext.registerConfig(CreateTFMG.MOD_ID, pair.getKey(), pair.getValue().specification);

		BlockStressValues.registerProvider(CreateTFMG.MOD_ID, server().stressValues);

		ModConfigEvents.loading(CreateTFMG.MOD_ID).register(TFMGConfigs::onLoad);
		ModConfigEvents.loading(CreateTFMG.MOD_ID).register(TFMGConfigs::onReload);
	}

	public static void onLoad(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig
					.getSpec())
				config.onLoad();
	}

	public static void onReload(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig
					.getSpec())
				config.onReload();
	}

}
