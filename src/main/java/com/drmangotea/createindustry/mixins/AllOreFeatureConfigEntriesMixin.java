package com.drmangotea.createindustry.mixins;


import com.drmangotea.createindustry.worldgen.TFMGLayeredPatterns;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.DynamicDataProvider;
import com.simibubi.create.infrastructure.worldgen.AllLayerPatterns;
import com.simibubi.create.infrastructure.worldgen.AllOreFeatureConfigEntries;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

 /**
 * really goofy way to do worldgen but it works
 */
@Mixin(AllOreFeatureConfigEntries.class)
public class AllOreFeatureConfigEntriesMixin {




	@Shadow
	public static final OreFeatureConfigEntry STRIATED_ORES_OVERWORLD =
			create("striated_ores_overworld", 32, 1 / 18f, -30, 70)
					.layeredDatagenExt()
					.withLayerPattern(TFMGLayeredPatterns.BAUXITE)
					.withLayerPattern(TFMGLayeredPatterns.LIGNITE)
					.withLayerPattern(TFMGLayeredPatterns.FIRECLAY)
					.withLayerPattern(TFMGLayeredPatterns.GALENA)
					.withLayerPattern(AllLayerPatterns.SCORIA)
					.withLayerPattern(AllLayerPatterns.CINNABAR)
					.withLayerPattern(AllLayerPatterns.MAGNETITE)
					.withLayerPattern(AllLayerPatterns.MALACHITE)
					.withLayerPattern(AllLayerPatterns.LIMESTONE)
					.withLayerPattern(AllLayerPatterns.OCHRESTONE)
					.biomeTag(BiomeTags.IS_OVERWORLD)
					.parent();
	@Shadow
	public static final OreFeatureConfigEntry STRIATED_ORES_NETHER =
			create("striated_ores_nether", 32, 1 / 18f, 40, 90)
					.layeredDatagenExt()
					.withLayerPattern(TFMGLayeredPatterns.SULFUR)
					.withLayerPattern(AllLayerPatterns.SCORIA_NETHER)
					.withLayerPattern(AllLayerPatterns.SCORCHIA_NETHER)
					.biomeTag(BiomeTags.IS_NETHER)
					.parent();

	//

	private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency,
												int minHeight, int maxHeight) {
		ResourceLocation id = Create.asResource(name);
		OreFeatureConfigEntry configDrivenFeatureEntry = new OreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
		return configDrivenFeatureEntry;
	}

	@Shadow
	public static void init() {}
}

