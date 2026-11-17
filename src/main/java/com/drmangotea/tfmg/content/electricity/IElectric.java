package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import net.createmod.catnip.data.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.AbstractCableBlockEntity.connectToNeighbors;

public interface IElectric {


    default ElectricalProperties getProperties() {
        return new ElectricalProperties(this.getPos());
    }

    default ElectricalNetwork getNetwork(Level level) {
        return ElectricNetworkManager.getNetwork(level);
    }

    default ElectricalNetwork getNetwork() {
        return getNetwork(getWorld());
    }

    long getPos();


    default void updateNetwork(BlockPos pos) {
        if (this instanceof LargeTransformerBlockEntity t && !t.isMainPart)
            return;

        ElectricalNetwork network = ElectricNetworkManager.getNetwork(getWorld());
        if (network == null)
            return;
        network.addMember(pos, getProperties());
        network.update();
        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
    }


    default void removeBlock() {
        ElectricalNetwork network = ElectricNetworkManager.getNetwork(getWorld());
        network.members.remove(this.getPos());
        network.removeConnections(this);
        if (this.getProperties() != null)
            network.nodes.removeAll(this.getProperties().nodes);


        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
        network.update();
    }


    default List<Pair<Pair<Integer, Integer>, Pair<Float, Float>>> getVoltageGeneration() {
        List<Pair<Pair<Integer, Integer>, Pair<Float, Float>>> list = new ArrayList<>();

        ElectricalProperties properties = getNetwork().members.get(getPos());
        if (properties != null) {
            List<Integer> voltageList = new ArrayList<>();
            List<Integer> phaseOffsetList = new ArrayList<>();
            List<Float> maxPowerList = new ArrayList<>();
            List<Float> currentList = new ArrayList<>();

            for (int i = 0; i < properties.components.size(); i++) {
                ElectricalComponent component = properties.components.get(i);

                if (component instanceof IdealVoltageSource source) {
                    voltageList.add((int) source.amplitude);
                    phaseOffsetList.add((int) source.phaseOffset);
                    maxPowerList.add((float) source.power);
                }
                if (component instanceof Resistance resistor) {
                    currentList.add((float) (resistor.getVoltage(getWorld()) / resistor.resistance));
                }
            }
            for (int i = 0; i < voltageList.size(); i++) {
                int voltage = voltageList.get(i);
                int phaseOffset = phaseOffsetList.get(i);
                float maxPower = maxPowerList.get(i);
                float current = currentList.get(i);
                list.add(Pair.of(Pair.of(voltage, phaseOffset), Pair.of(maxPower, current)));
            }
        }
        return list;
    }

    default void onUpdated() {
    }

    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("multimeter").style(ChatFormatting.WHITE)
                .forGoggles(tooltip);


        // if (getVoltageGeneration().is) {
        //     TFMGTexts.Multimeter.powerGenerated(powerGeneration()).forGoggles(tooltip, 1);
        //     TFMGTexts.Multimeter.voltageGenerated(voltageGeneration()).forGoggles(tooltip, 1);
        //     TFMGTexts.Multimeter.separator().forGoggles(tooltip);
        // }
        // if (resistance() != 0 && !(this instanceof CableConnectorBlockEntity) && !(this instanceof CableHubBlockEntity))
        //     TFMGTexts.Multimeter.resistance(voltageGeneration() > 0 ? getGeneratorResistance() : resistance()).forGoggles(tooltip, 1);
        // TFMGTexts.Multimeter.voltage(getData().getVoltage()).forGoggles(tooltip, 1);
        // TFMGTexts.Multimeter.current(resistance() == 0 ? getData().highestCurrent : getCurrent()).forGoggles(tooltip, 1);
        // if (resistance() != 0)
        //     TFMGTexts.Multimeter.power(getPowerUsage()).forGoggles(tooltip, 1);
//
        // if (getData().energyGiven > 0) {
        //     TFMGTexts.Multimeter.sendingFE(getData().energyGiven).forGoggles(tooltip, 1);
        // }
        // if (getData().energyTakenPerTick > 0) {
        //     TFMGTexts.Multimeter.takingFE(getData().energyTakenPerTick).forGoggles(tooltip, 1);
        // }
//
        // if (isPlayerSneaking) {
        //     TFMGTexts.Multimeter.separator().forGoggles(tooltip);
        //     TFMGTexts.Multimeter.networkGeneration(getNetworkPowerGeneration()).forGoggles(tooltip, 1);
        //     TFMGTexts.Multimeter.networkConsumption(getNetworkPowerUsage()).forGoggles(tooltip, 1);
        // }


        return false;
    }

    default void onPlace() {
        this.updateNetwork(BlockPos.of(getPos()));
        //if (this instanceof ElectricSwitchBlockEntity)
        //    for (Direction facing : Direction.values()) {
        //        if (getWorld().getBlockEntity(BlockPos.of(getPos()).relative(facing)) instanceof IRealisticElectric be) {
        //            connectToNeighbors(getWorld(), be);
        //        }
//
        //    }


      //  if (!(this instanceof CableBlockEntity) && getProperties().cableConnectable()) {
      //      connectToNeighbors(getWorld(), this);
      //      updateNetwork(BlockPos.of(getPos()));
      //  }
        connectToNeighbors(getWorld(), this);
        this.updateNetwork(BlockPos.of(getPos()));

    }

    Level getWorld();

}
