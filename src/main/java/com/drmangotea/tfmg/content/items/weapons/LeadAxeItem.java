package com.drmangotea.tfmg.content.items.weapons;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class LeadAxeItem extends AxeItem {
    public LeadAxeItem(Tier pTier, Properties pProperties) {
        super(pTier,pProperties);
    }
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, LivingEntity.getSlotForHand(pTarget.getUsedItemHand()));
        MobEffectInstance poison = pTarget.getEffect(MobEffects.POISON);

        ;


        if(poison!=null) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 160 + poison.getDuration()));
        }
        pTarget.addEffect(new MobEffectInstance(MobEffects.POISON,160));
        return true;
    }
}
