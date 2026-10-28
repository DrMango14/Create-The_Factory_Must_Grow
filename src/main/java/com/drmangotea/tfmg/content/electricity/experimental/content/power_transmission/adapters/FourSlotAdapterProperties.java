package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.adapters;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.HybridConnectedNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FourSlotAdapterProperties extends DirectionalElectricalProperties {


    public FourSlotAdapterProperties(long pos, Direction direction) {
        super(pos, direction);

        List<Direction> directions = new ArrayList<>();
        directions.add(direction.getOpposite());

        HybridConnectedNode N = new HybridConnectedNode(position, 0 ,directions, getRotation(direction) .get(0),NodeType.NEUTRAL);
        HybridConnectedNode L1 = new HybridConnectedNode(position, 1,directions, getRotation(direction).get(1),NodeType.PHASE_1);
        HybridConnectedNode L2 = new HybridConnectedNode(position, 2,directions, getRotation(direction).get(2),NodeType.PHASE_2);
        HybridConnectedNode L3 = new HybridConnectedNode(position, 3,directions, getRotation(direction).get(3), NodeType.PHASE_3);

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
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case DOWN -> {
                positions.add(new CablePos(8/16f, 12/16f, 10/16f));
                positions.add(new CablePos(11/16f, 12/16f, 7/16f));
                positions.add(new CablePos(8/16f, 12/16f, 7/16f));
                positions.add(new CablePos(5/16f, 12/16f, 7/16f));
            }
            case UP ->{
                positions.add(new CablePos(8/16f, 4/16f, 6/16f));
                positions.add(new CablePos(5/16f, 4/16f, 9/16f));
                positions.add(new CablePos(8/16f, 4/16f, 9/16f));
                positions.add(new CablePos(11/16f, 4/16f, 9/16f));
            }
            case NORTH ->{
                positions.add(new CablePos(8/16f, 6/16f, 12/16f));
                positions.add(new CablePos(5/16f, 9/16f, 12/16f));
                positions.add(new CablePos(8/16f, 9/16f, 12/16f));
                positions.add(new CablePos(11/16f, 9/16f, 12/16f));
            }
            case SOUTH ->{
                positions.add(new CablePos(8/16f, 6/16f,  4/16f));
                positions.add(new CablePos(11/16f, 9/16f, 4/16f));
                positions.add(new CablePos(8/16f, 9/16f, 4/16f));
                positions.add(new CablePos(5/16f, 9/16f, 4/16f));
            }
            case EAST ->{
                positions.add(new CablePos(4/16f, 6/16f, 8/16f));
                positions.add(new CablePos(4/16f, 9/16f, 11/16f));
                positions.add(new CablePos(4/16f, 9/16f, 8/16f));
                positions.add(new CablePos(4/16f, 9/16f, 5/16f));
            }
            case WEST ->{
                positions.add(new CablePos(12/16f, 6/16f, 8/16f));
                positions.add(new CablePos(12/16f, 9/16f, 5/16f));
                positions.add(new CablePos(12/16f, 9/16f, 8/16f));
                positions.add(new CablePos(12/16f, 9/16f, 11/16f));
            }
        }

        return positions;
    }
}
