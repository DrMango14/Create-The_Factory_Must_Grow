package com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.creative_generator;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class CreativeGeneratorProperties extends ElectricalProperties {
    public CreativeGeneratorProperties(long pos1) {
        super(pos1);

        BlockPos pos = BlockPos.of(position);

        ElectricalNode N = new ConnectingElectricalNode(position, 0, getRotation(0 ),NodeType.NEUTRAL);
        ElectricalNode L1 = new ConnectingElectricalNode(position, 1, getRotation(1), NodeType.PHASE_1);
        ElectricalNode L2 = new ConnectingElectricalNode(position, 2, getRotation(2),NodeType.PHASE_2);
        ElectricalNode L3 = new ConnectingElectricalNode(position, 3, getRotation(3),NodeType.PHASE_3);


        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);
        components.add(new IdealVoltageSource(L1, N, 100, 99999, 0, 0, position));
        components.add(new IdealVoltageSource(L2, N, 100, 99999, 120, 1, position));
        components.add(new IdealVoltageSource(L3, N, 100, 99999, 240, 2, position));


    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public boolean cableConnectable() {
        return true;
    }


    @Override
    public int getId() {
        return 10;
    }

    @Override
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
    }

    public CablePos getRotation(int i) {
        CablePos position = new CablePos(0,0,0);

        switch (i) {
            case 0 -> position = new CablePos(1,13/16f,0);
            case 1 -> position = new CablePos(1,1,1);
            case 2 -> position = new CablePos(0,1,1);
            case 3 -> position = new CablePos(0,1,0);
        }
        ;

        return position;
    }
}
