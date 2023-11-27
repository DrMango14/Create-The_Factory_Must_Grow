package com.drmangotea.tfmg.blocks.concrete.formwork.rebar;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RebarFormWorkBlock extends Block implements IWrenchable, IBE<RebarFormWorkBlockEntity> {
    public RebarFormWorkBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<RebarFormWorkBlockEntity> getBlockEntityClass() {
        return RebarFormWorkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RebarFormWorkBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.REBAR_FORMWORK.get();
    }
}
