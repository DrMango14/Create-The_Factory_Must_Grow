package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.concurrent.atomic.AtomicBoolean;

public class ElectrodeHolderBlock extends Block implements IBE<ElectrodeHolderBlockEntity> {
    public ElectrodeHolderBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) {
            AtomicBoolean success = new AtomicBoolean(false);
            withBlockEntityDo(level, pos, (electrodeHolder) -> {
                if (!electrodeHolder.inventory.isEmpty()) {
                    TFMGUtils.returnItemToInventory(electrodeHolder.inventory, 0, player, hand);
                    electrodeHolder.onInventoryChanged(0);
                    success.set(true);
                }
            });
            if (success.get()) {
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        AtomicBoolean success = new AtomicBoolean(false);
        withBlockEntityDo(level, pos, (electrodeHolder) -> {
            var electrode = stack.getOrDefault(TFMGDataComponents.ELECTRODE, Electrode.Stored.NONE).electrode().value();
            if (electrode.isValid()) {
                boolean doInsert = true;
                if (!electrodeHolder.inventory.isEmpty()) {
                    if (ItemStack.isSameItemSameComponents(stack, electrodeHolder.inventory.getStackInSlot(0))) {
                        doInsert = false;
                    } else {
                        TFMGUtils.returnItemToInventory(electrodeHolder.inventory, 0, player, hand);
                        electrodeHolder.onInventoryChanged(0);
                    }
                }
                if (doInsert) {
                    electrodeHolder.inventory.insertItem(0, stack.copyWithCount(1), false);
                    electrodeHolder.onInventoryChanged(0);
                    stack.shrink(1);
                    success.set(true);
                }
            }
        });
        if (success.get()) {
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof IElectric be){
            be.onPlace();
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new AddElectricalComponentPacket(BlockPos.of(pos.asLong())));


        }
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
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
