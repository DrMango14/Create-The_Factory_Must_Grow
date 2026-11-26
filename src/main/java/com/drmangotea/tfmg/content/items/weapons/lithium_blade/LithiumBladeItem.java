package com.drmangotea.tfmg.content.items.weapons.lithium_blade;

import com.drmangotea.tfmg.base.TFMGTiers;
import com.drmangotea.tfmg.base.spark.LithiumSpark;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LithiumBladeItem extends SwordItem {
    public static final int MAX_TIME = 2000;

    public LithiumBladeItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pStack.has(TFMGDataComponents.LITHIUM_BLADE_TIMER)) {
            pStack.hurtAndBreak(2, pAttacker, LivingEntity.getSlotForHand(pAttacker.getUsedItemHand()));
            MobEffectInstance hellfire = pTarget.getEffect(TFMGMobEffects.HELLFIRE);
            if (hellfire != null) {
                pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 140 + hellfire.getDuration()));
            }
            pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 140));
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.has(TFMGDataComponents.LITHIUM_BLADE_TIMER)) {
            int slot = -1;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).is(TFMGItems.LITHIUM_CHARGE.get())) {
                    slot = i;
                    break;
                }
            }
            if (slot == -1) return super.use(pLevel, player, hand);
            setCharge(stack, MAX_TIME);
            pLevel.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);
            player.getInventory().getItem(slot).shrink(1);
        } else {
            if (stack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) <= 100)
                return super.use(pLevel, player, hand);

            decrementCharge(stack, 100);
            pLevel.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);
            for (int i = 0; i < 10; i++) {
                LithiumSpark spark = TFMGEntityTypes.LITHIUM_SPARK.create(pLevel);
                if (spark == null) continue;
                spark.setPos(player.getX(), player.getY() + 1.3, player.getZ());
                spark.burst(player.getLookAngle(), 1, 30);
                pLevel.addFreshEntity(spark);
            }
            player.getCooldowns().addCooldown(TFMGItems.LITHIUM_BLADE.get(), 60);
        }

        return super.use(pLevel, player, hand);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.has(TFMGDataComponents.LITHIUM_BLADE_TIMER);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0xDD0B13;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        if (!pStack.has(TFMGDataComponents.LITHIUM_BLADE_TIMER))
            return 0;

        return (int) ((((float) pStack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) / (float) LithiumBladeItem.MAX_TIME) * 12) + 1);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, pLevel, entity, pSlotId, pIsSelected);
        decrementCharge(stack, 1);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged)
            return false;
        return super.shouldCauseReequipAnimation(oldStack, newStack, true);
    }

    public static void decrementCharge(ItemStack stack, int amount) {
        if (stack.has(TFMGDataComponents.LITHIUM_BLADE_TIMER)) {
            if (stack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) > 0) {
                int toDecrement = Math.max(0, stack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) - amount);
                if (stack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) - toDecrement > 0) {
                    stack.set(DataComponents.ATTRIBUTE_MODIFIERS, AxeItem.createAttributes(TFMGTiers.STEEL, 3, -2.4F));
                }
                stack.set(TFMGDataComponents.LITHIUM_BLADE_TIMER, toDecrement);
            } else {
                stack.remove(TFMGDataComponents.LITHIUM_BLADE_TIMER);
                stack.set(DataComponents.ATTRIBUTE_MODIFIERS, AxeItem.createAttributes(TFMGTiers.STEEL, 2, -2.4F));
            }
        }
    }

    public static void setCharge(ItemStack stack, int charge) {
        stack.set(TFMGDataComponents.LITHIUM_BLADE_TIMER, charge);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, AxeItem.createAttributes(TFMGTiers.STEEL, 3, -2.4F));
    }
}
