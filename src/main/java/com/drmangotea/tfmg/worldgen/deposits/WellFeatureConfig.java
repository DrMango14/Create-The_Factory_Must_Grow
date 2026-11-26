package com.drmangotea.tfmg.worldgen.deposits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public record WellFeatureConfig(IntProvider discardChance, IntProvider exposedHeight, IntProvider spillageChance, List<RuleTest> spillageReplaces, boolean linkReservoir, BlockState deposit, Fluid fluid, TargetBlockState sideStone) implements FeatureConfiguration {

    public static final Codec<WellFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            IntProvider.CODEC.fieldOf("discard_chance").forGetter(WellFeatureConfig::discardChance),
            IntProvider.CODEC.fieldOf("exposed_height").forGetter(WellFeatureConfig::exposedHeight),
            IntProvider.CODEC.fieldOf("spillage_chance").forGetter(WellFeatureConfig::spillageChance),
            RuleTest.CODEC.listOf().fieldOf("spillage_replaces").forGetter(WellFeatureConfig::spillageReplaces),
            Codec.BOOL.optionalFieldOf("reservoir", true).forGetter(WellFeatureConfig::linkReservoir),
            BlockState.CODEC.fieldOf("deposit").forGetter(WellFeatureConfig::deposit),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(WellFeatureConfig::fluid),
            TargetBlockState.CODEC.fieldOf("side_stone").forGetter(WellFeatureConfig::sideStone)
    ).apply(instance, WellFeatureConfig::new));

    public record TargetBlockState(RuleTest target, BlockState state) {
        public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                RuleTest.CODEC.fieldOf("target").forGetter(TargetBlockState::target),
                BlockState.CODEC.fieldOf("state").forGetter(TargetBlockState::state)
        ).apply(instance, TargetBlockState::new));
    }
}
