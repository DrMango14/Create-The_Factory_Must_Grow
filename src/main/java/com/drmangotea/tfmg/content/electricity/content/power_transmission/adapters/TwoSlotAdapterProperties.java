package com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters;

import com.drmangotea.tfmg.content.electricity.CablePos;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class TwoSlotAdapterProperties extends DirectionalElectricalProperties {


    public TwoSlotAdapterProperties(long pos, Direction direction) {
        super(pos, direction);


        ConnectingElectricalNode N = new ConnectingElectricalNode( position, 0, getRotation(direction).get(0),NodeType.NEUTRAL);
        ConnectingElectricalNode L1 = new ConnectingElectricalNode(position, 1, getRotation(direction).get(1),NodeType.PHASE_1);

        nodes.add(N);
        nodes.add(L1);



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
            case DOWN -> {

                positions.add(new CablePos(11 / 16f, 12 / 16f, 8 / 16f));

                positions.add(new CablePos(5 / 16f, 12 / 16f, 8 / 16f));
            }
            case UP -> {
                positions.add(new CablePos(5 / 16f, 4 / 16f, 8 / 16f));
                positions.add(new CablePos(11 / 16f, 4 / 16f, 8 / 16f));
            }
            case NORTH -> {
                positions.add(new CablePos(5 / 16f, 8 / 16f, 12 / 16f));
                positions.add(new CablePos(11 / 16f, 8 / 16f, 12 / 16f));
            }
            case SOUTH -> {
                positions.add(new CablePos(11 / 16f, 8 / 16f, 4 / 16f));
                positions.add(new CablePos(5 / 16f, 8 / 16f, 4 / 16f));
            }
            case EAST -> {
                positions.add(new CablePos(4 / 16f, 8 / 16f, 11 / 16f));
                positions.add(new CablePos(4 / 16f, 8 / 16f, 5 / 16f));
            }
            case WEST -> {
                positions.add(new CablePos(12 / 16f, 8 / 16f, 5 / 16f));
                positions.add(new CablePos(12 / 16f, 8 / 16f, 11 / 16f));
            }
        }

        return positions;
    }
}
