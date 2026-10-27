package com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.three_phase_generator;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class ThreePhaseGeneratorProperties extends DirectionalElectricalProperties {
    public ThreePhaseGeneratorProperties(long pos1, Direction d) {
        super(pos1, d);

        BlockPos pos = BlockPos.of(position);

        ElectricalNode N = new ConnectingElectricalNode(position, 0, getRotation(direction).get(0), NodeType.NEUTRAL);
        ElectricalNode L1 = new ConnectingElectricalNode(position, 1, getRotation(direction).get(1), NodeType.PHASE_1);
        ElectricalNode L2 = new ConnectingElectricalNode(position, 2, getRotation(direction).get(2), NodeType.PHASE_2);
        ElectricalNode L3 = new ConnectingElectricalNode(position, 3, getRotation(direction).get(3), NodeType.PHASE_3);

        ElectricalNode L1r = new ElectricalNode(position, 4);
        ElectricalNode L2r = new ElectricalNode(position, 5);
        ElectricalNode L3r = new ElectricalNode(position, 6);


        nodes.add(N);
        nodes.add(L1);
        nodes.add(L2);
        nodes.add(L3);
        nodes.add(L1r);
        nodes.add(L2r);
        nodes.add(L3r);
        components.add(new IdealVoltageSource(L1r, N, 100, 12000, 0, 0, position));
        components.add(new IdealVoltageSource(L2r, N, 100, 12000, 120, 1, position));
        components.add(new IdealVoltageSource(L3r, N, 100, 12000, 240, 2, position));

        components.add(new Resistance(L1, L1r, 5, 0, position));
        components.add(new Resistance(L2, L2r, 5, 1, position));
        components.add(new Resistance(L3, L3r, 5, 2, position));
        //components.add(new Resistance(L1, N, 10));
        //components.add(new Resistance(L1, N, 10));

    }

    @Override
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
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
        return 1;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case UP, DOWN, EAST, WEST, NORTH, SOUTH -> {
                positions.add(new CablePos(0, 7 / 16f, 3 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 3 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 8 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 13 / 16f));
            }
        }
        ;

        return positions;
    }
}
