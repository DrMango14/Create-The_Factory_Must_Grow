package com.drmangotea.createindustry.blocks.machines.metal_processing.casting_spout;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CastingSpoutBlock extends Block implements IBE<CastingSpoutBlockEntity> {
    public CastingSpoutBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<CastingSpoutBlockEntity> getBlockEntityClass() {
        return CastingSpoutBlockEntity.class;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.CASTING_SPOUT;
    }
    @Override
    public BlockEntityType<? extends CastingSpoutBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CASTING_SPOUT.get();
    }
}
