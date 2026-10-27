package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.large_switch;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.CableProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.electric_switch.SwitchProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.AbstractCableBlockEntity.connectToNeighbors;
import static com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class LargeSwitchBlockEntity extends KineticBlockEntity implements IRealisticElectric {


    public boolean closed = false;

    public LerpedFloat visualAngle = LerpedFloat.angular();
    public float angle = 900;

    final boolean isMainPart;

    boolean update = true;

    public double resistance = 0;
    public double voltage = 0;
    public double current = 0;

    public boolean connectNextTick = false;
    ElectricalProperties p;

    public LargeSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        isMainPart = state.getValue(IS_MAIN_PART);
        visualAngle.setValue(90);
        List<Direction> directions = new ArrayList<>();
        Direction direction = state.getValue(HORIZONTAL_FACING);
        directions.add(direction);
        directions.add(direction.getOpposite());
        p = state.getValue(IS_MAIN_PART) ? new LargeSwitchProperties(getPos(), state.getValue(HORIZONTAL_FACING), 1E12) : new CableProperties(getPos(), Arrays.stream(Direction.values()).toList());
        ;
     //  this.p = new CableProperties(getPos(), Arrays.stream(Direction.values()).toList());



        int i = 0;
    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        angle = compound.getFloat("Angle");
        closed = compound.getBoolean("Closed");
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Angle", angle);
        compound.putBoolean("Closed", closed);
    }

    @Override
    public void onUpdated() {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        Resistance resistor = network.getResistance(getPos(), 0);

        if (resistor != null) {

            resistance = resistor.resistance;
            voltage = resistor.getVoltage(level);
            current = (float) (voltage / resistance);

        }
    }


    @Override
    public void tick() {
        super.tick();

        if(update&&!isMainPart&&level.isClientSide){
            onPlace();
            update = false;
        }
        if(update){
            for(Direction facing : Arrays.stream(Direction.values()).toList()){
                if(level.getBlockEntity(getBlockPos().relative(facing)) instanceof IRealisticElectric n){
                    connectToNeighbors(level,n);
                }
            }
        }
        if (connectNextTick) {

            connectNextTick = connectToNeighbors(level, this);
        }

        if (level.isClientSide) {
            visualAngle.chase(angle / 10d, 1d, LerpedFloat.Chaser.EXP);
            visualAngle.tickChaser();
        }

        if (!isMainPart)
            return;
        if (angle < 0)
            angle = 0;
        if (angle > 900)
            angle = 900;
        if (getSpeed() == 0)
            return;

        if (getSpeed() < 0 && angle != 900) {
            angle += getArmSpeed();
        }
        if (getSpeed() > 0 && angle != 0) {
            angle -= getArmSpeed();
        }

        boolean oldValue = closed;


        closed = angle == 0;
        if (oldValue != closed)
            stateChanged();

    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Resistance: " + TFMGUtils.formatUnits(resistance, "Ohm")).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + TFMGUtils.formatUnits(voltage, "V")).forGoggles(tooltip);
        TFMGLang.text("Power: " + TFMGUtils.formatUnits(current * voltage, "W")).forGoggles(tooltip);

        return true;
    }



    protected void stateChanged() {

        double resistance = 0;

        if (closed) {
            resistance = 0.001;
        } else resistance = 1E12;

        //if(newSignal!=0){
        //    resistance =1f / (newSignal * 1000);
        //} else resistance = 1E12;

        //if (newSignal == 15) {
        //    resistance = 0.001;
        //}

        getNetwork().setResistance(this, 0, resistance);



    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }
    @Override
    public void onPlace() {
        IRealisticElectric.super.onPlace();
        connectNextTick = connectToNeighbors(level, this);
        updateNetwork(getBlockPos());
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

    public float getArmSpeed() {
        return Math.abs(getSpeed()) * 0.3f;
    }


}
