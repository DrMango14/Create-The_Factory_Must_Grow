package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables;

import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.List;

public class CableProperties extends ElectricalProperties {

    public List<Direction> directions;

    public CableProperties(long pos, List<Direction> directions) {
        super(pos);
        this.directions = directions;


        DirectionalElectricalNode N = new DirectionalElectricalNode(position,  0, directions,NodeType.NEUTRAL);
        DirectionalElectricalNode L1 = new DirectionalElectricalNode(position, 1, directions,NodeType.PHASE_1);
        DirectionalElectricalNode L2 = new DirectionalElectricalNode(position, 2, directions,NodeType.PHASE_2);
        DirectionalElectricalNode L3 = new DirectionalElectricalNode(position, 3, directions, NodeType.PHASE_3);
        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);


    }

    @Override
    public List<Direction> getConnectableDirections() {
        return directions;
    }

    @Override
    public int getId() {
        return 4;
    }
}
