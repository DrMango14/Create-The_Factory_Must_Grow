package com.drmangotea.createindustry.base.multiblock;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FluidOutputBlock extends Block implements IBE<FluidOutputBlockEntity> {
    public FluidOutputBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<FluidOutputBlockEntity> getBlockEntityClass() {
        return FluidOutputBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends FluidOutputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.FLUID_OUTPUT.get();
    }
}
