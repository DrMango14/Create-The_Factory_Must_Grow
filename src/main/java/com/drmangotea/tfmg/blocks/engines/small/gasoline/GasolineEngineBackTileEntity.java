package com.drmangotea.tfmg.blocks.engines.small.gasoline;


import com.drmangotea.tfmg.blocks.engines.small.EngineBackBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GasolineEngineBackTileEntity extends EngineBackBlockEntity implements IHaveGoggleInformation {


    public GasolineEngineBackTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}

