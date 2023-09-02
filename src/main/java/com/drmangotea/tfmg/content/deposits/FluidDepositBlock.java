package com.drmangotea.tfmg.content.deposits;



import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public  class FluidDepositBlock extends Block implements IBE<FluidDepositTileEntity> {

    public FluidDepositBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<FluidDepositTileEntity> getBlockEntityClass() {
        return FluidDepositTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidDepositTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.OIL_DEPOSIT.get();
    }
}
