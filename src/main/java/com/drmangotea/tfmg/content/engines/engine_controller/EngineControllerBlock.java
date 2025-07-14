package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EngineControllerBlock extends TFMGHorizontalDirectionalBlock implements IBE<EngineControllerBlockEntity>, IWrenchable {
    public EngineControllerBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.isShiftKeyDown() && EngineControllerBlockEntity.playerInRange(player, level, pos)&&player.getItemInHand(hand).getItem()!= AllItems.WRENCH.get()&&player.getItemInHand(hand).getItem()!= TFMGItems.TRANSMISSION.get()) {



            if (!level.isClientSide) {
                withBlockEntityDo(level, pos, be -> {
                    if(be.isUsedBy(player)){
                        be.tryStopUsing(player);
                    }else
                    be.tryStartUsing(player);

                });
            }
            return ItemInteractionResult.SUCCESS;
        }


        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return TFMGShapes.ENGINE_CONTROLLER.get(p_60555_.getValue(FACING).getOpposite());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), be -> be.use(context.getPlayer()));
    }

    @Override
    public Class<EngineControllerBlockEntity> getBlockEntityClass() {
        return EngineControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EngineControllerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ENGINE_CONTROLLER.get();
    }
}
