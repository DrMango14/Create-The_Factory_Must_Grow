package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class DebugResistorProperties extends DirectionalElectricalProperties {


    public DebugResistorProperties(long pos, Direction direction) {
        super(pos,direction);
        BlockPos pos1 = BlockPos.of(position);
        ConnectingElectricalNode node1 = new ConnectingElectricalNode(position, 0, 0, getRotation(this.direction).getFirst());
        ConnectingElectricalNode node2 = new ConnectingElectricalNode(position, 0, 1, getRotation(this.direction).get(1));
        nodes.add(node1);
        nodes.add(node2);
        components.add(new Resistance(node1, node2, 10, 0));

    }

    public List<CablePos> getRotation(Direction direction) {

        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case DOWN,UP -> {
                positions.add(new CablePos(0.5f,1,0.5f));
                positions.add(new CablePos(0.5f,0,0.5f));
            }
            case WEST,EAST -> {
                positions.add(new CablePos(1,0.5f,0.5f));
                positions.add(new CablePos(0,0.5f,0.5f));
            }
            case NORTH,SOUTH -> {
                positions.add(new CablePos(0.5f,0.5f,1));
                positions.add(new CablePos(0.5f,0.5f,0));
            }


        };

        return positions;
    }

    @Override
    public int getId() {
        return 3;
    }
}
