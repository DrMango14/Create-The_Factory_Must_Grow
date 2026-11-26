package com.drmangotea.tfmg.content.electricity.simulation.nodes;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class MergedNode extends ConnectableElectricalNode{

    public List<ElectricalNode> mergedNodes = new ArrayList<>();

    public MergedNode(BlockPos pos, int localId) {
        super(pos, localId, NodeType.MERGED);
    }
    public MergedNode(BlockPos pos, int localId,List<ElectricalNode> nodes) {
        super(pos, localId, NodeType.MERGED);
        this.mergedNodes = nodes;
    }

    public MergedNode(BlockPos pos, int localId,ElectricalNode... nodes) {
        super(pos, localId, NodeType.MERGED);
        this.mergedNodes = List.of(nodes);
    }


}
