package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IElectric extends IHaveMultimeterInformation {
	ElectricalProperties getProperties();

    BlockPos getPos();
	
	Level getWorld();
	
    default ElectricalNetwork getNetwork(Level level) {
        return ElectricalNetworkManager.getNetwork(level);
    }

    default ElectricalNetwork getNetwork() {
        return getNetwork(getWorld());
    }

    default void updateNetwork(BlockPos pos) {
		ElectricalNetwork network = getNetwork();
        if (network == null)
            return;
        network.addMember(pos, getProperties());
        network.update();
        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
    }

    default void removeBlock() {
        ElectricalNetwork network = getNetwork();
        network.members.remove(this.getPos());
        network.removeConnections(this);
        if (this.getProperties() != null)
            network.nodes.removeAll(this.getProperties().nodes.values());

		network.update();
        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
    }

    default void onUpdated() {
    }

    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("multimeter").style(ChatFormatting.WHITE)
                .forGoggles(tooltip);

		/* TODO: Add getters or something like that
		TFMGTexts.Multimeter.resistance(resistance).forGoggles(tooltip);
		TFMGTexts.Multimeter.voltage(voltage).forGoggles(tooltip);
		TFMGTexts.Multimeter.current(current).forGoggles(tooltip);
		TFMGTexts.Multimeter.power(current*voltage).forGoggles(tooltip);
		*/

        return false;
    }

	/// This updates the network, calls {@link IElectric#whilePlacing}, then updates the network again.
	/// Then it sends a packet, so keep that in mind if overriding.
    default void onPlace() {
        this.updateNetwork(getPos());
		
		whilePlacing();
        this.updateNetwork(getPos());

		if (getWorld() instanceof ServerLevel serverLevel)
			CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getPos()), new AddElectricalComponentPacket(getPos()));
    }
	
	/// This gets called during placement logic, between network updates.
	/// This allows for changing that behaviour without having to then call for additional network updates.
	default void whilePlacing() {
		connectToNeighbors(getWorld(), this);
	}
	
	/// Creates invisible connections between neighbouring IElectrics
	/// @return ???? I have no idea mango, I think it's about trying again
	static boolean connectToNeighbors(Level level, IElectric be) {
		ElectricalNetwork network = be.getNetwork(level);
		ElectricalProperties properties = network.members.get(be.getPos());
		network.removeConnections(be);
		
		if (properties == null) {
			return false;
		}
		
		boolean bool = false;
		for (Direction facing : Direction.values()) {
			BlockPos pos = be.getPos().relative(facing);
			if (!(level.getBlockEntity(pos) instanceof IElectric)) continue;
			//no reason to expect properties from this, so just move on.
			
			ElectricalProperties neighborProperties = network.members.get(pos);
			if (neighborProperties == null) {
				bool = true; //To not cut the search short
				continue;
			}
			
			for (ElectricalNode n1 : properties.nodes.values()) {
				if (!(n1 instanceof ConnectableElectricalNode node)) continue;
				for (ElectricalNode n2 : neighborProperties.nodes.values()) {
					if (!(n2 instanceof ConnectableElectricalNode neighborNode)) continue;
					
					if (node.type != neighborNode.type) continue;
					
					if (node.facing.contains(facing) && neighborNode.facing.contains(facing.getOpposite())) {
						WireConnection connection = invisibleConnection(node, neighborNode);
						if (!network.connections.contains(connection))
							network.connections.add(connection);
					}
				}
			}
		}
		return bool;
	}
	
	static WireConnection invisibleConnection(ConnectableElectricalNode A, ConnectableElectricalNode B) {
		return new WireConnection(A.pos, B.pos, TFMG.asResource("universal"), false);
	}
}
