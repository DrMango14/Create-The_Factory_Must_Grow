package com.drmangotea.tfmg.content.electricity.content.power_transmission.wires;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.HybridConnectedNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ConnectorProperties extends DirectionalElectricalProperties {
    public ConnectorProperties(BlockPos pos, Direction d) {
        super(pos, d);
		
		List<Direction> directions = new ArrayList<>();
		directions.add(d);
		directions.add(d.getOpposite());
		
		nodes.add(new HybridConnectedNode(position,0, directions, new Vec3(0.5d, 0.5d, 0.5d), NodeType.CONNECTOR));
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        return List.of();
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.CONNECTOR.get();
    }

    public CompoundTag saveData(CompoundTag compound) {
            return compound;
    }
}
