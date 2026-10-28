package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.large_switch;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class LargeSwitchProperties extends DirectionalElectricalProperties {

    public LargeSwitchProperties(long pos, Direction direction) {
        this(pos, direction, 1000);
    }

    public LargeSwitchProperties(long pos, Direction direction, double r) {
        super(pos, direction);
        BlockPos pos1 = BlockPos.of(position);

        List<Direction> directions = new ArrayList<>();
        directions.add(direction);
        directions.add(direction.getOpposite());

        DirectionalElectricalNode IN = new DirectionalElectricalNode(position, 0, direction, NodeType.CONNECTOR);
        DirectionalElectricalNode OUT = new DirectionalElectricalNode(position, 1, direction.getOpposite(), NodeType.CONNECTOR);


        nodes.add(IN);
        nodes.add(OUT);



        components.add(new Resistance(IN, OUT, r, 0, position));


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
        return 14;
    }
}
