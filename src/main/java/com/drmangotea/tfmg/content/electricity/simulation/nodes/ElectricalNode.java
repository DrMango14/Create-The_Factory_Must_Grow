package com.drmangotea.tfmg.content.electricity.simulation.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class ElectricalNode {
    public int networkId;
    public NodePos pos;

	public ElectricalNode(BlockPos pos, int localId) {
        this.networkId = 0;
        this.pos = new NodePos(pos, localId);
    }
	
	protected ElectricalNode(BlockPos pos, Vec3 offset, int localId) {
		this.networkId = 0;
		this.pos = new NodePos(pos, offset, localId);
	}

    public int getNetworkId() {
        return networkId;
    }

    public int getLocalId() {
        return pos.localId();
    }
}
