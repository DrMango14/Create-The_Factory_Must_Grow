package com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.large_switch;

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

public class LargeSwitchProperties extends DirectionalElectricalProperties {

    public LargeSwitchProperties(BlockPos pos, Direction direction) {
        this(pos, direction, 1000);
    }

    public LargeSwitchProperties(BlockPos pos, Direction direction, double r) {
        super(pos, direction);

        DirectionalElectricalNode IN = new DirectionalElectricalNode(position, 0, direction, NodeType.CONNECTOR);
        DirectionalElectricalNode OUT = new DirectionalElectricalNode(position, 1, direction.getOpposite(), NodeType.CONNECTOR);


        nodes.put(0, IN);
        nodes.put(1, OUT);

        components.add(new Resistance(IN, OUT, r, 0, position));
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
        return TFMGElectricalProperties.LARGE_SWITCH.get();
    }
}
