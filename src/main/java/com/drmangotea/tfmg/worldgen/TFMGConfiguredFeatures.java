package com.drmangotea.tfmg.worldgen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.worldgen.deposits.DepositFeatureConfig;
import com.drmangotea.tfmg.worldgen.deposits.WellFeatureConfig;
import com.simibubi.create.infrastructure.worldgen.AllFeatures;
import com.simibubi.create.infrastructure.worldgen.LayeredOreConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class TFMGConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
            OIL_DEPOSIT = key("oil_deposit"),
            OIL_WELL = key("oil_well"),
            LEAD_ORE = key("lead_ore"),
            NICKEL_ORE = key("nickel_ore"),
            LITHIUM_ORE = key("lithium_ore"),
            BAUXITE = key("bauxite"),
            GALENA = key("galena"),
            LIGNITE = key("lignite"),
            FIRECLAY = key("fireclay"),
            SULFUR = key("sulfur"),
            NETHER_FIRECLAY = key("nether_fireclay");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, TFMG.asResource(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);



        List<OreConfiguration.TargetBlockState> leadTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceables, TFMGBlocks.LEAD_ORE.get()
                        .defaultBlockState()),
                OreConfiguration.target(deepslateOreReplaceables, TFMGBlocks.DEEPSLATE_LEAD_ORE.get()
                        .defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> nickelTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceables, TFMGBlocks.NICKEL_ORE.get()
                        .defaultBlockState()),
                OreConfiguration.target(deepslateOreReplaceables, TFMGBlocks.DEEPSLATE_NICKEL_ORE.get()
                        .defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> lithiumTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceables, TFMGBlocks.LITHIUM_ORE.get()
                        .defaultBlockState()),
                OreConfiguration.target(deepslateOreReplaceables, TFMGBlocks.DEEPSLATE_LITHIUM_ORE.get()
                        .defaultBlockState())
        );


        register(ctx, OIL_DEPOSIT, TFMGFeatures.OIL_DEPOSIT.get(), new DepositFeatureConfig(
                UniformInt.of(0, 20),
                UniformInt.of(0, 6),
                UniformInt.of(0, 40),
                UniformInt.of(10, 25),
                true,
                TFMGBlocks.OIL_DEPOSIT.getDefaultState(),
                TFMGFluids.CRUDE_OIL.get().getSource(),
                new DepositFeatureConfig.TargetBlockState(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), TFMGBlocks.FOSSILSTONE.getDefaultState())
        ));
        register(ctx, OIL_WELL, TFMGFeatures.OIL_WELL.get(), new WellFeatureConfig(
                UniformInt.of(0, 7),
                UniformInt.of(0, 12),
                UniformInt.of(0, 10),
                List.of(new TagMatchTest(Tags.Blocks.SANDS)),
                true,
                TFMGBlocks.OIL_DEPOSIT.getDefaultState(),
                TFMGFluids.CRUDE_OIL.get().getSource(),
                new WellFeatureConfig.TargetBlockState(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), TFMGBlocks.FOSSILSTONE.getDefaultState())
        ));

        register(ctx, LEAD_ORE, Feature.ORE, new OreConfiguration(leadTargetStates, 12));
        register(ctx, NICKEL_ORE, Feature.ORE, new OreConfiguration(nickelTargetStates, 10));
        register(ctx, LITHIUM_ORE, Feature.ORE, new OreConfiguration(lithiumTargetStates, 7));

        register(ctx, BAUXITE, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.BAUXITE.get()), 32, 0));
        register(ctx, GALENA, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.GALENA.get()), 32, 0));
        register(ctx, LIGNITE, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.LIGNITE.get()), 32, 0));
        register(ctx, FIRECLAY, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.FIRECLAY.get()), 32, 0));

        register(ctx, SULFUR, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.SULFUR.get()), 32, 0));
        register(ctx, NETHER_FIRECLAY, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(List.of(TFMGLayeredPatterns.FIRECLAY_NETHER.get()), 32, 0));
    }
}
