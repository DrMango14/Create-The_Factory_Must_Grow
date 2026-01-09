package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class LargeCoilBlockEntity extends SmartBlockEntity {

    int turns = 0;

    public LargeCoilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemInteractionResult createTransformer(Player player, Direction lookingDirection) {
        BlockPos otherCoilPos = null;
        Direction otherCoilDirection;
        List<Direction> coilDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isVertical()) {
                BlockPos coilPos = getBlockPos().relative(direction);
                if (level.getBlockState(coilPos).is(TFMGBlocks.LARGE_COIL))
                    coilDirections.add(direction);

            }
        }
        if (coilDirections.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;


        if (coilDirections.size() == 1) {
            otherCoilDirection = coilDirections.get(0);
        } else {
            if (coilDirections.contains(lookingDirection)) {
                otherCoilDirection = lookingDirection;
            } else otherCoilDirection = coilDirections.get(0);
        }
        otherCoilPos = getBlockPos().relative(otherCoilDirection);
        if (level.getBlockEntity(otherCoilPos) instanceof LargeCoilBlockEntity be) {
            float primaryTurns = turns;
            float secondaryTurns = be.turns;

            float turnRatio = secondaryTurns/primaryTurns;

            if (!level.isClientSide) {
                level.setBlock(otherCoilPos, TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, otherCoilDirection).setValue(LargeTransformerBlock.IS_MAIN_PART, false), 3);

            }
            if(level.getBlockEntity(otherCoilPos) instanceof LargeTransformerBlockEntity transformerBe)
                transformerBe.turnRatio = turnRatio;
            if (!level.isClientSide) {

                level.setBlock(getBlockPos(), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, otherCoilDirection), 3);
            }



            TFMGUtils.playSound(level, getBlockPos(), SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void setCapacity(ItemStack stack) {
        if (stack.get(TFMGDataComponents.COIL_TURNS) != null)
            turns = stack.getOrDefault(TFMGDataComponents.COIL_TURNS, 0);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        turns = tag.getInt("Turns");
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("Turns", turns);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
