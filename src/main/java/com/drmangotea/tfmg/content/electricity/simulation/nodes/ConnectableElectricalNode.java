package com.drmangotea.tfmg.content.electricity.simulation.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

import java.util.Arrays;
import java.util.List;

public class ConnectableElectricalNode extends ElectricalNode {

    public List<Direction> facing = Arrays.stream(Direction.values()).toList();

    public final NodeType type;

    public ConnectableElectricalNode(BlockPos pos, int localId, NodeType type) {
        super(pos, localId);
        this.type = type;
    }
	
	protected ConnectableElectricalNode(BlockPos pos, Vec3 offset, int localId, NodeType type) {
		super(pos, offset, localId);
		this.type = type;
	}

    public Vec3 getPosition() {
        return pos.offset();
    }
}
