package com.drmangotea.createindustry.mixins;


import com.drmangotea.createindustry.registry.TFMGPotions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Arrow.class)
public abstract class ArrowMixin extends AbstractArrow {

    @Shadow public abstract int getColor();

    @Shadow private boolean fixedColor;
    @Shadow private Potion potion;
    @Shadow @Final private Set<MobEffectInstance> effects;

    protected ArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(at = @At("HEAD"),method = "tick",remap = false)
    public void tick(CallbackInfo ci) {
        Vec3 vec3;
        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        if(potion == TFMGPotions.HELLFIRE_POTION.get()) {
            this.setSecondsOnFire(20);
            for(int i = 0; i < 4; ++i) {
                this.level.addAlwaysVisibleParticle(ParticleTypes.FLAME, this.getX() + d5 * (double)i / 4.0, this.getY() + d6 * (double)i / 4.0, this.getZ() + d1 * (double)i / 4.0, -d5, -d6 + 0.2, -d1);
            }
        }
        if(potion == TFMGPotions.FROSTY_POTION.get()) {
            for (int i = 0; i < 4; ++i) {
                this.level.addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, this.getX() + d5 * (double)i / 4.0, this.getY() + d6 * (double)i / 4.0, this.getZ() + d1 * (double)i / 4.0, -d5, -d6 + 0.2, -d1);
            }
        }
    }

    @Shadow
    protected ItemStack getPickupItem() {
        if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setPotion(itemstack, this.potion);
            PotionUtils.setCustomEffects(itemstack, this.effects);
            if (this.fixedColor) {
                itemstack.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
            }

            return itemstack;
        }
    }

}
