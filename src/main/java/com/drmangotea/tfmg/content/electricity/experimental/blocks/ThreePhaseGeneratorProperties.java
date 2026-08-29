package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class ThreePhaseGeneratorProperties extends DirectionalElectricalProperties {
    public ThreePhaseGeneratorProperties(long pos1, Direction d) {
        super(pos1,d);

        BlockPos pos = BlockPos.of(position);

        ElectricalNode N = new ConnectingElectricalNode(position, 0, 0,  getRotation(direction).get(0));
        ElectricalNode L1 = new ConnectingElectricalNode(position, 0, 1, getRotation(direction).get(1));
        ElectricalNode L2 = new ConnectingElectricalNode(position, 0, 2, getRotation(direction).get(2));
        ElectricalNode L3 = new ConnectingElectricalNode(position, 0, 3, getRotation(direction).get(3));

        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);
        components.add(new IdealVoltageSource(L1, N, 100, 0, 0));
        components.add(new IdealVoltageSource(L2, N, 100, 120, 1));
        components.add(new IdealVoltageSource(L3, N, 100, 240, 2));
        //components.add(new Resistance(L1, N, 10));
        //components.add(new Resistance(L1, N, 10));

    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction){
            case UP,DOWN,EAST,WEST,NORTH,SOUTH -> {
                positions.add(new CablePos(0, 7/16f,    3/16f));
                positions.add(new CablePos(0, 13/16f,   3/16f));
                positions.add(new CablePos(0, 13/16f,   8/16f));
                positions.add(new CablePos(0, 13/16f,   13/16f));
            }
        };

        return positions;
    }
}
