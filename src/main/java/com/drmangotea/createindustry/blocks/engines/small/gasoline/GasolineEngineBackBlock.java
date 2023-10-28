package com.drmangotea.createindustry.blocks.engines.small.gasoline;


import com.drmangotea.createindustry.blocks.engines.small.EngineBackPartBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GasolineEngineBackBlock extends EngineBackPartBlock implements IBE<GasolineEngineBackTileEntity> {
    public GasolineEngineBackBlock(Properties properties) {
        super(properties);

    }
    @Override
    public Class<GasolineEngineBackTileEntity> getBlockEntityClass() {
        return GasolineEngineBackTileEntity.class;
    }

    @Override
    public BlockEntityType<GasolineEngineBackTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.GASOLINE_ENGINE_BACK.get();
    }
}
