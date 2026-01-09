package com.drmangotea.tfmg.content.electricity.network.transformer.small;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.IVoltageChanger;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TransformerBlock extends TFMGHorizontalDirectionalBlock implements IBE<TransformerBlockEntity>, IVoltageChanger {
    public TransformerBlock(Properties p_54120_) {
        super(p_54120_);
    }


    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return TFMGShapes.TRANSFORMER.get(p_60555_.getValue(FACING));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(hand == InteractionHand.OFF_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack inHand = player.getItemInHand(hand);
        if(level.getBlockEntity(pos) instanceof TransformerBlockEntity be){
        Direction facing = state.getValue(FACING);
        Direction lookingDirection = TransformerBlockEntity.getCoilDirections(level,pos,hitResult).get(0);
        boolean primary =lookingDirection == facing.getClockWise();
        ItemStack coil = primary  ? be.primaryCoil : be.secondaryCoil;


            if(inHand.is(TFMGItems.ELECTROMAGNETIC_COIL.get())){
                if(coil.isEmpty()) {
                    if(primary){
                        be.primaryCoil = inHand;
                    }else be.secondaryCoil = inHand;
                    player.setItemInHand(hand,ItemStack.EMPTY);
                    withBlockEntityDo(level, pos, TransformerBlockEntity::updateCoils);
                    return ItemInteractionResult.SUCCESS;
                }


            }else if(inHand.isEmpty()){
                if(!coil.isEmpty()) {
                    player.setItemInHand(hand,coil);
                    if(primary){
                        be.primaryCoil = ItemStack.EMPTY;
                    }else be.secondaryCoil = ItemStack.EMPTY;
                    withBlockEntityDo(level, pos, TransformerBlockEntity::updateCoils);

                    return ItemInteractionResult.SUCCESS;
                }

            }
        }

        return super.useItemOn(stack,state, level, pos, player, hand,hitResult);
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
    public Class<TransformerBlockEntity> getBlockEntityClass() {
        return TransformerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TransformerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TRANSFORMER.get();
    }
}
