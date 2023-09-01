package com.drmangotea.tfmg.content.concrete.formwork;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FormWorkBlock extends Block implements IWrenchable, IBE<FormWorkBlockEntity> {
    public FormWorkBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<FormWorkBlockEntity> getBlockEntityClass() {
        return FormWorkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FormWorkBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.FORMWORK.get();
    }
}
