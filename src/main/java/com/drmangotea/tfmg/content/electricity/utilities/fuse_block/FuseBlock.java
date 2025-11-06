package com.drmangotea.tfmg.content.electricity.utilities.fuse_block;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.IVoltageChanger;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FuseBlock extends TFMGHorizontalDirectionalBlock implements IBE<FuseBlockEntity>, IVoltageChanger {

    public FuseBlock(BlockBehaviour.Properties p_54120_) {
        super(p_54120_);
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        //ItemStack inHand = player.getItemInHand(hand);
//
        //if(level.getBlockEntity(pos) instanceof FuseBlockEntity be){
//
        //    if(inHand.is(TFMGItems.FUSE.get())){
//
        //        player.setItemInHand(hand, be.fuse);
        //        be.fuse = inHand;
        //        be.updateInFrontNextTick();
        //        be.updateNextTick();
        //        be.updateNetwork();
        //        be.updateInFront();
        //        be.sendStuff();
        //        return InteractionResult.SUCCESS;
//
        //    }else
        //        if(inHand.isEmpty()){
        //            player.setItemInHand(hand, be.fuse);
        //            be.fuse = ItemStack.EMPTY;
        //            be.updateNextTick();
        //            be.updateNetwork();
        //            be.updateNetwork();
        //            be.updateInFront();
        //            be.sendStuff();
        //            return InteractionResult.SUCCESS;
        //        }
//
        //}
//
        return super.use(blockState, level, pos, player, hand, blockHitResult);
    }


    @Override
    public Class<FuseBlockEntity> getBlockEntityClass() {
        return FuseBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FuseBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.FUSE_BLOCK.get();
    }
}
