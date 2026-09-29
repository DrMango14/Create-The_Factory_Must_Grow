package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.adapters;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class TwoSlotAdapterProperties extends DirectionalElectricalProperties {


    public TwoSlotAdapterProperties(long pos, Direction direction) {
        super(pos, direction);


        ConnectingElectricalNode N = new ConnectingElectricalNode(position, 0, getRotation(direction).get(0));
        ConnectingElectricalNode L1 = new ConnectingElectricalNode(position, 1, getRotation(direction).get(1));
        ConnectingElectricalNode L2 = new ConnectingElectricalNode(position, 2, getRotation(direction).get(2));
        ConnectingElectricalNode L3 = new ConnectingElectricalNode(position, 3, getRotation(direction).get(3));
        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);


    }

    @Override
    public boolean cableConnectable() {
        return true;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case DOWN, UP, NORTH, SOUTH, WEST, EAST -> {
                positions.add(new CablePos(0, 0, 0));
                positions.add(new CablePos(1, 0, 0));
                positions.add(new CablePos(0, 1, 0));
                positions.add(new CablePos(0, 0, 1));
            }
        }

        return positions;
    }
}
