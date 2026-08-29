package com.drmangotea.tfmg.content.electricity.experimental.simulation;

public class ElectricalNode {
    public int networkId;
    public int localId;

    public long pos;

    public ElectricalNode(long pos, int networkId, int localId) {
        this.networkId = networkId;
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
