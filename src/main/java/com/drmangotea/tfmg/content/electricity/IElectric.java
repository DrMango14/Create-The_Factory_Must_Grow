package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IElectric extends IHaveMultimeterInformation {
	ElectricalProperties getProperties();

    BlockPos getPos();
	
	Level getWorld();
	
    default ElectricalNetwork getNetwork(Level level) {
        return ElectricNetworkManager.getNetwork(level);
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
            network.nodes.removeAll(this.getProperties().nodes);


        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
        network.update();
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

    default void onPlace() {
        this.updateNetwork(getPos());
		
		connectToNeighbors(getWorld(), this);
        this.updateNetwork(getPos());

    }
	
	static boolean connectToNeighbors(Level level, IElectric be) {
		ElectricalNetwork network = be.getNetwork(level);
		ElectricalProperties properties = network.members.get(be.getPos());
		network.removeConnections(be);
		
		if (properties == null) {
			return false;
		}
		
		for (Direction facing : Direction.values()) {
			BlockPos pos = be.getPos().relative(facing);
			
			ElectricalProperties neighborProperties = network.members.get(pos);
			if (neighborProperties == null) {
				return true;
			}
			
			for (ElectricalNode n1 : properties.nodes) {
				if (!(n1 instanceof ConnectableElectricalNode node)) continue;
				for (ElectricalNode n2 : neighborProperties.nodes) {
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
		return false;
	}
	
	static WireConnection invisibleConnection(ConnectableElectricalNode A, ConnectableElectricalNode B) {
		return new WireConnection(A, B, TFMG.asResource("universal"), false);
	}
}
