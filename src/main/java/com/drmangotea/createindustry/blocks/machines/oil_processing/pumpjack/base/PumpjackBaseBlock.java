package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.base;

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

public class PumpjackBaseBlock extends Block implements IBE<PumpjackBaseBlockEntity> {
    public PumpjackBaseBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.PUMPJACK_BASE;
    }

    @Override
    public Class<PumpjackBaseBlockEntity> getBlockEntityClass() {
        return PumpjackBaseBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpjackBaseBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.PUMPJACK_BASE.get();
    }


}
