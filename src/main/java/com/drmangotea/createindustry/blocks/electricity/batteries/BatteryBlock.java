package com.drmangotea.createindustry.blocks.electricity.batteries;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BatteryBlock extends Block implements IBE<BatteryBlockEntity>, IWrenchable {
    public BatteryBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        withBlockEntityDo(level, pos, be -> be.setCapacity(stack));
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {

        int capacity = 0;

        if(be !=null)
            if(be instanceof BatteryBlockEntity be1)
                capacity = be1.capacity;


        stack.getOrCreateTag().putInt("Capacity",capacity);

        super.playerDestroy(level,player,pos,state,be,stack);
    }


    @Override
    public Class<BatteryBlockEntity> getBlockEntityClass() {
        return BatteryBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BatteryBlockEntity> getBlockEntityType() {
        //return TFMGBlockEntities.ACCUMULATOR.get();
        return null;
    }
}
