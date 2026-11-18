package com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TwoSlotAdapterProperties extends DirectionalElectricalProperties {

    public TwoSlotAdapterProperties(BlockPos pos, Direction direction) {
        super(pos, direction);


        ConnectingElectricalNode N = new ConnectingElectricalNode( position, 0, getRotation(direction).get(0),NodeType.NEUTRAL);
        ConnectingElectricalNode L1 = new ConnectingElectricalNode(position, 1, getRotation(direction).get(1),NodeType.PHASE_1);

        nodes.add(N);
        nodes.add(L1);



    }

    @Override
    public boolean cableConnectable() {
        return true;
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.TWO_SLOT_ADAPTER.get();
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        List<Vec3> positions = new ArrayList<>();

        switch (direction) {
            case DOWN -> {

                positions.add(new Vec3(11 / 16f, 12 / 16f, 8 / 16f));

                positions.add(new Vec3(5 / 16f, 12 / 16f, 8 / 16f));
            }
            case UP -> {
                positions.add(new Vec3(5 / 16f, 4 / 16f, 8 / 16f));
                positions.add(new Vec3(11 / 16f, 4 / 16f, 8 / 16f));
            }
            case NORTH -> {
                positions.add(new Vec3(5 / 16f, 8 / 16f, 12 / 16f));
                positions.add(new Vec3(11 / 16f, 8 / 16f, 12 / 16f));
            }
            case SOUTH -> {
                positions.add(new Vec3(11 / 16f, 8 / 16f, 4 / 16f));
                positions.add(new Vec3(5 / 16f, 8 / 16f, 4 / 16f));
            }
            case EAST -> {
                positions.add(new Vec3(4 / 16f, 8 / 16f, 11 / 16f));
                positions.add(new Vec3(4 / 16f, 8 / 16f, 5 / 16f));
            }
            case WEST -> {
                positions.add(new Vec3(12 / 16f, 8 / 16f, 5 / 16f));
                positions.add(new Vec3(12 / 16f, 8 / 16f, 11 / 16f));
            }
        }

        return positions;
    }
}
