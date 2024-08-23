package com.drmangotea.createindustry.blocks.electricity.base;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class ConverterBlock extends Block implements IBE<ConverterBlockEntity>, IWrenchable {



    public ConverterBlock(Properties p_49795_) {
        super(p_49795_);

    }





    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

    }

    @Override
    public Class<ConverterBlockEntity> getBlockEntityClass() {
        return ConverterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ConverterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CONVERTER.get();
    }

}
