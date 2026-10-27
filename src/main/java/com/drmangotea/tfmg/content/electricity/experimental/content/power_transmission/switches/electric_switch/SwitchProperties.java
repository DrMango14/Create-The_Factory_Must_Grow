package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.electric_switch;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class SwitchProperties extends DirectionalElectricalProperties {

    public SwitchProperties(long pos, Direction direction) {
        this(pos, direction, 1000);
    }

    public SwitchProperties(long pos, Direction direction, double r) {
        super(pos, direction);
        BlockPos pos1 = BlockPos.of(position);

        List<Direction> directions = new ArrayList<>();
        directions.add(direction);
        directions.add(direction.getOpposite());

        DirectionalElectricalNode N = new DirectionalElectricalNode(position, 0, directions, NodeType.NEUTRAL);
        DirectionalElectricalNode L1A = new DirectionalElectricalNode(position, 1, direction, NodeType.PHASE_1);
        DirectionalElectricalNode L2A = new DirectionalElectricalNode(position, 2, direction, NodeType.PHASE_2);
        DirectionalElectricalNode L3A = new DirectionalElectricalNode(position, 3, direction, NodeType.PHASE_3);
        DirectionalElectricalNode L1B = new DirectionalElectricalNode(position, 4, direction.getOpposite(), NodeType.PHASE_1);
        DirectionalElectricalNode L2B = new DirectionalElectricalNode(position, 5, direction.getOpposite(), NodeType.PHASE_2);
        DirectionalElectricalNode L3B = new DirectionalElectricalNode(position, 6, direction.getOpposite(), NodeType.PHASE_3);


        nodes.add(N);
        nodes.add(L1A);
        nodes.add(L2A);
        nodes.add(L3A);
        nodes.add(L1B);
        nodes.add(L2B);
        nodes.add(L3B);


        components.add(new Resistance(L1A, L1B, r, 0, position));
        components.add(new Resistance(L2A, L2B, r, 1, position));
        components.add(new Resistance(L3A, L3B, r, 2, position));

    }

    public List<CablePos> getRotation(Direction direction) {

        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case DOWN, UP -> {
                positions.add(new CablePos(0.5f, 1, 0.5f));
                positions.add(new CablePos(0.5f, 0, 0.5f));
            }
            case WEST, EAST -> {
                positions.add(new CablePos(0.5f, 0.5f, 1));
                positions.add(new CablePos(0.5f, 0.5f, 0));
            }
            case NORTH, SOUTH -> {
                positions.add(new CablePos(1, 0.5f, 0.5f));
                positions.add(new CablePos(0, 0.5f, 0.5f));
            }


        }
        ;

        return positions;
    }
    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public int getId() {
        return 11;
    }
}
