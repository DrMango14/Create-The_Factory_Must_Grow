package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.electric_switch;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.ResistiveLoadBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.ResistorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.SwitchProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ElectricSwitchBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    int signal;
    boolean signalChanged;

    SwitchProperties p;

    public double resistance = 0;
    public double voltage = 0;
    public double current = 0;

    public ElectricSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new SwitchProperties(getPos(), state.getValue(TFMGDirectionalBlock.FACING),1E12);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();


    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Resistance: " + TFMGUtils.formatUnits(resistance,"Ohm")).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + TFMGUtils.formatUnits(voltage,"V")).forGoggles(tooltip);
        TFMGLang.text("Power: " + TFMGUtils.formatUnits(current*voltage,"W")).forGoggles(tooltip);

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

        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }
    }

    protected void analogSignalChanged(int newSignal) {
        signal = newSignal;

        double resistance = 0;

        if(newSignal!=0){
            resistance =1f / (newSignal * 1000);
        } else resistance = 1E12;

        if (newSignal == 15) {
            resistance = 0.001;
        }

        getNetwork().setResistance(this, 0, resistance);
        getNetwork().setResistance(this, 1, resistance);
        getNetwork().setResistance(this, 2, resistance);


    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;
        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            signalChanged = true;
    }


    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }
}
