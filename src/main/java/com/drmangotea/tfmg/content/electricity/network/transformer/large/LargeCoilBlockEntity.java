package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class LargeCoilBlockEntity extends SmartBlockEntity {

    public int turns = 0;

    public LargeCoilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public InteractionResult createTransformer(Player player, Direction lookingDirection) {
        // Find adjacent horizontal coils
        List<Direction> coilDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isVertical()) {
                if (level.getBlockState(getBlockPos().relative(direction))
                        .is(TFMGBlocks.LARGE_COIL.get()))
                    coilDirections.add(direction);
            }
        }
        if (coilDirections.isEmpty())
            return InteractionResult.PASS;

        // Pick which coil to pair with — prefer the one the player is looking toward
        Direction otherCoilDirection;
        if (coilDirections.size() == 1) {
            otherCoilDirection = coilDirections.get(0);
        } else {
            otherCoilDirection = coilDirections.contains(lookingDirection)
                    ? lookingDirection
                    : coilDirections.get(0);
        }

        BlockPos otherCoilPos = getBlockPos().relative(otherCoilDirection);
        if (!(level.getBlockEntity(otherCoilPos) instanceof LargeCoilBlockEntity otherBe))
            return InteractionResult.PASS;

        float primaryTurns = turns;
        float secondaryTurns = otherBe.turns;
        float turnRatio = primaryTurns == 0 ? 1f : secondaryTurns / primaryTurns;

        if (!level.isClientSide) {
            // Place secondary half first (otherCoilPos → secondary block facing back toward this pos)
            level.setBlock(otherCoilPos,
                    TFMGBlocks.LARGE_TRANSFORMER.getDefaultState()
                            .setValue(HorizontalKineticBlock.HORIZONTAL_FACING, otherCoilDirection)
                            .setValue(LargeTransformerBlock.IS_MAIN_PART, false), 3);
        }
        if (level.getBlockEntity(otherCoilPos) instanceof LargeTransformerBlockEntity transformerBe)
            transformerBe.turnRatio = turnRatio;

        if (!level.isClientSide) {
            // Place main half (this pos → main block facing toward otherCoilPos)
            level.setBlock(getBlockPos(),
                    TFMGBlocks.LARGE_TRANSFORMER.getDefaultState()
                            .setValue(HorizontalKineticBlock.HORIZONTAL_FACING, otherCoilDirection), 3);
        }

        TFMGUtils.playSound(level, getBlockPos(), SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
    }

    /**
     * Called on placement — reads the turn count from the item's NBT tag (1.20.1 substitute
     * for the data component system used in 1.21.1).
     */
    public void setCapacity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Turns"))
            turns = tag.getInt("Turns");
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("Turns", turns);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        turns = tag.getInt("Turns");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
}
