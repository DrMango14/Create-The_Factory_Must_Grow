package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ElectrodeHolderBlock extends Block implements IBE<ElectrodeHolderBlockEntity> {
    public ElectrodeHolderBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if(level.getBlockEntity(pos) instanceof ElectrodeHolderBlockEntity be){
            ItemStack stack = player.getItemInHand(hand);
            Electrode electrode = be.electrode;
            ItemStack stackInside = electrode.getStack();
            if(stack.is(stackInside.getItem()))
                return InteractionResult.PASS;
            if(be.setElectrode(stack, true)) {
                player.setItemInHand(hand, electrode.getStack());
                be.setElectrode(stack, false);
                return InteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
                if (be.electrode == TFMGUtils.getElectrode(TFMG.asResource("none"))) return InteractionResult.PASS;
                player.setItemInHand(hand, electrode.getStack());
                be.setElectrode(TFMGUtils.getElectrode(TFMG.asResource("none")), false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level,pos, IElectric::onPlaced);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<ElectrodeHolderBlockEntity> getBlockEntityClass() {
        return ElectrodeHolderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectrodeHolderBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ELECTRODE_HOLDER.get();
    }
}
