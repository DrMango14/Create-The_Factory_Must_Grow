package com.drmangotea.tfmg.content.engines;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerItem;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


import java.util.List;

public class FluidContainingItem extends Item {

    public final FluidEntry<?> fluid;

    public static final int CAPACITY = 4000;

    public FluidContainingItem(Properties p_41383_, FluidEntry<?> fluid) {
        super(p_41383_);
        this.fluid = fluid;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(TFMGLang.translateDirect("tooltip.fluid_item", stack.get(TFMGDataComponents.AMOUNT)==null?0:stack.get(TFMGDataComponents.AMOUNT))
                .withStyle(ChatFormatting.GREEN)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }



    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();


        if (context.getPlayer().isShiftKeyDown()&&stack.get(TFMGDataComponents.AMOUNT)>0) {

            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
            stack.set(TFMGDataComponents.AMOUNT, 0);
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) != null)
            if (level.getBlockEntity(pos) instanceof FluidTankBlockEntity fluidTankBe) {

                FluidTankBlockEntity be = fluidTankBe.isController() ? fluidTankBe : fluidTankBe.getControllerBE();

                if (be.getFluid(0).getFluid().isSame(fluid.get())) {

                    int toDrain = Math.min(CAPACITY - stack.get(TFMGDataComponents.AMOUNT), be.getFluid(0).getAmount());
                    if(toDrain == 0||context.getPlayer().getCooldowns().isOnCooldown(stack.getItem()))
                        return InteractionResult.PASS;
                    level.playSound(null, be.getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
                    be.getTankInventory().drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                    stack.set(TFMGDataComponents.AMOUNT, stack.get(TFMGDataComponents.AMOUNT) + toDrain);
                    context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);




                    return InteractionResult.SUCCESS;
                }
            }

        return InteractionResult.PASS;
    }
}
