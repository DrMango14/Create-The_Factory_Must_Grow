package com.drmangotea.tfmg.blocks.deposits;



import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public  class FluidDepositBlock extends Block implements IBE<FluidDepositBlockEntity> {

    public FluidDepositBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<FluidDepositBlockEntity> getBlockEntityClass() {
        return FluidDepositBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidDepositBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.OIL_DEPOSIT.get();
    }
}
