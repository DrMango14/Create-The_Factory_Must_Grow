package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import java.util.ArrayList;
import java.util.List;

public class MergedNode extends ConnectableElectricalNode{

    public List<ElectricalNode> mergedNodes = new ArrayList<>();

    public MergedNode(long pos, int localId) {
        super(pos, localId,NodeType.MERGED);
    }
    public MergedNode(long pos, int localId,List<ElectricalNode> nodes) {
        super(pos, localId,NodeType.MERGED);
        this.mergedNodes = nodes;
    }

    public MergedNode(long pos, int localId,ElectricalNode... nodes) {
        super(pos, localId,NodeType.MERGED);
        this.mergedNodes = List.of(nodes);
    }


}
