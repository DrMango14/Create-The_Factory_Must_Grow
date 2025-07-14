package com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
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
        return TFMGBlockEntities.DISTILLATION_OUTPUT.get();
    }
}
