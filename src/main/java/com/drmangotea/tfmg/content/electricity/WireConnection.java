package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;

public record WireConnection (
	ConnectableElectricalNode node1,
	ConnectableElectricalNode node2,
	double resistance,
	boolean render,
	PartialModel model
) {
	
	public CompoundTag save () {
		
		CompoundTag connectionTag = new CompoundTag();
		
		//TODO: Store cable type, rather than resistance and model
		connectionTag.putDouble("Resistance", resistance);
		connectionTag.putBoolean("Should Render", render);
		connectionTag.put("Node1", node1.save());
		connectionTag.put("Node2", node2.save());
		
		return connectionTag;
	}
	
	public static WireConnection readTag (CompoundTag connection, ElectricalNetwork network) {
		double resistance = connection.getDouble("Resistance");
		boolean shouldRender = connection.getBoolean("Should Render");
		PartialModel model = PartialModel.of(ResourceLocation.parse(connection.getString("Model")));
		
		ConnectableElectricalNode node1 = getNode(connection.getCompound("Node1"), network);
		if (node1 == null) return null;
		ConnectableElectricalNode node2 = getNode(connection.getCompound("Node2"), network);
		if (node2 == null) return null;
		
		return new WireConnection(node1, node2, resistance, shouldRender, model);
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
