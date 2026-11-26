package com.drmangotea.tfmg.content.items;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.function.Predicate;

@NothingNullByDefault
public class FluidContainingItem extends Item {

    public final Predicate<FluidStack> validator;

    public static final int CAPACITY = 4000;

    public FluidContainingItem(Properties properties, Predicate<FluidStack> validator) {
        super(properties);
        this.validator = validator;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(TFMGLang.translateDirect("tooltip.fluid_item", stack.getOrDefault(TFMGDataComponents.AMOUNT, 0))
                .withStyle(ChatFormatting.GREEN)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.AMOUNT))
            return false;
        return stack.getOrDefault(TFMGDataComponents.AMOUNT, 0) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xC7C4A4;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.AMOUNT))
            return 0;

        return Math.round( 13* ((float)stack.getOrDefault(TFMGDataComponents.AMOUNT, 0) / (float)CAPACITY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player == null)
            return InteractionResult.PASS;

        if (player.isShiftKeyDown() && stack.getOrDefault(TFMGDataComponents.AMOUNT, 0) > 0) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1f, 1f);
            stack.set(TFMGDataComponents.AMOUNT, 0);
            return InteractionResult.SUCCESS;
        }

        int contained = stack.getOrDefault(TFMGDataComponents.AMOUNT, 0);

        BlockEntity blockEntity = level.getBlockEntity(pos);

        boolean foundFluid = false;

        if (blockEntity != null) {
            IFluidHandler capability = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), context.getClickedFace());
            if (capability != null) {
                for (int i = 0; i < capability.getTanks(); i++) {
                    if (capability.getFluidInTank(i).isEmpty()) continue;
                    FluidStack fluidStack = capability.getFluidInTank(i);
                    int toDrain = Math.min(CAPACITY - contained, fluidStack.getAmount());
                    FluidStack stackToDrain = fluidStack.copyWithAmount(toDrain);
                    if (validator.test(stackToDrain)) {
                        FluidStack actuallyDrained = capability.drain(stackToDrain, IFluidHandler.FluidAction.EXECUTE);
                        stack.set(TFMGDataComponents.AMOUNT, contained + actuallyDrained.getAmount());
                        player.getCooldowns().addCooldown(stack.getItem(), 20);
                        foundFluid = true;
                        break;
                    }
                }
            }
        }

        return foundFluid ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}
