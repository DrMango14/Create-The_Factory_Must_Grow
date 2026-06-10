package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class KineticElectricBlockEntity extends GeneratingKineticBlockEntity implements IElectric, IHaveGoggleInformation, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());
    int timer = 0;

    public KineticElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        data.connectNextTick = true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }

    @Override
    public void sendStuff() {
        sendData();
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void remove() {
        super.remove();
        onRemoved();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        lazyTickElectricity();
    }

    @Override
    public void tick() {
        super.tick();
        tickElectricity();
    }

    @Override
    public void setNetwork(long network) {
        this.data.electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevel()).remove(getPos());
    }

    @Override
    public int getNetworkResistance() {
        return data.networkResistance;
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
        if (voltageGeneration == 0) data.getsOutsidePower = false;
        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {
        int powerGeneration = 0;
        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {
                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be && be.canWork()) {
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                powerGeneration = Math.max(powerGeneration, be.getMaxPowerOutput());
                                if (powerGeneration > be.getNetworkPowerGeneration()) {
                                    powerGeneration = 0;
                                    be.data.updatePowerNextTick = true;
                                }
                            }
                }
            }
        }
        return powerGeneration;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (!clientPacket)
            data.connectNextTick = true;
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (this instanceof RegularEngineBlockEntity)
            notifyNetworkAboutSpeedChange();
        timer = 0;
    }

    public void notifyNetworkAboutSpeedChange() {
        updateNextTick();
    }
}
