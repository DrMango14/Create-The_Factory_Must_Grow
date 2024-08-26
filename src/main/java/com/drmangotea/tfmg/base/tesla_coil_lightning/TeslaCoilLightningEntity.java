package com.drmangotea.tfmg.base.tesla_coil_lightning;


import com.drmangotea.tfmg.items.weapons.explosives.napalm.NapalmBombEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;


public class TeslaCoilLightningEntity extends LightningBolt {


    public BlockPos coilPos;


    public TeslaCoilLightningEntity(EntityType<? extends LightningBolt> p_20865_, Level p_20866_) {
        super(p_20865_, p_20866_);
        coilPos = this.getOnPos().above(5).north(3);
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<TeslaCoilLightningEntity> entityBuilder = (EntityType.Builder<TeslaCoilLightningEntity>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}