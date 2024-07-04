package com.drmangotea.createindustry.items.weapons;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class LeadSwordItem extends SwordItem {
    public LeadSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        MobEffectInstance poison = pTarget.getEffect(MobEffects.POISON);

        if(poison!=null) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100 + poison.getDuration()));
        }
        pTarget.addEffect(new MobEffectInstance(MobEffects.POISON,100));
        return true;
    }
}
