package com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.registry.TFMGElectricalProperties.LARGE_GENERATOR;

public class LargeGeneratorProperties extends DirectionalElectricalProperties {
    public LargeGeneratorProperties(BlockPos pos, Direction d) {
        super(pos, d);
		
		List<Direction> dirs = List.of(Direction.values());

        ElectricalNode N  = new DirectionalElectricalNode(position, 0, dirs, NodeType.NEUTRAL);
        ElectricalNode L1 = new DirectionalElectricalNode(position, 1, dirs, NodeType.PHASE_1);
        ElectricalNode L2 = new DirectionalElectricalNode(position, 2, dirs, NodeType.PHASE_2);
        ElectricalNode L3 = new DirectionalElectricalNode(position, 3, dirs, NodeType.PHASE_3);

        ElectricalNode L1r = new ElectricalNode(position, 4);
        ElectricalNode L2r = new ElectricalNode(position, 5);
        ElectricalNode L3r = new ElectricalNode(position, 6);


        nodes.put(0, N);
        nodes.put(1, L1);
        nodes.put(2, L2);
        nodes.put(3, L3);
        nodes.put(4, L1r);
        nodes.put(5, L2r);
        nodes.put(6, L3r);
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
    public ElectricalPropertiesType<?> getType() {
        return LARGE_GENERATOR.get();
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        List<Vec3> positions = new ArrayList<>();

        switch (direction) {
            case EAST -> {
                positions.add(new Vec3(0, 7 / 16f, 3 / 16f));
                positions.add(new Vec3(0, 13 / 16f, 3 / 16f));
                positions.add(new Vec3(0, 13 / 16f, 8 / 16f));
                positions.add(new Vec3(0, 13 / 16f, 13 / 16f));
            }
            case WEST -> {
                positions.add(new Vec3(1, 7 / 16f, 13 / 16f));
                positions.add(new Vec3(1, 13 / 16f, 3 / 16f));
                positions.add(new Vec3(1, 13 / 16f, 8 / 16f));
                positions.add(new Vec3(1, 13 / 16f, 13 / 16f));
            }
            case SOUTH -> {
                positions.add(new Vec3(13 / 16f, 7 / 16f, 0));
                positions.add(new Vec3(3 / 16f, 13 / 16f, 0));
                positions.add(new Vec3(8 / 16f, 13 / 16f, 0));
                positions.add(new Vec3(13 / 16f, 13 / 16f, 0));
            }
            case NORTH -> {
                positions.add(new Vec3(3 / 16f, 7 / 16f, 1));
                positions.add(new Vec3(3 / 16f, 13 / 16f, 1));
                positions.add(new Vec3(8 / 16f, 13 / 16f, 1));
                positions.add(new Vec3(13 / 16f, 13 / 16f, 1));
            }
            case DOWN -> {
                positions.add(new Vec3(3 / 16f, 1f, 9 / 16f));
                positions.add(new Vec3(3 / 16f, 1f, 3 / 16f));
                positions.add(new Vec3(8 / 16f, 1f, 3 / 16f));
                positions.add(new Vec3(13 / 16f, 1f, 3 / 16f));
            }
            case UP -> {
                positions.add(new Vec3(3 / 16f, 0, 7 / 16f));
                positions.add(new Vec3(3 / 16f, 0, 13 / 16f));
                positions.add(new Vec3(8 / 16f, 0, 13 / 16f));
                positions.add(new Vec3(13 / 16f, 0, 13 / 16f));
            }
        }

        return positions;
    }
}
