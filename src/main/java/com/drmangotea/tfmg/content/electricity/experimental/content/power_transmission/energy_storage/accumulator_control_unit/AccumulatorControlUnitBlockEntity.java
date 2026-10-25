package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_control_unit;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class AccumulatorControlUnitBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    AccumulatorControlUnitProperties p;

    public double inputVoltage = 0;
    public double inputCurrent = 0;
    public double inputResistance = 500;

    public double outputVoltage = 0;
    public double outputPower = 0;
    public double outputResistance = 1;

    AccumulatorRackBlockEntity rack = null;

    public AccumulatorControlUnitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new AccumulatorControlUnitProperties(pos.asLong(), state.getValue(TFMGHorizontalDirectionalBlock.FACING));
        setLazyTickRate(20);
    }

    @Override
    public void onUpdated() {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        Resistance resistor = network.getResistance(getPos(), 0);

        if (resistor != null) {
            inputResistance = resistor.resistance;
            inputVoltage = resistor.getVoltage(level);
            inputCurrent = (float) (inputVoltage / inputResistance);
        }

        Resistance outputResistor = network.getResistance(getPos(), 1);
        IdealVoltageSource source = network.getVoltageSource(getPos(), 0);

        if (outputResistor != null && source != null) {
            outputResistance = outputResistor.resistance;
            outputVoltage = source.amplitude;
            double current = outputResistor.getVoltage(level) / outputResistance;
            outputPower = outputVoltage * current;

        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        findAccumulatorRack();
    }


    public void findAccumulatorRack() {
        for (Direction facing : Direction.values()) {
            if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof AccumulatorRackBlockEntity be) {
                if (level.getBlockEntity(be.controller) instanceof AccumulatorRackBlockEntity controller) {
                    this.rack = controller;
                    sendData();
                    return;
                }


            }
        }
    }

    @Override
    public void onPlace() {
        IRealisticElectric.super.onPlace();
        findAccumulatorRack();
    }

    @Override
    public void lazyTick() {


        if (rack != null) {
            double energyObtained = inputCurrent * inputVoltage;
            rack.energy.receiveEnergy(Math.abs(energyObtained) < 1 ? 0 : (int) Math.abs(energyObtained), false);

            rack.energy.extractEnergy(Math.abs(outputPower) < 1 ? 0 : (int) Math.abs(outputPower), false);
        }


    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Resistance: " + TFMGUtils.formatUnits(inputResistance, "Ohm")).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + TFMGUtils.formatUnits(inputVoltage, "V")).forGoggles(tooltip);
        TFMGLang.text("Power: " + TFMGUtils.formatUnits(inputCurrent * inputVoltage, "W")).forGoggles(tooltip);
        if (rack != null)
            TFMGLang.text("Energy: " + TFMGUtils.formatUnits(rack.energy.getEnergyStored(), "FE")).forGoggles(tooltip);
        return true;
    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
