package com.drmangotea.createindustry.blocks.engines.small.lpg;


import com.drmangotea.createindustry.blocks.engines.small.EngineBackPartBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LPGEngineBackBlock extends EngineBackPartBlock implements IBE<LPGEngineBackTileEntity> {
    public LPGEngineBackBlock(Properties properties) {
        super(properties);

    }
    @Override
    public Class<LPGEngineBackTileEntity> getBlockEntityClass() {
        return LPGEngineBackTileEntity.class;
    }

    @Override
    public BlockEntityType<LPGEngineBackTileEntity> getBlockEntityType() {
        return TFMGBlockEntities.LPG_ENGINE_BACK.get();
    }
}
