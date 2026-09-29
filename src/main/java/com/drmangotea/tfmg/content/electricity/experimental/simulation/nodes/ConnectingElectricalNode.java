package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;

public class ConnectingElectricalNode extends ConnectableElectricalNode {



    public ConnectingElectricalNode(long pos, int localId, CablePos position) {
        super(pos, localId);
        this.position = position;
    }



}
