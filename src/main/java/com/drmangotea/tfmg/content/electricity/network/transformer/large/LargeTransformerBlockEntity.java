package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class LargeTransformerBlockEntity extends KineticElectricBlockEntity {

    public boolean updateInFront = false;
    public float turnRatio = 1f;
    final boolean isMainPart;
    public TransformerConstructionState constructionState = TransformerConstructionState.NEEDS_STEEL;

    public LargeTransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        isMainPart = state.getValue(IS_MAIN_PART);
    }

    @Override
    public int getPowerUsage() {
        if (super.getPowerUsage() == 0) return 0;
        if (isMainPart && level.getBlockEntity(
                getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING)))
                instanceof LargeTransformerBlockEntity be)
            return (int) (Math.pow(be.data.getVoltage(), 2) / resistance());
        return super.getPowerUsage();
    }

    // Returns InteractionResult (1.20.1) instead of ItemInteractionResult (1.21.1)
    public InteractionResult addComponent(ItemStack stack, Player player, InteractionHand hand) {
        if (constructionState == TransformerConstructionState.NEEDS_STEEL
                && stack.is(CommonMetal.STEEL.storageBlocks.items())) {
            if (!player.isCreative())
                player.getItemInHand(hand).shrink(1);
            TFMGUtils.playSound(level, getBlockPos(), SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS);
            constructionState = TransformerConstructionState.NEEDS_OIL;

            Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
            if (level.getBlockEntity(getBlockPos().relative(facing))
                    instanceof LargeTransformerBlockEntity be) {
                be.turnRatio = turnRatio;
                be.constructionState = constructionState;
            }
            level.setBlock(getBlockPos().relative(facing),
                    getBlockState()
                            .setValue(LargeTransformerBlock.UNFINISHED_MODEL, false)
                            .setValue(LargeTransformerBlock.IS_MAIN_PART, false), 3);
            level.setBlock(getBlockPos(),
                    getBlockState().setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), 3);
            onPlaced();
            return InteractionResult.SUCCESS;
        }

        // Check for lubrication oil bucket
        if (constructionState == TransformerConstructionState.NEEDS_OIL
                && !stack.isEmpty()
                && stack.is(TFMGFluids.LUBRICATION_OIL.getBucket().get())) {
            if (!player.isCreative())
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
            TFMGUtils.playSound(level, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
            constructionState = TransformerConstructionState.FINISHED;
            Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
            if (level.getBlockEntity(getBlockPos().relative(facing))
                    instanceof LargeTransformerBlockEntity be)
                be.constructionState = constructionState;
            onPlaced();
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public int voltageGeneration() {
        if (isMainPart) return 0;
        int voltageGeneration = 0;
        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(
                getBlockState().getValue(HORIZONTAL_FACING).getOpposite()))
                instanceof LargeTransformerBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0)
                    voltageGeneration = (int) Math.max(voltageGeneration,
                            be.data.getVoltage() * turnRatio);
        getData().getsOutsidePower = voltageGeneration != 0;
        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {
        if (isMainPart) return 0;
        Direction oppFacing = getBlockState().getValue(HORIZONTAL_FACING).getOpposite();
        if (level.getBlockEntity(getBlockPos().relative(oppFacing))
                instanceof LargeTransformerBlockEntity be && be.data.notEnoughPower)
            return 0;

        int powerGeneration = 0;
        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(oppFacing))
                instanceof LargeTransformerBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0) {
                    int maxPower = switch (constructionState) {
                        case FINISHED -> 100000;
                        case NEEDS_OIL -> 50000;
                        case NEEDS_STEEL -> 30000;
                    };
                    powerGeneration = Math.max(powerGeneration, maxPower);
                }
        getData().getsOutsidePower = powerGeneration != 0;
        return powerGeneration;
    }

    public int getMaxPowerOutput() {
        return switch (constructionState) {
            case FINISHED -> 100000;
            case NEEDS_OIL -> 50000;
            case NEEDS_STEEL -> 30000;
        };
    }

    public IElectric getControlledBlock() {
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof LargeTransformerBlockEntity be)
            return be;
        return null;
    }

    @Override
    public float resistance() {
        if (!isMainPart) return 0;
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be
                && be.getData().getId() != data.getId()) {
            int count = getBlocksConnectedToNetworkCount(getControlledBlock().getData().getId());
            if (count != 0) return Math.max(be.getNetworkResistance() * count, 0);
        }
        return 0;
    }

    @Override
    public int getMaxCurrent() { return 500; }

    @Override
    public int getMaxVoltage() { return 100000; }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        if (oldVoltage != getData().getVoltage() || oldPower != getPowerUsage()) {
            updateInFront = true;
            getOrCreateElectricNetwork().handleInsufficientPower();
        }
        sendStuff();
        setChanged();
    }

    public void updateInFront() {
        if (!level.isClientSide) {
            LevelChunk chunk = ((ServerLevel) level).getChunkAt(worldPosition);
            TFMGPackets.getChannel().send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new UpdateInFrontPacket(BlockPos.of(getPos())));
        }
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be
                && be.getData().getId() != data.getId())
            be.updateNextTick();
        sendStuff();
        setChanged();
    }

    @Override
    public void tick() {
        super.tick();
        if (updateInFront) {
            updateInFront();
            updateInFront = false;
        }
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        String stateLabel = switch (constructionState) {
            case FINISHED -> "Oil + Heat Sink Cooled";
            case NEEDS_OIL -> "Metal Heat Sink Cooled";
            case NEEDS_STEEL -> "Air Cooled";
        };
        CreateLang.text("   State: " + stateLabel)
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip);
        CreateLang.text("   Turn Ratio: " + turnRatio)
                .style(ChatFormatting.WHITE)
                .forGoggles(tooltip);
        return super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("Ratio", turnRatio);
        compound.putString("State", constructionState.name());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        turnRatio = compound.getFloat("Ratio");
        String stateName = compound.getString("State");
        if (!stateName.isEmpty())
            constructionState = TransformerConstructionState.valueOf(stateName);
    }

    public enum TransformerConstructionState {
        FINISHED,
        NEEDS_OIL,
        NEEDS_STEEL
    }
}
