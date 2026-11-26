package com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_control_unit;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricalNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackBlockEntity;
import com.drmangotea.tfmg.content.electricity.packets.UpdateAccumulatorPacket;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AccumulatorControlUnitBlockEntity extends SmartBlockEntity implements IElectric {

    AccumulatorControlUnitProperties p;

    public double inputVoltage = 0;
    public double inputCurrent = 0;
    public double inputResistance = 500;

    public double outputVoltage = 0;
    public double outputPower = 0;
    public double outputResistance = 1;

    AccumulatorRackBlockEntity rack = null;

    public AccumulatorControlUnitScrollBehaviour outputVoltageScroll;

    public AccumulatorControlUnitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new AccumulatorControlUnitProperties(pos, state.getValue(TFMGHorizontalDirectionalBlock.FACING));
        setLazyTickRate(20);
    }

    @Override
    public void onUpdated() {
        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);

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
        AccumulatorControlUnitBlockEntity.updateVoltage(this, outputVoltageScroll.value);
        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(this.getWorld());

        network.update();
        //
    }


    public void findAccumulatorRack() {
		if (level == null) return;
        for (Direction facing : Direction.values()) {
            if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof AccumulatorRackBlockEntity be &&
				level.getBlockEntity(be.controller) instanceof AccumulatorRackBlockEntity controller
			) {
				this.rack = controller;
				sendData();
				return;
            }
        }
    }

    @Override
    public void onPlace() {
        IElectric.super.onPlace();
        findAccumulatorRack();
    }

    @Override
    public void lazyTick() {


        if (rack != null) {
            double energyObtained = inputCurrent * inputVoltage;
            rack.energy.receiveEnergy(Math.abs(energyObtained) < 1 ? 0 : (int) Math.abs(energyObtained), false);

            rack.energy.extractEnergy(Math.abs(outputPower) < 1 ? 0 : (int) Math.abs(outputPower), false);


        } else if (outputVoltage > 0) {
            getNetwork().setVoltageGen(this, 0);
        }


    }

    @Override
    public void tick() {
        super.tick();
        if (rack != null) {
            if (outputVoltage > 0 && rack.energy.getEnergyStored() == 0) {
                outputVoltage = 0;
                getNetwork().setVoltageGen(this, 0);
            }

            if (outputVoltage == 0 && rack.energy.getEnergyStored() > 0) {
                updateVoltage(this, outputVoltageScroll.getValue());
            }
        }

    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Resistance: " + TFMGUtils.formatUnits(inputResistance, "Ohm")).forGoggles(tooltip);
        TFMGLang.text("output Voltage: " + TFMGUtils.formatUnits(outputVoltage, "V")).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + TFMGUtils.formatUnits(inputVoltage, "V")).forGoggles(tooltip);
        TFMGLang.text("Power: " + TFMGUtils.formatUnits(inputCurrent * inputVoltage, "W")).forGoggles(tooltip);
        if (rack != null)
            TFMGLang.text("Energy: " + TFMGUtils.formatUnits(rack.energy.getEnergyStored(), "FE")).forGoggles(tooltip);
        return true;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);


        // tag.putInt("Value",outputVoltageScroll.value);

    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        // outputVoltageScroll.setValue(tag.getInt("Value"));
//
        // AccumulatorControlUnitBlockEntity.updateVoltage(this,outputVoltageScroll.value);
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

    public static void updateVoltage(AccumulatorControlUnitBlockEntity be, int value) {
        int voltage = value;
        int phase = 0;


        if (voltage > 120 * 121) {
            voltage /= 122 * 122;
            phase = 2;
        } else if (voltage > 120) {
            voltage /= 121;
            phase = 1;
        }
        Level level = be.level;

        if (be.rack == null) {
            voltage = 0;
        } else if (level.getBlockEntity(be.rack.controller) instanceof AccumulatorRackBlockEntity rack) {
            if (rack.energy.getEnergyStored() == 0) {
                voltage = 0;
            }
        }

        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);
        TFMG.LOGGER.debug("Meow");
        network.setVoltageGen(be, voltage);
        network.setPhaseShift(be, phase * 120);
        if (level instanceof ServerLevel serverLevel) {

            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(be.worldPosition), new UpdateAccumulatorPacket(be.getBlockPos(), value));

        }
        network.update();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        int max = 120 * 122 * 122;
        outputVoltageScroll = new AccumulatorControlUnitScrollBehaviour(TFMGLang.translateDirect("creative_generator.voltage_generation"),
                this, new AccumulatorControlUnitValueBox());


        outputVoltageScroll.between(0, max);
        outputVoltageScroll.value = 50;
        outputVoltageScroll.withCallback(i -> updateVoltage(this, i));
        behaviours.add(outputVoltageScroll);
    }

    static class AccumulatorControlUnitValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }


        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == Direction.UP;
        }

    }
}
