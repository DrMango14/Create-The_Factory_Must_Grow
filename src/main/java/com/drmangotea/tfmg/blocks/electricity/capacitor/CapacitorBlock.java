package com.drmangotea.tfmg.blocks.electricity.capacitor;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class CapacitorBlock extends Block implements IBE<CapacitorBlockEntity> {



    public CapacitorBlock(Properties p_49795_) {
        super(p_49795_);

    }





    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

    }

    @Override
    public Class<CapacitorBlockEntity> getBlockEntityClass() {
        return CapacitorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CapacitorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CAPACITOR.get();
    }

}
