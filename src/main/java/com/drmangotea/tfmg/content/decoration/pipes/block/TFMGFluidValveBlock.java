package com.drmangotea.tfmg.content.decoration.pipes.block;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.fluids.pipes.IAxisPipe;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TFMGFluidValveBlock extends FluidValveBlock implements IAxisPipe, IBE<FluidValveBlockEntity>, ProperWaterloggedBlock {

    public TFMGFluidValveBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ENABLED, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public Class<FluidValveBlockEntity> getBlockEntityClass() {
        return FluidValveBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidValveBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TFMG_FLUID_VALVE.get();
    }
}
