package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.base.HellFireEffect;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;applyEffectTick(Lnet/minecraft/world/entity/LivingEntity;I)Z"))
    private boolean onEffectTick(MobEffect effect, LivingEntity entity, int amplifier, Operation<Boolean> original) {
        if (effect instanceof HellFireEffect hellfire) {
            return hellfire.tick(entity, (MobEffectInstance) (Object) this);
        }
        return original.call(effect, entity, amplifier);
    }
}
