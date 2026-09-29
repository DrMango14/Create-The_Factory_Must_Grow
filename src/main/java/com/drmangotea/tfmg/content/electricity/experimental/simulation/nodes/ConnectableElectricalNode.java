package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;

public class ConnectableElectricalNode extends ElectricalNode{

    public CablePos position = new CablePos(0,0,0);

    public ConnectableElectricalNode(long pos, int localId) {
        super(pos, localId);
    }

    public CablePos getPosition() {
        return position;
    }
}
