package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import net.minecraft.core.Direction;

import java.util.List;

public class ConnectableElectricalNode extends ElectricalNode{

    public CablePos position = new CablePos(0,0,0);

    public List<Direction> facing;

    public final NodeType type;

    public ConnectableElectricalNode(long pos, int localId, NodeType type) {
        super(pos, localId);
        this.type = type;
    }

    public CablePos getPosition() {
        return position;
    }
}
