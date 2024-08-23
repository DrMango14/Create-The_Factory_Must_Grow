package com.drmangotea.createindustry.worldgen;


import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;


public class TFMGConfiguredFeatures {

    //  public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
    //        DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CreateIndustry.MOD_ID);


    public static final RuleTest BEDROCK = new BlockMatchTest(Blocks.BEDROCK);
    //-------------------------------------------------------------------------------------------------//


    public static final Holder<ConfiguredFeature<GeodeConfiguration, ?>> OIL_CONFIGURED =
            FeatureUtils.register("createindustry:oil", TFMGFeatures.OIL.get(),
                    new GeodeConfiguration(new GeodeBlockSettings(BlockStateProvider.simple(TFMGFluids.CRUDE_OIL.get().getSource().defaultFluidState().createLegacyBlock()), BlockStateProvider.simple(Blocks.AIR), BlockStateProvider.simple(Blocks.AIR), BlockStateProvider.simple(Blocks.AIR), BlockStateProvider.simple(TFMGBlocks.FOSSILSTONE.get()), List.of(Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState()), BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS), new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformInt.of(4, 6), UniformInt.of(3, 4), UniformInt.of(1, 2), -16, 16, 0.05D, 1));


//public static final Holder<ConfiguredFeature<OreConfiguration, ?>> SIMULATED_OIL_CONFIGURED =
//        FeatureUtils.register("createindustry:simulated_oil", TFMGFeatures.SIMULATED_OIL.get(),
//        new OreConfiguration(BEDROCK, TFMGBlocks.OIL_DEPOSIT.get().defaultBlockState(), 35));


    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> OIL_DEPOSIT_CONFIGURED =
            FeatureUtils.register("createindustry:oil_deposit", TFMGFeatures.OIL_DEPOSIT.get(),
                    new NoneFeatureConfiguration());


    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> OIL_WELL_CONFIGURED =
            FeatureUtils.register("createindustry:oil_well", TFMGFeatures.OIL_WELL.get(),
                    new NoneFeatureConfiguration());


    //-------------------------------------------------------------------------------------------------//


    public static final Holder<PlacedFeature> OIL_PLACED = PlacementUtils.register("createindustry:oil",
            OIL_CONFIGURED, RarityFilter.onAverageOnceEvery(120),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-50),
                    VerticalAnchor.absolute(-10)), BiomeFilter.biome());

//public static final Holder<PlacedFeature> SIMULATED_OIL_PLACED = PlacementUtils.register("createindustry:simulated_oil",
//        SIMULATED_OIL_CONFIGURED, RarityFilter.onAverageOnceEvery(75));

    public static final Holder<PlacedFeature> OIL_DEPOSIT_PLACED = PlacementUtils.register("createindustry:oil_deposit",
            OIL_DEPOSIT_CONFIGURED, RarityFilter.onAverageOnceEvery(50)
            , HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(-64)));

    public static final Holder<PlacedFeature> OIL_WELL_PLACED = PlacementUtils.register("createindustry:oil_well",
            OIL_WELL_CONFIGURED, RarityFilter.onAverageOnceEvery(200)
            , HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(-64)));


    // public static void register(IEventBus eventBus) {
    //   PLACED_FEATURES.register(eventBus);
    //}


}
