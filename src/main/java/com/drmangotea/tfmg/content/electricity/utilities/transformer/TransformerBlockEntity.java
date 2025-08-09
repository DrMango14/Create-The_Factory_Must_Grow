package com.drmangotea.tfmg.content.electricity.utilities.transformer;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.base.VoltageAlteringBlockEntity;

import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class TransformerBlockEntity extends VoltageAlteringBlockEntity {
    boolean updateInFront = false;

    public ItemStack primaryCoil = ItemStack.EMPTY;
    public ItemStack secondaryCoil = ItemStack.EMPTY;

    public float coilRatio = 0;

    public TransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getOutputVoltage() {
        return (int) (getData().getVoltage()*coilRatio);
    }

    @Override
    public int getOutputPower() {
        return coilRatio == 0 ? 0 : getPowerUsage();
    }

    @Override
    public IElectric getControlledBlock() {
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            return be;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if(updateInFront) {
            updateInFront();
            updateInFront = false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        BlockPos pos = this.getBlockPos();
        if(!primaryCoil.isEmpty()){
            ItemEntity item = new ItemEntity(level, pos.getX()+.5f,pos.getY()+.5f,pos.getZ()+.5f,primaryCoil);
            level.addFreshEntity(item);
        }
        if(!secondaryCoil.isEmpty()){
            ItemEntity item = new ItemEntity(level, pos.getX()+.5f,pos.getY()+.5f,pos.getZ()+.5f,secondaryCoil);
            level.addFreshEntity(item);
        }
    }

    @Override
    public int getPowerUsage() {
        Direction facing = getDirection();

        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {

                    return Math.max(be.getNetworkPowerUsage(this), 0);


            }
        }

        return 0;

    }



    public void updateCoils(){
        if(primaryCoil.get(TFMGDataComponents.COIL_TURNS)==null||secondaryCoil.get(TFMGDataComponents.COIL_TURNS)==null)
            return;
        int primaryTurns = primaryCoil.get(TFMGDataComponents.COIL_TURNS);
        int secondaryTurns = secondaryCoil.get(TFMGDataComponents.COIL_TURNS);

        if(primaryCoil.isEmpty()||secondaryCoil.isEmpty()||primaryTurns<50||secondaryTurns<50){
            coilRatio = 0;
            updateNextTick();
            updateInFront();
            return;
        }

        coilRatio = (float) (float)secondaryTurns/(float) primaryTurns;

        updateNextTick();
        updateInFront();

    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);

        if(coilRatio!=0) {
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
            TFMGTexts.Multimeter.transformerRatio(coilRatio).forGoggles(tooltip, 1);
        }
        return true;
    }

    @Override
    public float resistance() {
        Direction facing = getBlockState().getValue(FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite()))
                return Math.max(be.getNetworkResistance(), 0);
        }
        return 0;
    }
    public Direction getDirection(){
        if(!getBlockState().hasProperty(DirectionalBlock.FACING)){
            return getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getCounterClockWise();
        }

        return getBlockState().getValue(DirectionalBlock.FACING);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getClockWise();
    }
    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        if (oldVoltage != getData().getVoltage() || oldPower != getPowerUsage()) {
            updateInFront = true;
        }
        sendStuff();
        setChanged();
    }

    @Override
    public void updateNetwork() {
        super.updateNetwork();
        updateInFront();
    }

    @Override
    public void remove() {

        super.remove();
        updateInFront();
    }

    @Override
    public void onPlaced() {

        super.onPlaced();
        updateInFront = true;
    }

    public void updateInFront() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {
                be.updateNextTick();

            }
        }
        sendStuff();
        setChanged();
    }
    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        if(!primaryCoil.isEmpty())
            compound.put("PrimaryCoil", primaryCoil.saveOptional(registries));
        if(!secondaryCoil.isEmpty())
            compound.put("SecondaryCoil", secondaryCoil.save(registries));

        compound.putFloat("CoilRation", coilRatio);

    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        if (compound.contains("PrimaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("PrimaryCoil")).ifPresent(i -> primaryCoil = i);
        }
        if (compound.contains("SecondaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("SecondaryCoil")).ifPresent(i -> secondaryCoil = i);
        }
;

        coilRatio = compound.getFloat("CoilRation");
    }
}
