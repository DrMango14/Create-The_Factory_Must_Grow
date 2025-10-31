package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire.GreenFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class GreenSpark extends Spark{

    public GreenSpark(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }


    public float[] getCustomParticleTrail() {
        return new float[]{0.01f, 100.25f, 20.1f};
    }


    @Override
    public int getColor() {
        return 0x4CFF00;
    }

    @Override
    public Optional<BlockState> getFireState(BlockPos pos) {
        return Optional.of(GreenFireBlock.getState(this.level(),pos));
    }
}
