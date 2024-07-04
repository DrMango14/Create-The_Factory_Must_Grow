package com.drmangotea.createindustry.items;

import com.drmangotea.createindustry.base.TFMGPipes;
import com.drmangotea.createindustry.blocks.pipes.normal.LockablePipeBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;


public class ScrewdriverItem extends Item {




    public ScrewdriverItem(Properties p_40566_) {
        super( p_40566_);

    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();

        BlockPos positionClicked = pContext.getClickedPos();

        Level level = pContext.getLevel();




        if(
                level.getBlockState(positionClicked).is(TFMGPipes.STEEL_PIPE.get())||
                        level.getBlockState(positionClicked).is(TFMGPipes.ALUMINUM_PIPE.get())||
                                level.getBlockState(positionClicked).is(TFMGPipes.PLASTIC_PIPE.get())
        ) {
            //level.playSound(player, positionClicked, SoundEvents.COPPER_STEP, SoundSource.BLOCKS, 0.3f,0.5f);
            ((LockablePipeBlockEntity)level.getBlockEntity(positionClicked)).toggleLock(player);
           // Direction side = Direction.UP;
           // Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
           // ((FluidPipeBlock)level.getBlockState(positionClicked).getBlock()).updateBlockState(TFMGBlocks.ALUMINUM_PIPE.getDefaultState()
           //         .setValue(facingToPropertyMap.get(side), true)
           //         .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, level, positionClicked);;
        }



        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                (playerr) -> playerr.broadcastBreakEvent(playerr.getUsedItemHand()));


        return InteractionResult.SUCCESS;

    }
}