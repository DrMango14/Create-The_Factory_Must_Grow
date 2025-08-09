package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire.BlueFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BlueSpark extends Spark{

    public BlueSpark(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }
    @Override
    public int getColor() {
        return 0x00FFFF;
    }

    public float[] getCustomParticleTrail() {
        return new float[]{4.1f, 60.2f, 100.3f};
    }

    @Override
    public Optional<BlockState> getFireState(BlockPos pos) {
        return Optional.of(BlueFireBlock.getState(this.level(),pos));
    }
}
