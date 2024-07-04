package com.drmangotea.createindustry.blocks.electricity.generation.creative_generator;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CreativeGeneratorBlock extends Block implements IBE<CreativeGeneratorBlockEntity> {
    public CreativeGeneratorBlock(Properties p_49795_) {
        super(p_49795_);
    }




    @Override
    public Class<CreativeGeneratorBlockEntity> getBlockEntityClass() {
        return CreativeGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CreativeGeneratorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CREATIVE_GENERATOR.get();
    }
}
