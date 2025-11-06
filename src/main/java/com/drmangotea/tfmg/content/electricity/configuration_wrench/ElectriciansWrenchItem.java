package com.drmangotea.tfmg.content.electricity.configuration_wrench;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.utilities.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import net.createmod.catnip.gui.ScreenOpener;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ElectriciansWrenchItem extends Item {
    public ElectriciansWrenchItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    openWandGUI(itemStack, hand);
                });
                player.getCooldowns()
                        .addCooldown(this, 5);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(!context.getPlayer().isShiftKeyDown()) {
            if (level.getBlockEntity(pos) instanceof IElectric be && be.canBeInGroups()) {
                be.updateNextTick();
                be.sendStuff();
                be.getData().group.id = context.getItemInHand().getOrCreateTag().getInt("Number");
                TFMGUtils.playSound(level, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, context.getPlayer());
                if(be instanceof ElectricMotorBlockEntity kineticBE)
                    kineticBE.delayedUpdate=true;


                return InteractionResult.SUCCESS;
            }
            if (level.getBlockEntity(pos) instanceof AbstractEngineBlockEntity be ) {

                be.changeDirection();
                return InteractionResult.SUCCESS;

            }

        }


        return super.useOn(context);
    }

    @OnlyIn(Dist.CLIENT)
    private void openWandGUI(ItemStack itemStack, InteractionHand hand) {
        ScreenOpener.open(new ElectriciansWrenchScreen(itemStack, hand));
    }
}
