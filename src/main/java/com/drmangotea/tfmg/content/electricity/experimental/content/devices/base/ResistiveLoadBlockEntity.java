package com.drmangotea.tfmg.content.electricity.experimental.content.devices.base;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ResistiveLoadBlockEntity extends SmartBlockEntity implements IRealisticElectric, IHaveGoggleInformation {

    ResistorProperties p;

    public double resistance = 0;
    public double voltage = 0;
    public double current = 0;


    public ResistiveLoadBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new ResistorProperties(getPos(), state.getValue(TFMGDirectionalBlock.FACING),resistance());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public double resistance(){
        return 1000;
    }


    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGLang.text("Resistance: " + TFMGUtils.formatUnits(resistance,"Ohm")).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + TFMGUtils.formatUnits(voltage,"V")).forGoggles(tooltip);
        TFMGLang.text("Power: " + TFMGUtils.formatUnits(current*voltage,"W")).forGoggles(tooltip);

        return true;
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
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return getLevel();
    }

}
