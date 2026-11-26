package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@NothingNullByDefault
public class LithiumSpark extends Spark{

    public LithiumSpark(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }


    public float[] getCustomParticleTrail() {
        return new float[]{100, 0, 0};
    }
	
	protected void onHitLiving(LivingEntity living) {
		living.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 60));
	}


    @Override
    public int getColor() {
        return 0xCC0000;
    }


    @Override
    public Optional<BlockState> getFireState(BlockPos pos) {
        return Optional.empty();
    }
}
