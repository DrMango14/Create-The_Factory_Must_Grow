package com.drmangotea.createindustry.items.weapons.lithium_blade;

import com.drmangotea.createindustry.registry.TFMGItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.Map;

public class LithiumBladeItem extends SwordItem {


    public static final int MAX_TIME = 2000;

    public LithiumBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        ItemStack stack1 = new ItemStack(TFMGItems.LIT_LITHIUM_BLADE.get(), 1, stack.getOrCreateTag());


        Map<Enchantment, Integer> enchantments = stack.getAllEnchantments();

        enchantments.forEach(stack1::enchant);



        int slot = -1;


        for(int i=0;i<player.getInventory().getContainerSize();i++){

            if(player.getInventory().getItem(i).is(TFMGItems.LITHIUM_CHARGE.get())){
                slot = i;
                break;

            }


        }


        if(slot==-1)
            return super.use(pLevel, player, hand);

        stack1.getOrCreateTag().putInt("time",MAX_TIME);


        pLevel.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);
        player.getInventory().getItem(slot).shrink(1);
        player.setItemInHand(hand, stack1);





        return super.use(pLevel, player, hand);
    }



}
