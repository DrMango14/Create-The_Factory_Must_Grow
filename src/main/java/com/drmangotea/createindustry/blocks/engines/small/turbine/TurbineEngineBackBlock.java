package com.drmangotea.createindustry.blocks.engines.small.turbine;


import com.drmangotea.createindustry.blocks.engines.small.EngineBackPartBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TurbineEngineBackBlock extends EngineBackPartBlock implements IBE<TurbineEngineBackTileEntity> {
    public TurbineEngineBackBlock(Properties properties) {
        super(properties);

    }
    @Override
    public Class<TurbineEngineBackTileEntity> getBlockEntityClass() {
        return TurbineEngineBackTileEntity.class;
    }

    @Override
    public BlockEntityType<TurbineEngineBackTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.TURBINE_ENGINE_BACK.get();
    }
}
