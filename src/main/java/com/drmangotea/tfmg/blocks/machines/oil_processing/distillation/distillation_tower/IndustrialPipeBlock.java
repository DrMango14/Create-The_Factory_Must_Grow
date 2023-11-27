package com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower;


import com.drmangotea.tfmg.registry.TFMGShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IndustrialPipeBlock extends Block {


    public IndustrialPipeBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

     return TFMGShapes.INDUSTRIAL_PIPE;
    }

}
