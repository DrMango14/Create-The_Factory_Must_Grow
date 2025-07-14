package com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower;


import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.decoration.concrete.SimpleConcreteloggedBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class IndustrialPipeBlock extends SimpleConcreteloggedBlock {


    public IndustrialPipeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        tickDrying(level,state,TFMGBlocks.CONCRETE_ENCASED_INDUSTRIAL_PIPE.getDefaultState(),pos, randomSource);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TFMGShapes.INDUSTRIAL_PIPE;
    }

}
