package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
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

public class DebugResistorBlockEntity extends SmartBlockEntity implements IRealisticElectric, IHaveGoggleInformation {

    DebugResistorProperties p;

    public double resistance = 0;

    public DebugResistorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new DebugResistorProperties(getPos(), state.getValue(TFMGDirectionalBlock.FACING));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        DebugResistorProperties properties = (DebugResistorProperties) RealElectricNetworkManager.getNetwork(level).members.get(getPos());

        if(properties == null){
            return false;
        }

        Resistance resistor = ((Resistance) properties.components.get(0));

        TFMGLang.text("Resistance: " + resistor.resistance).forGoggles(tooltip);
        TFMGLang.text("Voltage: " + resistor.getVoltage(level)).forGoggles(tooltip);
        TFMGLang.text("Power: " + Math.pow(resistor.getVoltage(level),2)/resistor.resistance).forGoggles(tooltip);


        return true;
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
