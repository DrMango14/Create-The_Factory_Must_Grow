package com.drmangotea.tfmg.content.electricity.configuration_wrench;

import net.createmod.catnip.gui.ScreenOpener;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public class ElectriciansWrenchItem extends Item {
    public ElectriciansWrenchItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> {
                    openWandGUI(itemStack, hand);
                });
                player.getCooldowns()
                        .addCooldown(this, 5);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }




    @OnlyIn(Dist.CLIENT)
    private void openWandGUI(ItemStack itemStack, InteractionHand hand) {
        ScreenOpener.open(new ElectriciansWrenchScreen(itemStack, hand));
    }
}
