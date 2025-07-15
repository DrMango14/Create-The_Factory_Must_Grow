package com.drmangotea.tfmg.worldgen;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.infrastructure.worldgen.AllConfiguredFeatures;
import com.simibubi.create.infrastructure.worldgen.ConfigPlacementFilter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
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
            TFMG_STRIATED_ORES_OVERWORLD = key("tfmg_striated_ores_overworld"),
            TFMG_STRIATED_ORES_NETHER = key("tfmg_striated_ores_nether");

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
        Holder<ConfiguredFeature<?, ?>> striatedOresOverworld = featureLookup.getOrThrow(TFMGConfiguredFeatures.TFMG_STRIATED_ORES_OVERWORLD);
        Holder<ConfiguredFeature<?, ?>> striatedOresNether = featureLookup.getOrThrow(TFMGConfiguredFeatures.TFMG_STRIATED_ORES_NETHER);

        register(ctx, OIL_DEPOSIT,oilDeposit,oilPlacement(RarityFilter.onAverageOnceEvery(4)));

        register(ctx, OIL_WELL,oilWell,oilPlacement(RarityFilter.onAverageOnceEvery(500)));

        register(ctx, LEAD_ORE, leadOre, placement(CountPlacement.of(5), -15, 80));
        register(ctx, NICKEL_ORE, nickelOre, placement(CountPlacement.of(5), -63, 20));
        register(ctx, LITHIUM_ORE, lithiumOre, placement(CountPlacement.of(3), -63, -5));
        register(ctx, TFMG_STRIATED_ORES_OVERWORLD, striatedOresOverworld, placement(RarityFilter.onAverageOnceEvery(18), -30, 70));
        register(ctx, TFMG_STRIATED_ORES_NETHER, striatedOresNether, placement(RarityFilter.onAverageOnceEvery(18), 40, 90));
    }

    private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
                frequency,
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                ConfigPlacementFilter.INSTANCE
        );
    }
    private static List<PlacementModifier> oilPlacement(PlacementModifier frequency) {
        return List.of(
                frequency,
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-64)),
                ConfigPlacementFilter.INSTANCE
        );
    }
}
