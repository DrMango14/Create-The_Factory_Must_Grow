package com.drmangotea.createindustry.blocks.electricity.batteries;


import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GalvanicCellBlock extends TFMGHorizontalDirectionalBlock implements IBE<GalvanicCellBlockEntity> {


    public GalvanicCellBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.GALVANIC_CELL.get(pState.getValue(FACING));
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
    public Class<GalvanicCellBlockEntity> getBlockEntityClass() {
        return GalvanicCellBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GalvanicCellBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.GALVANIC_CELL.get();
    }
}
