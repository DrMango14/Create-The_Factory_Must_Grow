package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity.MixerMode;

public class IndustrialMixerBlock extends KineticBlock implements IBE<IndustrialMixerBlockEntity> {



    public IndustrialMixerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public void onRemove(BlockState state, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        VatBlock.updateVatState(state, pLevel, pPos.relative(Direction.DOWN));
        super.onRemove(state, pLevel, pPos, pNewState, pIsMoving);

    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;

        if (!(level.getBlockEntity(pos) instanceof IndustrialMixerBlockEntity be))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);
        MixerMode current = be.mixerMode;
        Item installedItem = current.item;

        if (held.isEmpty() && installedItem != null) {
            if (!level.isClientSide) {
                ItemStack refund = new ItemStack(installedItem);
                if (!player.addItem(refund)) {
                    Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), refund);
                }
                be.setMixerMode("none", false);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.isEmpty())
            return InteractionResult.PASS;

        if (installedItem != null && held.getItem() == installedItem)
            return InteractionResult.PASS;

        if (!be.setMixerMode(held, true))
            return InteractionResult.PASS;

        if (!level.isClientSide) {
            if (installedItem != null) {
                ItemStack prev = new ItemStack(installedItem);
                if (!player.addItem(prev)) {
                    Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), prev);
                }
            }

            ItemStack toInstall = held.copy();
            toInstall.setCount(1);
            be.setMixerMode(toInstall, false);

            held.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }



    @Override
    public void onPlace(BlockState state, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        VatBlock.updateVatState(state, pLevel, pPos.relative(Direction.DOWN));
        super.onPlace(state, pLevel, pPos, pOldState, pIsMoving);

    }

    @Override
    public Class<IndustrialMixerBlockEntity> getBlockEntityClass() {
        return IndustrialMixerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IndustrialMixerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.INDUSTRIAL_MIXER.get();
    }
}
