package com.drmangotea.tfmg.content.decoration.concrete;

import com.drmangotea.tfmg.registry.TFMGFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface ConcreteloggedBlock {
    BooleanProperty CONCRETELOGGED = BooleanProperty.create("concretelogged");



    default FluidState fluidState(BlockState state) {
      //  return state.getValue(CONCRETELOGGED) ? TFMGFluids.LIQUID_CONCRETE.getSource().getSource(false) : Fluids.EMPTY.defaultFluidState();
        return state.getValue(CONCRETELOGGED) ? TFMGFluids.LIQUID_CONCRETE.get().getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    default void updateConcrete(LevelAccessor level, BlockState state, BlockPos pos) {
        if (state.getValue(CONCRETELOGGED))
            //level.scheduleTick(pos, TFMGFluids.LIQUID_CONCRETE.get(), TFMGFluids.LIQUID_CONCRETE.get().getTickDelay(level));
            level.scheduleTick(pos, TFMGFluids.LIQUID_CONCRETE.get(), TFMGFluids.LIQUID_CONCRETE.get().getTickDelay(level));
    }
    default ItemInteractionResult onClicked(Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);

        if(state.getValue(CONCRETELOGGED)){
            if(stack.is(Items.BUCKET)){
                level.setBlock(pos, state.setValue(CONCRETELOGGED, false),3);
                if(!player.isCreative())
                    player.setItemInHand(hand, TFMGFluids.LIQUID_CONCRETE.getBucket().get().getDefaultInstance());
                player.playSound(SoundEvents.BUCKET_FILL, 1F, 1.0F + player.getRandom().nextFloat() * 0.4F);
                return ItemInteractionResult.SUCCESS;
            }

        }else {
            if(stack.is(TFMGFluids.LIQUID_CONCRETE.getBucket().get())){
                level.setBlock(pos, state.setValue(CONCRETELOGGED, true),3);
                if(!player.isCreative())
                    player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                player.playSound(SoundEvents.BUCKET_EMPTY, 1F, 1.0F + player.getRandom().nextFloat() * 0.4F);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default void tickDrying(Level level,BlockState state,BlockState newStack, BlockPos pos, RandomSource random){
        if(!state.getValue(CONCRETELOGGED))
            return;

        int randomInt = random.nextInt(7) ;
        if(randomInt==2) {
            level.setBlock(pos, newStack, 3);
        }
    }

    default BlockState withConcrete(BlockState placementState, BlockPlaceContext ctx) {
        return withConcrete(ctx.getLevel(), placementState, ctx.getClickedPos());
    }

    static BlockState withConcrete(LevelAccessor level, BlockState placementState, BlockPos pos) {
        if (placementState == null)
            return null;
        FluidState ifluidstate = level.getFluidState(pos);
        if (placementState.isAir())
            return ifluidstate.getType() == TFMGFluids.LIQUID_CONCRETE.getSource() ? ifluidstate.createLegacyBlock() : placementState;
        return placementState.setValue(CONCRETELOGGED, ifluidstate.getType() == TFMGFluids.LIQUID_CONCRETE.getSource());
    }


}
