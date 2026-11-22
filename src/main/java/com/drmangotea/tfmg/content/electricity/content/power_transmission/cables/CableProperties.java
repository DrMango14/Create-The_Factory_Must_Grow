package com.drmangotea.tfmg.content.electricity.content.power_transmission.cables;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;

public class CableProperties extends ElectricalProperties {

    public List<Direction> directions;

    public CableProperties(BlockPos pos, List<Direction> directions) {
        super(pos);
        this.directions = directions;


        DirectionalElectricalNode N = new DirectionalElectricalNode(position,  0, directions,NodeType.NEUTRAL);
        DirectionalElectricalNode L1 = new DirectionalElectricalNode(position, 1, directions,NodeType.PHASE_1);
        DirectionalElectricalNode L2 = new DirectionalElectricalNode(position, 2, directions,NodeType.PHASE_2);
        DirectionalElectricalNode L3 = new DirectionalElectricalNode(position, 3, directions, NodeType.PHASE_3);
        nodes.put(0, N);
        nodes.put(1, L1);
        nodes.put(2, L2);
        nodes.put(3, L3);

    }

    @Override
    public List<Direction> getConnectableDirections() {
        return directions;
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.CABLE.get();
    }
}
