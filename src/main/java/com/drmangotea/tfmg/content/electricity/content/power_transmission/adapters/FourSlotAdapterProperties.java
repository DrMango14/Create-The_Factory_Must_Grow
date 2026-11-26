package com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.HybridConnectedNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FourSlotAdapterProperties extends DirectionalElectricalProperties {
	
	public FourSlotAdapterProperties(BlockPos pos, Direction direction) {
		super(pos, direction);
		List<Vec3> positions = getRotation(direction);
		
		List<Direction> directions = new ArrayList<>();
		directions.add(direction.getOpposite());
		
		nodes.put(0, new HybridConnectedNode(position, 0, directions, positions.get(0), NodeType.NEUTRAL));
		nodes.put(1, new HybridConnectedNode(position, 1, directions, positions.get(1), NodeType.PHASE_1));
		nodes.put(2, new HybridConnectedNode(position, 2, directions, positions.get(2), NodeType.PHASE_2));
		nodes.put(3, new HybridConnectedNode(position, 3, directions, positions.get(3), NodeType.PHASE_3));
    }

    @Override
    public boolean cableConnectable() {
        return true;
    }

    @Override
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.FOUR_SLOT_ADAPTER.get();
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        List<Vec3> positions = new ArrayList<>();

        switch (direction) {
            case DOWN -> {
                positions.add(new Vec3(8 / 16f, 12 / 16f, 10 / 16f));
                positions.add(new Vec3(11 / 16f, 12 / 16f, 7 / 16f));
                positions.add(new Vec3(8 / 16f, 12 / 16f, 7 / 16f));
                positions.add(new Vec3(5 / 16f, 12 / 16f, 7 / 16f));
            }
            case UP -> {
                positions.add(new Vec3(8 / 16f, 4 / 16f, 6 / 16f));
                positions.add(new Vec3(5 / 16f, 4 / 16f, 9 / 16f));
                positions.add(new Vec3(8 / 16f, 4 / 16f, 9 / 16f));
                positions.add(new Vec3(11 / 16f, 4 / 16f, 9 / 16f));
            }
            case NORTH -> {
                positions.add(new Vec3(8 / 16f, 6 / 16f, 12 / 16f));
                positions.add(new Vec3(5 / 16f, 9 / 16f, 12 / 16f));
                positions.add(new Vec3(8 / 16f, 9 / 16f, 12 / 16f));
                positions.add(new Vec3(11 / 16f, 9 / 16f, 12 / 16f));
            }
            case SOUTH -> {
                positions.add(new Vec3(8 / 16f, 6 / 16f, 4 / 16f));
                positions.add(new Vec3(11 / 16f, 9 / 16f, 4 / 16f));
                positions.add(new Vec3(8 / 16f, 9 / 16f, 4 / 16f));
                positions.add(new Vec3(5 / 16f, 9 / 16f, 4 / 16f));
            }
            case EAST -> {
                positions.add(new Vec3(4 / 16f, 6 / 16f, 8 / 16f));
                positions.add(new Vec3(4 / 16f, 9 / 16f, 11 / 16f));
                positions.add(new Vec3(4 / 16f, 9 / 16f, 8 / 16f));
                positions.add(new Vec3(4 / 16f, 9 / 16f, 5 / 16f));
            }
            case WEST -> {
                positions.add(new Vec3(12 / 16f, 6 / 16f, 8 / 16f));
                positions.add(new Vec3(12 / 16f, 9 / 16f, 5 / 16f));
                positions.add(new Vec3(12 / 16f, 9 / 16f, 8 / 16f));
                positions.add(new Vec3(12 / 16f, 9 / 16f, 11 / 16f));
            }
        }

        return positions;
    }
}
