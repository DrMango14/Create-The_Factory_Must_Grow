package com.drmangotea.tfmg.content.electricity.content.power_transmission.wires;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;

public class UnwireableConnectorProperties extends ElectricalProperties {
	public UnwireableConnectorProperties(BlockPos pos, List<Direction> directions) {
		super(pos);
        nodes.put(0, new DirectionalElectricalNode(position,0,directions, NodeType.CONNECTOR));
    }



    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.UNWIREABLE_CONNECTOR.get();
    }




}
