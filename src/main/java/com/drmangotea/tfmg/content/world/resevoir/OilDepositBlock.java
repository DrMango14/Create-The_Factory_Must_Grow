package com.drmangotea.tfmg.content.world.resevoir;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OilDepositBlock extends Block implements IBE<OilDepositBlockEntity> {
    public OilDepositBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<OilDepositBlockEntity> getBlockEntityClass() {
        return OilDepositBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends OilDepositBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.OIL_DEPOSIT.get();
    }
}
