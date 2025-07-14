package com.drmangotea.tfmg.worldgen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.infrastructure.worldgen.AllFeatures;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.simibubi.create.infrastructure.worldgen.LayeredOreConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class TFMGConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
            OIL_DEPOSIT = key("oil_deposit"),
            OIL_WELL = key("oil_well"),
            LEAD_ORE = key("lead_ore"),
            NICKEL_ORE = key("nickel_ore"),
            LITHIUM_ORE = key("lithium_ore"),
            TFMG_STRIATED_ORES_OVERWORLD = key("tfmg_striated_ores_overworld"),
            TFMG_STRIATED_ORES_NETHER = key("tfmg_striated_ores_nether");

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


        register(ctx, OIL_DEPOSIT, TFMGFeatures.OIL_DEPOSIT.get(),new NoneFeatureConfiguration());
        register(ctx, OIL_WELL, TFMGFeatures.OIL_WELL.get(),new NoneFeatureConfiguration());

        register(ctx, LEAD_ORE, Feature.ORE, new OreConfiguration(leadTargetStates, 12));
        register(ctx, NICKEL_ORE, Feature.ORE, new OreConfiguration(nickelTargetStates, 10));
        register(ctx, LITHIUM_ORE, Feature.ORE, new OreConfiguration(lithiumTargetStates, 7));

        List<LayerPattern> overworldLayerPatterns = List.of(
                TFMGLayeredPatterns.BAUXITE.get(),
                TFMGLayeredPatterns.GALENA.get(),
                TFMGLayeredPatterns.LIGNITE.get(),
                TFMGLayeredPatterns.FIRECLAY.get()

        );

        register(ctx, TFMG_STRIATED_ORES_OVERWORLD, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(overworldLayerPatterns, 32, 0));

        List<LayerPattern> netherLayerPatterns = List.of(
                TFMGLayeredPatterns.SULFUR.get(),
                TFMGLayeredPatterns.FIRECLAY_NETHER.get()
        );

        register(ctx, TFMG_STRIATED_ORES_NETHER, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(netherLayerPatterns, 32, 0));
    }
}
