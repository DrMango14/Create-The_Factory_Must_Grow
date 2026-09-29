package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

public class ElectricalNode {
    public int networkId;
    public int localId;

    public long pos;

    public ElectricalNode(long pos, int localId) {
        this.networkId = 0;
        this.localId = localId;
        this.pos = pos;
    }

    public int getNetworkId() {
        return networkId;
    }

    public int getLocalId() {
        return localId;
    }
}
