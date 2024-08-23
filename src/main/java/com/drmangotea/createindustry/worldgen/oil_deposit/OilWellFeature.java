package com.drmangotea.createindustry.worldgen.oil_deposit;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;

public class OilWellFeature extends Feature<NoneFeatureConfiguration> {
    public OilWellFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        BlockPos startingPos = context.origin();
        WorldGenLevel level = context.level();
        BlockPos pos = startingPos;
        RandomSource randomsource = context.random();

        ChunkGenerator chunkGenerator = context.chunkGenerator();

        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG,pos.getX(),pos.getZ())+70+randomsource.nextInt(12);





        for(int i = 0; i < height;i++){

            if(i==0) {
                level.setBlock(startingPos, TFMGBlocks.OIL_DEPOSIT.getDefaultState(), 2);
                pos = pos.above();
                continue;
            }



//            if(randomsource.nextInt(10)==7) {



            for(Direction direction : Iterate.directions){
            if(randomsource.nextInt(3)==1)
                if(direction.getAxis().isHorizontal()&&level.getBlockState(pos.relative(direction)).is(Blocks.STONE)){
                    level.setBlock(pos.relative(direction), TFMGBlocks.FOSSILSTONE.getDefaultState(), 2);
                }
            //if(i<height-2)
            //     if(direction.getAxis().isHorizontal()&&level.getBlockState(pos.relative(direction)).is(Blocks.AIR)){
            //         level.setBlock(pos.relative(direction), TFMGFluids.CRUDE_OIL.getSource().getFlowing().defaultFluidState().createLegacyBlock(), 3);
            //     }

            }

            if(i==height-18){
                AABB area = new AABB(pos).inflate(10);


                for (BlockPos pos1 : BlockPos.betweenClosed(new BlockPos(area.minX,area.minY,area.minZ), new BlockPos(area.maxX,area.maxY,area.maxZ))) {

                    if(randomsource.nextInt(10)==7){
                        if(level.getFluidState(pos1).is(Fluids.WATER)||level.getBlockState(pos1).is(Tags.Blocks.SAND)) {
                            level.setBlock(pos1, TFMGFluids.CRUDE_OIL.getSource().getSource(true).createLegacyBlock(), 3);
                            if(level.getBlockState(pos1).is(Tags.Blocks.SAND))
                                level.getBlockState(pos1).updateShape(Direction.NORTH,level.getBlockState(pos1),level,pos1,pos1);
                        }

                    }


                }
            }


            level.setBlock(pos, TFMGFluids.CRUDE_OIL.getSource().getSource(true).createLegacyBlock(), 3);
            level.getBlockState(pos).updateShape(Direction.NORTH,level.getBlockState(pos),level,pos,pos);



            pos = pos.above();





        }






        return true;
    }
}
