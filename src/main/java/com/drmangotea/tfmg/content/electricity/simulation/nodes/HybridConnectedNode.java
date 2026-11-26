package com.drmangotea.tfmg.content.electricity.simulation.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HybridConnectedNode extends ConnectingElectricalNode{


    public HybridConnectedNode(BlockPos pos, int localId, List<Direction> facing, Vec3 position, NodeType type) {
        super(pos, localId, position, type);
        this.facing = facing;
    }
	
    public HybridConnectedNode(BlockPos pos, int localId, Direction facing, Vec3 position,NodeType type) {
        this(pos,localId, new ArrayList<>(Collections.singleton(facing)),position,type);
    }
}
