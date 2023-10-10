package com.drmangotea.tfmg.blocks.machines.metal_processing.casting_basin;

import net.minecraft.world.item.Item;

public class CastingMoldItem extends Item {


    private final CastingBasinBlockEntity.MoldType type;
    public CastingMoldItem(Properties p_41383_, CastingBasinBlockEntity.MoldType type) {
        super(p_41383_);
        this.type = type;
    }


    public CastingBasinBlockEntity.MoldType getType(){
        return this.type;
    }

}
