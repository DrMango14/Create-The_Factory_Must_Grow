package com.drmangotea.tfmg.worldgen.deposits;


import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.mojang.serialization.Codec;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

public class OilWellFeature extends Feature<WellFeatureConfig> {
    public OilWellFeature(Codec<WellFeatureConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WellFeatureConfig> context) {
        BlockPos startingPos = context.origin();
        WorldGenLevel level = context.level();
        BlockPos pos = startingPos;

        WellFeatureConfig config = context.config();
        RandomSource random = context.random();

        if (config.discardChance().sample(random) <= 1)
            return false;

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

        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) + 70 + config.exposedHeight().sample(random);

        for (int i = 0; i < height; i++) {
            pos = pos.above();
            for (Direction direction : Iterate.horizontalDirections) {
                if (random.nextInt(3) == 1) {
                    if (direction.getAxis().isHorizontal() && config.sideStone().target().test(level.getBlockState(pos.relative(direction)), random)) {
                        setBlock(level, pos.relative(direction), config.sideStone().state());
                    }
                }
            }
            if (i == height - 18) {
                AABB area = new AABB(pos).inflate(10);
                for (BlockPos oilPos : BlockPos.betweenClosed(new BlockPos((int) area.minX, (int) area.minY, (int) area.minZ), new BlockPos((int) area.maxX, (int) area.maxY, (int) area.maxZ))) {
                    if (config.spillageChance().sample(random) == 7) {
                        if (level.getFluidState(oilPos).is(Fluids.WATER) || config.spillageReplaces().stream().anyMatch((rt) -> rt.test(level.getBlockState(oilPos), random))) {
                            setBlock(level, oilPos, config.fluid().defaultFluidState().setValue(FALLING, true).createLegacyBlock());
                            if (config.spillageReplaces().stream().anyMatch((rt) -> rt.test(level.getBlockState(oilPos), random)))
                                setBlock(level, oilPos, level.getBlockState(oilPos).updateShape(Direction.NORTH, level.getBlockState(oilPos), level, oilPos, oilPos));
                        }
                    }
                }
            }

            setBlock(level, pos, config.fluid().defaultFluidState().setValue(FALLING, true).createLegacyBlock());
            level.getBlockState(pos).updateShape(Direction.NORTH,level.getBlockState(pos),level,pos,pos);
        }
        return true;
    }

    public static void setBlock(WorldGenLevel level, BlockPos pos, BlockState state){
        level.setBlock(pos,state,2);
    }
}
