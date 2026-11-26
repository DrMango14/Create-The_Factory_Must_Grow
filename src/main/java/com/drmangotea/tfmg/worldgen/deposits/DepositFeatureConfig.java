package com.drmangotea.tfmg.worldgen.deposits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

public record DepositFeatureConfig(IntProvider discardChance, IntProvider clusterSize, IntProvider distancing, IntProvider height, boolean linkReservoir, BlockState deposit, Fluid fluid, TargetBlockState sideStone) implements FeatureConfiguration {

    public static final Codec<DepositFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            IntProvider.CODEC.fieldOf("discard_chance").forGetter(DepositFeatureConfig::discardChance),
            IntProvider.CODEC.fieldOf("cluster_size").forGetter(DepositFeatureConfig::clusterSize),
            IntProvider.CODEC.fieldOf("distancing").forGetter(DepositFeatureConfig::distancing),
            IntProvider.CODEC.fieldOf("height").forGetter(DepositFeatureConfig::height),
            Codec.BOOL.optionalFieldOf("reservoir", true).forGetter(DepositFeatureConfig::linkReservoir),
            BlockState.CODEC.fieldOf("deposit").forGetter(DepositFeatureConfig::deposit),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(DepositFeatureConfig::fluid),
            TargetBlockState.CODEC.fieldOf("side_stone").forGetter(DepositFeatureConfig::sideStone)
    ).apply(instance, DepositFeatureConfig::new));

    public record TargetBlockState(RuleTest target, BlockState state) {
        public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                RuleTest.CODEC.fieldOf("target").forGetter(TargetBlockState::target),
                BlockState.CODEC.fieldOf("state").forGetter(TargetBlockState::state)
        ).apply(instance, TargetBlockState::new));
    }
}
