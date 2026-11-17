package com.drmangotea.tfmg.content.electricity.content.power_transmission.wires;

import com.drmangotea.tfmg.content.electricity.CablePos;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.HybridConnectedNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ConnectorProperties extends DirectionalElectricalProperties {
    public ConnectorProperties(long pos1, Direction d) {
        super(pos1,d);

        List<Direction> directions = new ArrayList<>();
        directions.add(d);
        directions.add(d.getOpposite());

        BlockPos pos = BlockPos.of(position);
        nodes.add(new HybridConnectedNode(position,0,directions,new CablePos(0.5f,0.5f,0.5f), NodeType.CONNECTOR));
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        return List.of();
    }


    @Override
    public int getId() {
        return 2;
    }

    public CompoundTag saveData(CompoundTag compound){
            return compound;
    }
}
