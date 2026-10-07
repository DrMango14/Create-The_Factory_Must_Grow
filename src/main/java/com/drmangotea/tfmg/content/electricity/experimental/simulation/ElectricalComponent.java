package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;

import java.util.List;

public abstract class ElectricalComponent {
    public ElectricalNode nodeA;
    public ElectricalNode nodeB;

    public final ElectricalNode localNodeA;
    public final ElectricalNode localNodeB;


    public ElectricalComponent(ElectricalNode nodeA, ElectricalNode nodeB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.localNodeA = nodeA;
        this.localNodeB = nodeB;

    }

    public int getExtraRows() {
        return 0;
    }

    public abstract List<ElectricalNode> getConnectedNodes();

    public abstract void stamp(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset);

    public void setNodeA(ElectricalNode nodeA) {
        this.nodeA = nodeA;
    }

    public void setNodeB(ElectricalNode nodeB) {
        this.nodeB = nodeB;
    }
}
