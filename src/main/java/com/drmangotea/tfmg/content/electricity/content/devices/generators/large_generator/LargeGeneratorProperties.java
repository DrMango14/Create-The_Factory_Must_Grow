package com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator;

import com.drmangotea.tfmg.content.electricity.CablePos;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LargeGeneratorProperties extends DirectionalElectricalProperties {
    public LargeGeneratorProperties(long pos1, Direction d) {
        super(pos1, d);

        BlockPos pos = BlockPos.of(position);

        ElectricalNode N = new DirectionalElectricalNode(position, 0, Arrays.stream(Direction.values()).toList(), NodeType.NEUTRAL);
        ElectricalNode L1 = new DirectionalElectricalNode(position, 1, Arrays.stream(Direction.values()).toList(), NodeType.PHASE_1);
        ElectricalNode L2 = new DirectionalElectricalNode(position, 2, Arrays.stream(Direction.values()).toList(), NodeType.PHASE_2);
        ElectricalNode L3 = new DirectionalElectricalNode(position, 3, Arrays.stream(Direction.values()).toList(), NodeType.PHASE_3);

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

        components.add(new Resistance(L1, L1r, 1, 0, position));
        components.add(new Resistance(L2, L2r, 1, 1, position));
        components.add(new Resistance(L3, L3r, 1, 2, position));
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
        return 16;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        List<CablePos> positions = new ArrayList<>();

        switch (direction) {
            case EAST -> {
                positions.add(new CablePos(0, 7 / 16f, 3 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 3 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 8 / 16f));
                positions.add(new CablePos(0, 13 / 16f, 13 / 16f));
            }
            case WEST -> {
                positions.add(new CablePos(1, 7 / 16f, 13 / 16f));
                positions.add(new CablePos(1, 13 / 16f, 3 / 16f));
                positions.add(new CablePos(1, 13 / 16f, 8 / 16f));
                positions.add(new CablePos(1, 13 / 16f, 13 / 16f));
            }
            case SOUTH -> {
                positions.add(new CablePos(13 / 16f, 7 / 16f, 0));
                positions.add(new CablePos(3 / 16f, 13 / 16f, 0));
                positions.add(new CablePos(8 / 16f, 13 / 16f, 0));
                positions.add(new CablePos(13 / 16f, 13 / 16f, 0));
            }
            case NORTH -> {
                positions.add(new CablePos(3 / 16f, 7 / 16f, 1));
                positions.add(new CablePos(3 / 16f, 13 / 16f, 1));
                positions.add(new CablePos(8 / 16f, 13 / 16f, 1));
                positions.add(new CablePos(13 / 16f, 13 / 16f, 1));
            }
            case DOWN -> {
                positions.add(new CablePos(3 / 16f, 1f, 9 / 16f));
                positions.add(new CablePos(3 / 16f, 1f, 3 / 16f));
                positions.add(new CablePos(8 / 16f, 1f, 3 / 16f));
                positions.add(new CablePos(13 / 16f, 1f, 3 / 16f));
            }
            case UP -> {
                positions.add(new CablePos(3 / 16f, 0, 7 / 16f));
                positions.add(new CablePos(3 / 16f, 0, 13 / 16f));
                positions.add(new CablePos(8 / 16f, 0, 13 / 16f));
                positions.add(new CablePos(13 / 16f, 0, 13 / 16f));
            }
        }
        ;

        return positions;
    }
}
