package com.drmangotea.createindustry.blocks.engines.small.turbine;


import com.drmangotea.createindustry.blocks.engines.small.EngineBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TurbineEngineBlock extends EngineBlock implements IBE<TurbineEngineTileEntity> {
    public TurbineEngineBlock(Properties properties) {
        super(properties);

    }
    @Override
    public Class<TurbineEngineTileEntity> getBlockEntityClass() {
        return TurbineEngineTileEntity.class;
    }

    @Override
    public BlockEntityType<TurbineEngineTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.TURBINE_ENGINE.get();
    }
}
