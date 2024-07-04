package com.drmangotea.createindustry.blocks.engines.small.gasoline;


import com.drmangotea.createindustry.blocks.engines.small.AbstractEngineBlockEntity;
import com.drmangotea.createindustry.registry.TFMGFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class GasolineEngineTileEntity extends AbstractEngineBlockEntity {


    public GasolineEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

}
