package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;

public record WireConnection (
	ConnectableElectricalNode node1,
	ConnectableElectricalNode node2,
	ResourceLocation cableTypeKey,
	boolean render
) {
	
	public CableType cableType () {
		return TFMGUtils.getCableType(cableTypeKey);
	}
	
	public CompoundTag save () {
		CompoundTag connectionTag = new CompoundTag();
		
		connectionTag.putString("Cable", cableTypeKey.toString());
		connectionTag.putBoolean("Should Render", render);
		connectionTag.put("Node1", node1.save());
		connectionTag.put("Node2", node2.save());
		
		return connectionTag;
	}
	
	public static WireConnection readTag (CompoundTag connection, ElectricalNetwork network) {
		ResourceLocation type = ResourceLocation.parse(connection.getString("Cable"));
		boolean shouldRender = connection.getBoolean("Should Render");
		
		ConnectableElectricalNode node1 = getNode(connection.getCompound("Node1"), network);
		if (node1 == null) return null;
		ConnectableElectricalNode node2 = getNode(connection.getCompound("Node2"), network);
		if (node2 == null) return null;
		
		return new WireConnection(node1, node2, type, shouldRender);
	}
	
	private static ConnectingElectricalNode getNode(CompoundTag tag, ElectricalNetwork network) {
		ConnectingElectricalNode node = null;
		BlockPos pos = NbtUtils.readBlockPos(tag, "position").orElse(null);
		if (pos == null) return node;
		int id1 = tag.getInt("local id");
		for (ElectricalNode existingNode : network.getNodes(pos)) {
			if (existingNode.localId == id1 && existingNode instanceof ConnectingElectricalNode connectingNode) {
				node = connectingNode;
				node.pos = pos;
			}
		}
		return node;
	}
}
