package com.drmangotea.createindustry.mixins;

import com.drmangotea.createindustry.registry.TFMGPotions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(AreaEffectCloud.class)
public abstract class AreaEffectCloudMixin extends Entity {
    
    public AreaEffectCloudMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Shadow private Potion potion;
    
    @Shadow public abstract float getRadius();
    
    @Shadow public abstract boolean isWaiting();
    
    @Inject(at = @At("TAIL"),method = "tick",remap = false)
    public void tick(CallbackInfo ci) {
        int $$5 = isWaiting() ? 2 : Mth.ceil(3.1415927F * getRadius() * getRadius());
        float $$6 = isWaiting() ? 0.2F : getRadius();
        for(int $$7 = 0; $$7 < $$5; ++$$7) {
            float $$8 = this.random.nextFloat() * 6.2831855F;
            float $$9 = Mth.sqrt(this.random.nextFloat()) * $$6;
            double $$10 = this.getX() + (double)(Mth.cos($$8) * $$9);
            double $$11 = this.getY();
            double $$12 = this.getZ() + (double)(Mth.sin($$8) * $$9);
            double $$17;
            double $$18;
            double $$22;
            if (isWaiting()) {
                $$17 = 0.0;
                $$18 = 0.0;
                $$22 = 0.0;
            } else {
                $$17 = (0.5 - this.random.nextDouble()) * 0.15;
                $$18 = 0.009999999776482582;
                $$22 = (0.5 - this.random.nextDouble()) * 0.15;
            }
            if(potion == TFMGPotions.HELLFIRE_POTION.get())
                this.level.addAlwaysVisibleParticle(ParticleTypes.FLAME, $$10, $$11, $$12, $$17, $$18, $$22);
            if(potion == TFMGPotions.FROSTY_POTION.get())
                this.level.addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, $$10, $$11, $$12, $$17, $$18, $$22);
        }
        
    }
}
