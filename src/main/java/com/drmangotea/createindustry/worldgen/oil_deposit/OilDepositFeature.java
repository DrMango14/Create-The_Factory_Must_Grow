package com.drmangotea.createindustry.worldgen.oil_deposit;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;


public class OilDepositFeature  extends Feature<NoneFeatureConfiguration> {
    public OilDepositFeature(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        BlockPos startingPos = context.origin();
        WorldGenLevel level = context.level();
        BlockPos pos = startingPos;
        RandomSource randomsource = context.random();


        level.setBlock(startingPos, TFMGBlocks.OIL_DEPOSIT.getDefaultState(),2);


        for(int i = 0; i < randomsource.nextInt(25);i++){
            pos = pos.above();

            level.setBlock(pos, TFMGFluids.CRUDE_OIL.get().getSource().defaultFluidState().createLegacyBlock(), 2);


                Direction direction1 = Direction.getRandom(randomsource);
                if(direction1.getAxis().isHorizontal())
                    level.setBlock(pos.relative(direction1),TFMGFluids.CRUDE_OIL.get().getSource().defaultFluidState().createLegacyBlock(), 2);

            if(i<4) {
                Direction direction2 = Direction.getRandom(randomsource);
                if (direction2.getAxis().isHorizontal())
                    level.setBlock(pos.relative(direction2), TFMGBlocks.FOSSILSTONE.getDefaultState(), 2);
            }

        }





        return true;
    }
}
