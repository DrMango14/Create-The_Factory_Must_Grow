package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables;

import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import net.minecraft.core.Direction;

import java.util.List;

public class CableProperties extends ElectricalProperties {

    List<Direction> directions;

    public CableProperties(long pos, List<Direction> directions) {
        super(pos);
        this.directions = directions;


        DirectionalElectricalNode N = new DirectionalElectricalNode(position, 0, directions);
        DirectionalElectricalNode L1 = new DirectionalElectricalNode(position, 1, directions);
        DirectionalElectricalNode L2 = new DirectionalElectricalNode(position, 2, directions);
        DirectionalElectricalNode L3 = new DirectionalElectricalNode(position, 3, directions);
        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);


    }

    @Override
    public int getId() {
        return 4;
    }
}
