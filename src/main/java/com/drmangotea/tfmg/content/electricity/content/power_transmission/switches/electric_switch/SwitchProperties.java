package com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.electric_switch;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SwitchProperties extends DirectionalElectricalProperties {

    public SwitchProperties(BlockPos pos, Direction direction) {
        this(pos, direction, 1000);
    }

    public SwitchProperties(BlockPos pos, Direction direction, double r) {
        super(pos, direction);

        List<Direction> directions = new ArrayList<>();
        directions.add(direction);
        directions.add(direction.getOpposite());

        DirectionalElectricalNode N = new DirectionalElectricalNode(position, 0, directions, NodeType.NEUTRAL);
        DirectionalElectricalNode L1A = new DirectionalElectricalNode(position, 1, direction, NodeType.PHASE_1);
        DirectionalElectricalNode L2A = new DirectionalElectricalNode(position, 2, direction, NodeType.PHASE_2);
        DirectionalElectricalNode L3A = new DirectionalElectricalNode(position, 3, direction, NodeType.PHASE_3);
        DirectionalElectricalNode L1B = new DirectionalElectricalNode(position, 4, direction.getOpposite(), NodeType.PHASE_1);
        DirectionalElectricalNode L2B = new DirectionalElectricalNode(position, 5, direction.getOpposite(), NodeType.PHASE_2);
        DirectionalElectricalNode L3B = new DirectionalElectricalNode(position, 6, direction.getOpposite(), NodeType.PHASE_3);


        nodes.put(0, N);
        nodes.put(1,L1A);
        nodes.put(2, L2A);
        nodes.put(3, L3A);
        nodes.put(4, L1B);
        nodes.put(5, L2B);
        nodes.put(6, L3B);


        components.add(new Resistance(L1A, L1B, r, 0, position));
        components.add(new Resistance(L2A, L2B, r, 1, position));
        components.add(new Resistance(L3A, L3B, r, 2, position));

    }

    public List<Vec3> getRotation(Direction direction) {

        List<Vec3> positions = new ArrayList<>();

        switch (direction) {
            case DOWN, UP -> {
                positions.add(new Vec3(0.5f, 1, 0.5f));
                positions.add(new Vec3(0.5f, 0, 0.5f));
            }
            case WEST, EAST -> {
                positions.add(new Vec3(0.5f, 0.5f, 1));
                positions.add(new Vec3(0.5f, 0.5f, 0));
            }
            case NORTH, SOUTH -> {
                positions.add(new Vec3(1, 0.5f, 0.5f));
                positions.add(new Vec3(0, 0.5f, 0.5f));
            }


        }

        return positions;
    }
    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.SWITCH.get();
    }
}
