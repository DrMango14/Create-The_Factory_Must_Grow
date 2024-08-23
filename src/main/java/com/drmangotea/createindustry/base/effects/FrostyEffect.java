package com.drmangotea.createindustry.base.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrostyEffect extends MobEffect {
    public FrostyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getTicksFrozen() > 1) {
            pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + 5);
        } else {
            pLivingEntity.setTicksFrozen(5);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
