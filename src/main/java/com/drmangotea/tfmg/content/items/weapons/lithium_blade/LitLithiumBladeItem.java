package com.drmangotea.tfmg.content.items.weapons.lithium_blade;


import com.drmangotea.tfmg.base.spark.LithiumSpark;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LitLithiumBladeItem extends SwordItem {
    public LitLithiumBladeItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, LivingEntity.getSlotForHand(pAttacker.getUsedItemHand()));
        MobEffectInstance poison = pTarget.getEffect(TFMGMobEffects.HELLFIRE);

        if (poison != null) {
            pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 140 + poison.getDuration()));
        }
        pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 140));
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        //   :3

        ItemStack stack = player.getItemInHand(hand);

        if (stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) ==null){
            ItemStack stack1 = TFMGItems.LITHIUM_BLADE.asStack();
            player.setItemInHand(hand,stack1);
            return InteractionResultHolder.pass(stack1);
        }

        if (stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) <= 100)
            return super.use(level, player, hand);

        stack.set(TFMGDataComponents.LITHIUM_BLADE_TIMER, stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) - 100);

        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);


        for (int i = 0; i < 10; i++) {


            LithiumSpark spark = TFMGEntityTypes.LITHIUM_SPARK.create(level);



            spark.setPos(player.getX(), player.getY() + 1.3, player.getZ());


            spark.burst(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 1, 30);
            level.addFreshEntity(spark);
        }

        player.getCooldowns().addCooldown(TFMGItems.LIT_LITHIUM_BLADE.get(), 60);

        return super.use(level, player, hand);
    }


    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) != null;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0xDD0B13;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        if (pStack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) == null)
            return 0;

        return (int) ((((float) pStack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) / (float) LithiumBladeItem.MAX_TIME) * 12) + 1);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, pLevel, entity, pSlotId, pIsSelected);

        Player player = (Player) entity;
        if (stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) != null)
            if (stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) > 0) {
                stack.set(TFMGDataComponents.LITHIUM_BLADE_TIMER, stack.get(TFMGDataComponents.LITHIUM_BLADE_TIMER) - 1);


            } else {
                ItemStack stack1 = new ItemStack(TFMGItems.LITHIUM_BLADE, 1, stack.getComponentsPatch());

                var lookup = net.neoforged.neoforge.common.CommonHooks.resolveLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
                ItemEnchantments enchantments = stack.getAllEnchantments(lookup);
                for (int i = 0; i < enchantments.size(); i++) {
                    stack1.enchant(enchantments.keySet().stream().toList().get(i), enchantments.getLevel(enchantments.keySet().stream().toList().get(i)));
                }


                int slot = -1;

                if (entity instanceof Player)
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {

                        if (player.getInventory().getItem(i).is(TFMGItems.LITHIUM_CHARGE.get())) {
                            slot = i;
                            break;

                        }


                    }


                ((Player) entity).getInventory().setItem(pSlotId, stack1);
            }

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

        if (!slotChanged)
            return false;

        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }


}
