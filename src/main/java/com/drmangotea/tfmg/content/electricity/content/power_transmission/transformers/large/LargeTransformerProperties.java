package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large;

import com.drmangotea.tfmg.content.electricity.CablePos;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class LargeTransformerProperties extends DirectionalElectricalProperties {


    public LargeTransformerProperties(long pos1, Direction direction) {
        super(pos1, direction);

        ElectricalNode N = new ConnectingElectricalNode(position, 0, getRotation(direction).get(0), NodeType.NEUTRAL);
        ElectricalNode U = new ConnectingElectricalNode(position, 1, getRotation(direction).get(1), NodeType.PHASE_1);
        ElectricalNode V = new ConnectingElectricalNode(position, 2, getRotation(direction).get(2), NodeType.PHASE_2);
        ElectricalNode W = new ConnectingElectricalNode(position, 3, getRotation(direction).get(3), NodeType.PHASE_3);
        ElectricalNode u = new ConnectingElectricalNode(position, 4, getRotation(direction).get(4), NodeType.PHASE_1);
        ElectricalNode v = new ConnectingElectricalNode(position, 5, getRotation(direction).get(5), NodeType.PHASE_2);
        ElectricalNode w = new ConnectingElectricalNode(position, 6, getRotation(direction).get(6), NodeType.PHASE_3);
       // ElectricalNode n = new ConnectingElectricalNode(position, 7, getRotation(direction).get(7), NodeType.NEUTRAL);


        nodes.add(N);
        nodes.add(U);
        nodes.add(V);
        nodes.add(W);
        nodes.add(u);
        nodes.add(v);
        nodes.add(w);
       // nodes.add(n);

        components.add(new Transformer(U, N, u, N, 1, 10, 1, 0, position));
        components.add(new Transformer(V, N, v, N, 1, 10, 1, 1, position));
        components.add(new Transformer(W, N, w, N, 1, 10, 1, 2, position));

    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case SOUTH -> {
                positions.add(new CablePos(0 / 16f, 14 / 16f, 4 / 16f));
                positions.add(new CablePos(-3 / 16f, 18 / 16f, 9 / 16f));
                positions.add(new CablePos(-3 / 16f, 18 / 16f, 16 / 16f));
                positions.add(new CablePos(-3 / 16f, 18 / 16f, 23 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, 7.5 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, 16 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, 24.5 / 16f));
                positions.add(new CablePos(16 / 16f, 14 / 16f, 4 / 16f));

            }
            case NORTH -> {
                positions.add(new CablePos(16 / 16f, 14 / 16f, 12 / 16f));
                positions.add(new CablePos(19 / 16f, 18 / 16f, 7 / 16f));
                positions.add(new CablePos(19 / 16f, 18 / 16f, 0 / 16f));
                positions.add(new CablePos(19 / 16f, 18 / 16f, -7 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, 8.5 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, 0 / 16f));
                positions.add(new CablePos(8 / 16f, 26 / 16f, -8.5 / 16f));
                positions.add(new CablePos(0 / 16f, 14 / 16f, 12 / 16f));
            }
            case EAST -> {
                positions.add(new CablePos(4 / 16f, 14 / 16f, 0 / 16f));
                positions.add(new CablePos(9 / 16f, 18 / 16f,  19 / 16f));
                positions.add(new CablePos(16 / 16f, 18 / 16f, 19 / 16f));
                positions.add(new CablePos(23 / 16f, 18 / 16f, 19 / 16f));
                positions.add(new CablePos(7.5 / 16f, 26 / 16f, 8 / 16f));
                positions.add(new CablePos(16 / 16f, 26 / 16f, 8 / 16f));
                positions.add(new CablePos(24.5 / 16f, 26 / 16f, 8 / 16f));
                positions.add(new CablePos(4 / 16f, 14 / 16f, 16 / 16f));
            }
            default ->  {
                positions.add(new CablePos(12 / 16f, 14 / 16f, 16/16f));
                positions.add(new CablePos(7 / 16f , 18 / 16f, -3/16f));
                positions.add(new CablePos(0 / 16f , 18 / 16f, -3/16f));
                positions.add(new CablePos(-7 / 16f, 18 / 16f, -3/16f));
                positions.add(new CablePos(8.5 / 16f , 26 / 16f, 8/16f ));
                positions.add(new CablePos(0 / 16f , 26 / 16f, 8/16f ));
                positions.add(new CablePos(-8.5 / 16f, 26 / 16f, 8/16f ));
                positions.add(new CablePos(12 / 16f, 14 / 16f, 0/16f ));
            }

        }
        ;

        return positions;
    }
}
