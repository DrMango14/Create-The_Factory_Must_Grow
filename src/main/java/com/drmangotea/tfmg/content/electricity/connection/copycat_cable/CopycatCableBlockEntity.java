package com.drmangotea.tfmg.content.electricity.connection.copycat_cable;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.*;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.schematics.cannon.ConfigureSchematicannonPacket;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import java.util.List;

public class CopycatCableBlockEntity extends CopycatBlockEntity implements IElectric, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());

    int powerPercentage = 100;
    boolean setNextTick = true;
    public CopycatCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        data.connectNextTick = true;
        if (!canBeInGroups()) {
            data.group = new ElectricalGroup(-1);
        }
    }
    @Override
    public boolean hasCustomMaterial() {
        return !AllBlocks.COPYCAT_BASE.has(getMaterial());
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public boolean destroyed() {
        return data.destroyed;
    }

    @Override
    public ElectricalNetwork getOrCreateElectricNetwork() {
        if (level.getBlockEntity(BlockPos.of(data.electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) level.getBlockEntity(BlockPos.of(data.electricalNetworkId)));
        } else {
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(data.electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }


    @Override
    public float resistance() {
        return 0;
    }

    @Override
    public int voltageGeneration() {

        int voltageGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be)
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                                data.getsOutsidePower = true;
                            }
            }
        }

        if (voltageGeneration == 0)
            data.getsOutsidePower = false;

        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {

        int powerGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be) {
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                powerGeneration = Math.max(powerGeneration, be.getPowerUsage()) + 1;
                            }
                }
            }
        }

        return powerGeneration;
    }

    @Override
    public int frequencyGeneration() {
        return 0;
    }

    @Override
    public void updateNextTick() {
        data.updateNextTick = true;
    }

    @Override
    public void updateNetwork() {
        getOrCreateElectricNetwork().updateNetwork();
        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new NetworkUpdatePacket(BlockPos.of(getPos())));

        sendData();
    }

    @Override
    public void sendStuff() {
        sendData();
    }

    @Override
    public void setVoltage(int newVoltage) {
        if (canBeInGroups()) {
            data.voltage = (int) (((float) resistance() / data.group.resistance) * (float) data.voltageSupply);
            return;
        }
        data.voltage = newVoltage;
    }

    @Override
    public void setFrequency(int newFrequency) {
        data.frequency = newFrequency;
    }

    @Override
    public void setNetworkResistance(int newUsage) {
        data.networkResistance = newUsage;
    }

    @Override
    public int getNetworkResistance() {
        return data.networkResistance;
    }
    
    @Override
    public void setNetwork(long network) {
        this.data.electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(getPos());
    }

    public boolean networkUndersupplied() {
        return getNetworkPowerUsage() > data.networkPowerGeneration;
    }


    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void remove() {
        super.remove();
        this.data.destroyed = true;
        for (Direction d : Direction.values()) {
            if (hasElectricitySlot(d))
                if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(d)) instanceof IElectric be && be.hasElectricitySlot(d.getOpposite())) {
                    ElectricNetworkManager.networks.get(getLevel())
                            .remove(be.getPos());
                    be.setNetwork(be.getPos());
                    be.onPlaced();
                    be.updateNextTick();
                }
        }
        if (data.electricalNetworkId != getPos())
            getOrCreateElectricNetwork().getMembers().remove(this);

        if (data.electricalNetworkId == getPos())
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(getData().getId());
    }

    @Override
    public void tick() {
        super.tick();
        if (data.connectNextTick) {
            onPlaced();
            data.connectNextTick = false;
        }
        if (data.updateNextTick) {
            updateNetwork();
            data.updateNextTick = false;
        }
        if (data.setVoltageNextTick) {
            setVoltage(data.voltageSupply);
            data.setVoltageNextTick = false;
        }
        //if(setNextTick) {
        //    setMaterial(TFMGBlocks.COPYCAT_CABLE_BASE.getDefaultState());
        //    setNextTick = false;
        //}
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);

        compound.putInt("GroupId", data.group.id);
        compound.putFloat("GroupResistance", data.group.resistance);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        // Validate Material
        if (getMaterial() != null && !clientPacket) {
            BlockState blockState = getBlockState();
            if (blockState == null)
                return;
            if (!(blockState.getBlock() instanceof CopycatBlock cb))
                return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(level, worldPosition, ItemStack.EMPTY, null);
            if (acceptedBlockState != null && getMaterial().is(acceptedBlockState.getBlock()))
                return;
            setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        }

        //
        data.group = new ElectricalGroup(compound.getInt("GroupId"));
        data.group.resistance = compound.getFloat("GroupResistance");
        if (!clientPacket)
            data.connectNextTick = true;
    }
}
