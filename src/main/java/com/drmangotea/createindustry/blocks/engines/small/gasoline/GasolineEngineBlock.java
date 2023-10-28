package com.drmangotea.createindustry.blocks.engines.small.gasoline;


import com.drmangotea.createindustry.blocks.engines.small.EngineBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GasolineEngineBlock extends EngineBlock implements IBE<GasolineEngineTileEntity> {
    public GasolineEngineBlock(Properties properties) {
        super(properties);

    }


    @Override
    public Class<GasolineEngineTileEntity> getBlockEntityClass() {
        return GasolineEngineTileEntity.class;
    }

    @Override
    public BlockEntityType<GasolineEngineTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.GASOLINE_ENGINE.get();
    }
}
