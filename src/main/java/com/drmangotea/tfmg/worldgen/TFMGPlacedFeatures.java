package com.drmangotea.tfmg.worldgen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.world.placement_modifier.BooleanConfigPlacementModifier;
import com.drmangotea.tfmg.content.world.placement_modifier.WorldBottomModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static net.minecraft.data.worldgen.placement.PlacementUtils.register;

public class TFMGPlacedFeatures {
    public static final ResourceKey<PlacedFeature>
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

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, TFMG.asResource(name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> featureLookup = ctx.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> oilDeposit = featureLookup.getOrThrow(TFMGConfiguredFeatures.OIL_DEPOSIT);
        Holder<ConfiguredFeature<?, ?>> oilWell = featureLookup.getOrThrow(TFMGConfiguredFeatures.OIL_WELL);

        Holder<ConfiguredFeature<?, ?>> leadOre = featureLookup.getOrThrow(TFMGConfiguredFeatures.LEAD_ORE);
        Holder<ConfiguredFeature<?, ?>> nickelOre = featureLookup.getOrThrow(TFMGConfiguredFeatures.NICKEL_ORE);
        Holder<ConfiguredFeature<?, ?>> lithiumOre = featureLookup.getOrThrow(TFMGConfiguredFeatures.LITHIUM_ORE);

        Holder<ConfiguredFeature<?, ?>> bauxite = featureLookup.getOrThrow(TFMGConfiguredFeatures.BAUXITE);
        Holder<ConfiguredFeature<?, ?>> galena = featureLookup.getOrThrow(TFMGConfiguredFeatures.GALENA);
        Holder<ConfiguredFeature<?, ?>> lignite = featureLookup.getOrThrow(TFMGConfiguredFeatures.LIGNITE);
        Holder<ConfiguredFeature<?, ?>> fireclay = featureLookup.getOrThrow(TFMGConfiguredFeatures.FIRECLAY);

        Holder<ConfiguredFeature<?, ?>> sulfur = featureLookup.getOrThrow(TFMGConfiguredFeatures.SULFUR);
        Holder<ConfiguredFeature<?, ?>> netherFireclay = featureLookup.getOrThrow(TFMGConfiguredFeatures.NETHER_FIRECLAY);

        register(ctx, OIL_DEPOSIT, oilDeposit, oilPlacement(RarityFilter.onAverageOnceEvery(4), new BooleanConfigPlacementModifier(TFMG.asResource("oil_deposits"), true)));

        register(ctx, OIL_WELL, oilWell, oilPlacement(RarityFilter.onAverageOnceEvery(500), new BooleanConfigPlacementModifier(TFMG.asResource("oil_wells"), true)));

        register(ctx, LEAD_ORE, leadOre, placement(CountPlacement.of(5), -15, 80, new BooleanConfigPlacementModifier(TFMG.asResource("lead_ore"), true)));
        register(ctx, NICKEL_ORE, nickelOre, placement(CountPlacement.of(5), -63, 20, new BooleanConfigPlacementModifier(TFMG.asResource("nickel_ore"), true)));
        register(ctx, LITHIUM_ORE, lithiumOre, placement(CountPlacement.of(3), -63, -5, new BooleanConfigPlacementModifier(TFMG.asResource("lithium_ore"), true)));

        register(ctx, BAUXITE, bauxite, placement(RarityFilter.onAverageOnceEvery(18), -30, 70, new BooleanConfigPlacementModifier(TFMG.asResource("bauxite"), true)));
        register(ctx, GALENA, galena, placement(RarityFilter.onAverageOnceEvery(18), -30, 70, new BooleanConfigPlacementModifier(TFMG.asResource("galena"), true)));
        register(ctx, LIGNITE, lignite, placement(RarityFilter.onAverageOnceEvery(18), -30, 70, new BooleanConfigPlacementModifier(TFMG.asResource("lignite"), true)));
        register(ctx, FIRECLAY, fireclay, placement(RarityFilter.onAverageOnceEvery(18), -30, 70, new BooleanConfigPlacementModifier(TFMG.asResource("fireclay"), true)));

        register(ctx, SULFUR, sulfur, placement(RarityFilter.onAverageOnceEvery(18), 40, 90, new BooleanConfigPlacementModifier(TFMG.asResource("sulfur"), true)));
        register(ctx, NETHER_FIRECLAY, netherFireclay, placement(RarityFilter.onAverageOnceEvery(18), 40, 90, new BooleanConfigPlacementModifier(TFMG.asResource("nether_fireclay"), true)));
    }

    private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight, BooleanConfigPlacementModifier configPlacementModifier) {
        return List.of(
                frequency,
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                configPlacementModifier
        );
    }
    private static List<PlacementModifier> oilPlacement(PlacementModifier frequency, BooleanConfigPlacementModifier configPlacementModifier) {
        return List.of(
                frequency,
                InSquarePlacement.spread(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.BEDROCK)),
                new WorldBottomModifier(),
                configPlacementModifier
        );
    }
}
