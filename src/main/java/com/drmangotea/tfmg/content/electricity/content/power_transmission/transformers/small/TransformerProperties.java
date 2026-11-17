package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small;

import com.drmangotea.tfmg.content.electricity.CablePos;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class TransformerProperties extends DirectionalElectricalProperties {


    public TransformerProperties(long pos1, Direction direction) {
        super(pos1, direction);


        ElectricalNode A = new DirectionalElectricalNode(position, 0, direction.getCounterClockWise(), NodeType.PHASE_1);
        ElectricalNode B = new DirectionalElectricalNode(position, 1, direction.getCounterClockWise(),NodeType.NEUTRAL);
        ElectricalNode a = new DirectionalElectricalNode(position, 2, direction.getCounterClockWise().getOpposite(),NodeType.PHASE_1);
        ElectricalNode b = new DirectionalElectricalNode(position, 3, direction.getCounterClockWise().getOpposite(),NodeType.NEUTRAL);

        nodes.add(A);
        nodes.add(B);
        nodes.add(a);
        nodes.add(b);

        components.add(new Transformer(A,B,a,b,1,10,1,0,position));

    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case UP, DOWN, EAST, WEST, NORTH, SOUTH -> {
                positions.add(new CablePos(1, 7 / 16f, 8 / 16f));
                positions.add(new CablePos(1, 7 / 16f, 13 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 8 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 13 / 16f));
            }
        }
        ;

        return positions;
    }
}
