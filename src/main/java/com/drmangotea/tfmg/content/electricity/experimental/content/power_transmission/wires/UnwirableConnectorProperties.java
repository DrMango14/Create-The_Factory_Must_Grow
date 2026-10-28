package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.wires;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class UnwirableConnectorProperties extends ElectricalProperties {
    public UnwirableConnectorProperties(long pos1, List<Direction> directions) {
        super(pos1);

        BlockPos pos = BlockPos.of(position);
        nodes.add(new DirectionalElectricalNode(position,0,directions, NodeType.CONNECTOR));
    }



    @Override
    public int getId() {
        return 15;
    }




}
