package com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes;

import net.minecraft.core.Direction;

import java.util.List;

public class DirectionalElectricalNode extends ConnectableElectricalNode {

    public List<Direction> facing;

    public DirectionalElectricalNode(long pos, int localId, List<Direction> facing) {
        super(pos, localId);
        this.facing = facing;
    }
}
