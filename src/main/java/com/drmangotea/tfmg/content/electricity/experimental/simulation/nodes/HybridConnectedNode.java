package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HybridConnectedNode extends ConnectingElectricalNode{


    public HybridConnectedNode(long pos, int localId, List<Direction> facing, CablePos position, NodeType type) {
        super(pos, localId,position, type);
        this.facing = facing;

    }
    public HybridConnectedNode(long pos, int localId, Direction facing, CablePos position,NodeType type) {
        this(pos,localId,new ArrayList<>(Collections.singleton(facing)),position,type);
    }
}
