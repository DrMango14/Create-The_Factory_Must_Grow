package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectionalElectricalNode extends ConnectableElectricalNode {

    public List<Direction> facing;

    public DirectionalElectricalNode(long pos, int localId, List<Direction> facing,NodeType type) {
        super(pos, localId,type);
        this.facing = facing;
    }
    public DirectionalElectricalNode(long pos, int localId, Direction facing,NodeType type) {
        this(pos,localId,new ArrayList<>(Collections.singleton(facing)),type);
    }
}
