package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.lubricationOil;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class LargeTransformerBlockEntity extends KineticElectricBlockEntity {
    public boolean updateInFront = false;


    public float turnRatio = 1;


    final boolean isMainPart;

    public TransformerConstructionState constructionState = TransformerConstructionState.NEEDS_STEEL;

    public LargeTransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        isMainPart = state.getValue(IS_MAIN_PART);
    }

    @Override
    public int getPowerUsage() {

        if (super.getPowerUsage() == 0)
            return 0;

        if (isMainPart && level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING))) instanceof LargeTransformerBlockEntity be)
            return (int) ((int) Math.pow(be.data.getVoltage(), 2) / resistance());

        return super.getPowerUsage();
    }

    public ItemInteractionResult addComponent(ItemStack stack, Player player, InteractionHand hand) {


        if (constructionState == TransformerConstructionState.NEEDS_STEEL && stack.is(CommonMetal.STEEL.storageBlocks.items())) {
            if (!player.isCreative())
                player.getItemInHand(hand).shrink(1);
            TFMGUtils.playSound(level, getBlockPos(), SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS);
            constructionState = TransformerConstructionState.NEEDS_OIL;

            if (level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING))) instanceof LargeTransformerBlockEntity be) {
                turnRatio = be.turnRatio;
                be.constructionState = constructionState;
            }
            level.setBlock(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING)), getBlockState().setValue(LargeTransformerBlock.UNFINISHED_MODEL, false).setValue(LargeTransformerBlock.IS_MAIN_PART, false), 3);
            level.setBlock(getBlockPos(), getBlockState().setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), 3);
            onPlaced();
            return ItemInteractionResult.SUCCESS;
        }
        if (constructionState == TransformerConstructionState.NEEDS_OIL && stack.is(lubricationOil().getBucket())) {
            if (!player.isCreative())
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());

            TFMGUtils.playSound(level, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
            constructionState = TransformerConstructionState.FINISHED;
            if (level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING))) instanceof LargeTransformerBlockEntity be) {
                be.constructionState = constructionState;
            }
            onPlaced();
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    public int voltageGeneration() {

        if (isMainPart)
            return 0;

        int voltageGeneration = 0;


        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeTransformerBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0)
                    voltageGeneration = (int) Math.max(voltageGeneration, be.data.getVoltage() * turnRatio);
        getData().getsOutsidePower = true;


        if (voltageGeneration == 0)
            getData().getsOutsidePower = false;

        return voltageGeneration;
    }

    public IElectric getControlledBlock() {
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof LargeTransformerBlockEntity be) {
            return be;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Ratio", turnRatio);
        compound.putString("State", constructionState.toString());
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        turnRatio = compound.getFloat("Ratio");
        constructionState = Enum.valueOf(TransformerConstructionState.class, compound.getString("State"));
    }

    @Override
    public float resistance() {
        if (!isMainPart)
            return 0;

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            int count = getBlocksConnectedToNetworkCount(getControlledBlock().getData().getId());
            if (count != 0)
                return Math.max(be.getNetworkResistance() * count, 0);
        }
        return 0;
    }

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

    public int powerGeneration() {

        if (isMainPart)
            return 0;

        if (level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeTransformerBlockEntity be && be.data.notEnoughPower)
            return 0;

        int powerGeneration = 0;


        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeTransformerBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0) {
                    int maxPower = switch (constructionState) {
                        case FINISHED -> 100000;
                        case NEEDS_OIL -> 50000;
                        case NEEDS_STEEL -> 30000;
                    };
                    powerGeneration = Math.max(powerGeneration, maxPower);
                }
        getData().getsOutsidePower = true;


        if (powerGeneration == 0)
            getData().getsOutsidePower = false;

        return powerGeneration;
    }

    public void updateInFront() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            be.updateNextTick();

        }
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
    public int getMaxCurrent() {
        return 500;
    }

    @Override
    public int getMaxVoltage() {
        return 100000;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        String stateKey = switch (constructionState) {
            case FINISHED -> "multimeter.large_transformer.oil_cooled";
            case NEEDS_OIL -> "multimeter.large_transformer.metal_cooled";
            case NEEDS_STEEL -> "multimeter.large_transformer.air_cooled";
        };

        TFMGLang.translate(stateKey).color(0x69c9c5).forGoggles(tooltip);
        TFMGTexts.Multimeter.transformerRatio(turnRatio);

        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        return true;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {

        return direction == Direction.UP;

    }

    enum TransformerConstructionState {

        FINISHED,
        NEEDS_OIL,
        NEEDS_STEEL


    }
}
