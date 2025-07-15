package com.drmangotea.tfmg.content.electricity.storage;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

public class AccumulatorBlock extends TFMGDirectionalBlock implements IBE<AccumulatorBlockEntity> {




    public AccumulatorBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        withBlockEntityDo(level, pos, be -> be.setCapacity(stack));
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return Collections.emptyList();
    }



    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!player.isCreative()&&level.getBlockEntity(pos) instanceof AccumulatorBlockEntity be) {
            ItemStack item = TFMGBlocks.ACCUMULATOR.asItem().getDefaultInstance();
            item.set(TFMGDataComponents.ACCUMULATOR_STORAGE, be.energy.getEnergyStored());
            ItemEntity itemToSpawn = new ItemEntity((Level) level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, item);
            if (itemToSpawn.getItem().getCount() > 0)
                level.addFreshEntity(itemToSpawn);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {


        withBlockEntityDo(level,pos,AccumulatorBlockEntity::refreshMultiblock);

        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState1, boolean a) {


        withBlockEntityDo(level,pos, IElectric::onPlaced);
        withBlockEntityDo(level,pos,b->b.refreshNextTick =true);

        super.onPlace(state, level, pos, blockState1, a);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        for(Direction direction : Direction.values()){
            BlockPos neighborPos = pos.relative(direction);
            if(level.getBlockState(pos).is(TFMGBlocks.ACCUMULATOR.get()))
                withBlockEntityDo(level,neighborPos,AccumulatorBlockEntity::refreshMultiblock);
//
        }
        IBE.onRemove(state, level, pos, newState);
    }



    @Override
    public Class<AccumulatorBlockEntity> getBlockEntityClass() {
        return AccumulatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AccumulatorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ACCUMULATOR.get();
    }
}

