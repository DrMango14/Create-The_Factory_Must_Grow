package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TransformerProperties extends DirectionalElectricalProperties {


    public TransformerProperties(BlockPos pos1, Direction direction) {
        super(pos1, direction);
		Direction dir = direction.getCounterClockWise();

        ElectricalNode A = new DirectionalElectricalNode(position, 0, dir, NodeType.INPUT);
        ElectricalNode B = new DirectionalElectricalNode(position, 1, dir, NodeType.OUTPUT);
        ElectricalNode a = new DirectionalElectricalNode(position, 2, dir.getOpposite(), NodeType.INPUT);
        ElectricalNode b = new DirectionalElectricalNode(position, 3, dir.getOpposite(), NodeType.OUTPUT);

        nodes.put(0, A);
        nodes.put(1, B);
        nodes.put(2, a);
        nodes.put(3, b);

        components.add(new Transformer(A,B,a,b,1,10,1,0, position));
    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.TRANSFORMER.get();
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        List<Vec3> positions = new ArrayList<>();

        switch (direction) {
            case UP, DOWN, EAST, WEST, NORTH, SOUTH -> {
                positions.add(new Vec3(1, 7 / 16f, 8 / 16f));
                positions.add(new Vec3(1, 7 / 16f, 13 / 16f));
                positions.add(new Vec3(0, 13 / 16f, 8 / 16f));
                positions.add(new Vec3(0, 13 / 16f, 13 / 16f));
            }
        }

        return positions;
    }
}
