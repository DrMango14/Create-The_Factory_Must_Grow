package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.output;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DistillationOutputBlock extends Block implements IBE<DistillationOutputBlockEntity>, IWrenchable {
    public DistillationOutputBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<DistillationOutputBlockEntity> getBlockEntityClass() {
        return DistillationOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationOutputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_DISTILLATION_OUTPUT.get();
    }
}
