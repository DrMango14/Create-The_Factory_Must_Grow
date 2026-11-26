package com.drmangotea.tfmg.base;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class HellFireEffect extends MobEffect {
    public HellFireEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean tick(LivingEntity entity, MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance == null) return false;
        if (!entity.isOnFire()) {
            entity.setRemainingFireTicks(mobEffectInstance.getDuration());
        } else if (entity.getRemainingFireTicks() < mobEffectInstance.getDuration()) {
            entity.setRemainingFireTicks(mobEffectInstance.getDuration());
        }
        return true;
    }
}
