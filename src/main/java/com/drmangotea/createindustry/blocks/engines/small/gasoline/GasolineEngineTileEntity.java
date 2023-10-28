package com.drmangotea.createindustry.blocks.engines.small.gasoline;


import com.drmangotea.createindustry.blocks.engines.small.AbstractEngineTileEntity;
import com.drmangotea.createindustry.registry.TFMGFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class GasolineEngineTileEntity extends AbstractEngineTileEntity {


    public GasolineEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public Fluid validFuel() {
        return TFMGFluids.GASOLINE.getSource();
    }
}
