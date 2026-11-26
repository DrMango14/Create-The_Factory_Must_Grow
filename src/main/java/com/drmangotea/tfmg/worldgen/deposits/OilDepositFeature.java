package com.drmangotea.tfmg.worldgen.deposits;


import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.mojang.serialization.Codec;
import net.createmod.catnip.data.Iterate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class OilDepositFeature extends Feature<DepositFeatureConfig> {
    public OilDepositFeature(Codec<DepositFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DepositFeatureConfig> context) {
        BlockPos startingPos = context.origin();
        WorldGenLevel level = context.level();
        BlockPos pos = startingPos;

        DepositFeatureConfig config = context.config();
        RandomSource random = context.random();

        if (config.discardChance().sample(random) != 0)
            return false;

        for (int i = 0; i < config.clusterSize().sample(random) + 1; i++) {
            placeDeposit(pos, level, random, config);
            pos = pos.north(config.distancing().sample(random) - 20);
            pos = pos.west(config.distancing().sample(random) - 20);
        }
        return true;
    }

    public void placeDeposit(BlockPos startingPos, WorldGenLevel level, RandomSource random, DepositFeatureConfig config) {
        BlockPos pos = startingPos;
        setBlock(level, startingPos, config.deposit());
		//resevoir handling
        if (config.linkReservoir()) {
            ChunkAccess chunk = level.getChunk(startingPos);
            if (chunk.hasData(TFMGDataAttachments.FLUID_RESERVOIR)) {
                FluidReservoir.addToReservoir(chunk, startingPos);
            } else {
                FluidReservoir.createReservoir(chunk, startingPos, level.getRandom());
            }
        }

        int height = config.height().sample(random);
        for (int i = 0; i < height; i++) {
            pos = pos.above();

            if (level.getBlockState(pos).isAir()) continue;

            setBlock(level, pos, config.fluid().defaultFluidState().createLegacyBlock());

            Direction crudeBranchDir = Util.getRandom(Iterate.horizontalDirections, random);
            BlockPos crudeBranch = pos.relative(crudeBranchDir);
            if (!level.getBlockState(crudeBranch).isAir()) {
                setBlock(level, crudeBranch, config.fluid().defaultFluidState().createLegacyBlock());
            }

            if (i < (height / 2)) {
                Direction fossilDir = Util.getRandom(Iterate.horizontalDirections, random);
                BlockPos fossil = pos.relative(fossilDir);
                if (config.sideStone().target().test(level.getBlockState(fossil), random)) {
                    setBlock(level, fossil, config.sideStone().state());
                }
            }
        }
    }
    
    public static void setBlock(WorldGenLevel level,BlockPos pos, BlockState state){
        level.setBlock(pos,state,2);
    }
}
