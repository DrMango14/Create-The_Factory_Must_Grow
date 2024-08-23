package com.drmangotea.createindustry.blocks.electricity.resistors;


import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ResistorBlock extends TFMGHorizontalDirectionalBlock implements IBE<ResistorBlockEntity> {
    public ResistorBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public Class<ResistorBlockEntity> getBlockEntityClass() {
        return ResistorBlockEntity.class;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.RESISTOR.get(pState.getValue(FACING));
    }
    @Override
    public BlockEntityType<? extends ResistorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.RESISTOR.get();
    }
}
