package com.drmangotea.tfmg.content.electricity.simulation;

import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;

import java.util.List;

public abstract class ElectricalComponent {
    public final ElectricalNode nodeA;
    public final ElectricalNode nodeB;

    public ElectricalComponent(ElectricalNode nodeA, ElectricalNode nodeB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public int getExtraRows() {
        return 0;
    }

    public abstract List<ElectricalNode> getConnectedNodes();

    public abstract void stamp(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset);
}
