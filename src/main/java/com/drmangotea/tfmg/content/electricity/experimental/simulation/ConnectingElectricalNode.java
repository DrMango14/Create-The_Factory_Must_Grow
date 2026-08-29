package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;

public class ConnectingElectricalNode extends ElectricalNode {

    public CablePos position;

    public ConnectingElectricalNode(long pos, int networkId, int localId, CablePos position) {
        super(pos,networkId, localId);
        this.position = position;
    }

    public CablePos getPosition() {
        return position;
    }

}
