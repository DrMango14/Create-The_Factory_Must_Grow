package com.drmangotea.tfmg.config;

import com.drmangotea.tfmg.content.electricity.connection.cable_type.ResistivityValues;
import com.simibubi.create.api.stress.BlockStressValues;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFMGConfigs {

	private static final Map<ModConfig.Type, net.createmod.catnip.config.ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

	private static TFMGServerConfig server;
	private static TFMGCommonConfig common;



	public static TFMGServerConfig server() {
		return server;
	}

	public static TFMGCommonConfig common() {
		return common;
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
	@SuppressWarnings("removal")
	public static void register(ModLoadingContext context) {
		server = register(TFMGServerConfig::new, ModConfig.Type.SERVER);
		common = register(TFMGCommonConfig::new, ModConfig.Type.COMMON);
		for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			context.registerConfig(pair.getKey(), pair.getValue().specification);
		TFMGStress stress = TFMGConfigs.server().stressValues;
		BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
		BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
		TFMGResistivity resistivity = server().resistivityValues;
		ResistivityValues.RESISTIVITIES.registerProvider(resistivity::getResistivity);
	}

	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading event) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == event.getConfig()
					.getSpec())
				config.onLoad();
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading event) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == event.getConfig()
					.getSpec())
				config.onReload();
	}

}