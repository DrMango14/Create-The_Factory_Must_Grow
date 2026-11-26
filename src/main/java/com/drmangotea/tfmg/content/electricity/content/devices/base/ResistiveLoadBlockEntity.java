package com.drmangotea.tfmg.content.electricity.content.devices.base;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricalNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ResistiveLoadBlockEntity extends SmartBlockEntity implements IElectric, IHaveGoggleInformation {

    public ResistorProperties p;

    public double resistance = 0;
    public double voltage = 0;
    public double current = 0;


    public ResistiveLoadBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


        p = new ResistorProperties(getPos(), state.hasProperty(TFMGDirectionalBlock.FACING) ? state.getValue(TFMGDirectionalBlock.FACING) : Direction.UP,resistance(), NodeType.PHASE_1);

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public double resistance(){
        return 1000;
    }


    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGTexts.Multimeter.resistance(resistance).forGoggles(tooltip);
        TFMGTexts.Multimeter.voltage(voltage).forGoggles(tooltip);
        TFMGTexts.Multimeter.current(current).forGoggles(tooltip);
        TFMGTexts.Multimeter.power(current*voltage).forGoggles(tooltip);

        return true;
    }

    @Override
    public void onUpdated() {
        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);

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
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public Level getWorld() {
        return getLevel();
    }

}
