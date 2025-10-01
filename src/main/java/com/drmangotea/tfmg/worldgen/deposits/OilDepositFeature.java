package com.drmangotea.tfmg.worldgen.deposits;


import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class OilDepositFeature extends Feature<NoneFeatureConfiguration> {
    public OilDepositFeature(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        BlockPos startingPos = context.origin();
        WorldGenLevel level = context.level();
        BlockPos pos = startingPos;
        RandomSource randomsource = context.random();

        if (randomsource.nextInt(20) != 0)
            return false;

        for (int i = 0; i < randomsource.nextInt(6) + 1; i++) {
            placeDeposit(pos, level, randomsource);
            pos = pos.north(randomsource.nextInt(40) - 20);
            pos = pos.west(randomsource.nextInt(40) - 20);
        }

        return true;
    }

    public void placeDeposit(BlockPos startingPos, WorldGenLevel level, RandomSource randomsource) {
        BlockPos pos = startingPos;
        setBlock(level, startingPos, TFMGBlocks.OIL_DEPOSIT.getDefaultState());


        for (int i = 0; i < randomsource.nextInt(25); i++) {
            pos = pos.above();

            setBlock(level, pos, TFMGFluids.CRUDE_OIL.get().getSource().defaultFluidState().createLegacyBlock());


            Direction direction1 = Direction.getRandom(randomsource);
            if (direction1.getAxis().isHorizontal())
                setBlock(level, pos.relative(direction1), TFMGFluids.CRUDE_OIL.get().getSource().defaultFluidState().createLegacyBlock());

            if (i < 4) {
                Direction direction2 = Direction.getRandom(randomsource);
                if (direction2.getAxis().isHorizontal())
                    setBlock(level, pos.relative(direction2), TFMGBlocks.FOSSILSTONE.getDefaultState());
            }

        }
    }
    
    
    public static void setBlock(WorldGenLevel level,BlockPos pos, BlockState state){


        
        level.setBlock (pos,state,2);
    }
}
