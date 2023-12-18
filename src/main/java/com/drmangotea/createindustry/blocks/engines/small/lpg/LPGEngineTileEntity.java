package com.drmangotea.createindustry.blocks.engines.small.lpg;


import com.drmangotea.createindustry.blocks.engines.small.AbstractEngineTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.EngineBackPartBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class LPGEngineTileEntity extends AbstractEngineTileEntity {


    public LPGEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public EngineBackPartBlock backPartBlock() {
        return TFMGBlocks.LPG_ENGINE_BACK.get();
    }

    @Override
    public Fluid validFuel() {
        return TFMGFluids.LPG.getSource();
    }
}
