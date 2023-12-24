package com.drmangotea.createindustry.blocks.engines.small.turbine;


import com.drmangotea.createindustry.blocks.engines.small.AbstractEngineTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.EngineBackPartBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TurbineEngineTileEntity extends AbstractEngineTileEntity {


    public TurbineEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public EngineBackPartBlock backPartBlock() {
        return TFMGBlocks.TURBINE_ENGINE_BACK.get();
    }
}
