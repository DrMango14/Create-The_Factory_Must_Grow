package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
       if(hand == InteractionHand.OFF_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(level.getBlockEntity(pos) instanceof ElectrodeHolderBlockEntity be){
            ElectrodeHolderBlockEntity.ElectrodeType electrodeType = be.electrodeType;
            ItemStack stackInside = electrodeType.item;
            if(stack.is(stackInside.getItem()))
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if(be.setElectrode(stack, true)) {
                player.setItemInHand(hand, electrodeType.item);
                be.setElectrode(stack, false);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
