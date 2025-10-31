package com.drmangotea.tfmg.content.items.weapons.lithium_blade;

import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class LithiumBladeItem extends SwordItem {


    public static final int MAX_TIME = 2000;

    public LithiumBladeItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        ItemStack stack1 = new ItemStack(TFMGItems.LIT_LITHIUM_BLADE, 1, stack.getComponentsPatch());

        var lookup = net.neoforged.neoforge.common.CommonHooks.resolveLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
        ItemEnchantments enchantments = stack.getAllEnchantments(lookup);


        for(int i = 0;i<enchantments.size();i++){
            stack1.enchant(enchantments.keySet().stream().toList().get(i),enchantments.getLevel(enchantments.keySet().stream().toList().get(i)));
        }


        int slot = -1;


        for(int i=0;i<player.getInventory().getContainerSize();i++){

            if(player.getInventory().getItem(i).is(TFMGItems.LITHIUM_CHARGE.get())){
                slot = i;
                break;

            }


        }


        if(slot==-1)
            return super.use(pLevel, player, hand);

        stack1.set(TFMGDataComponents.LITHIUM_BLADE_TIMER,MAX_TIME);


        pLevel.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);
        player.getInventory().getItem(slot).shrink(1);
        player.setItemInHand(hand, stack1);





        return super.use(pLevel, player, hand);
    }



}
