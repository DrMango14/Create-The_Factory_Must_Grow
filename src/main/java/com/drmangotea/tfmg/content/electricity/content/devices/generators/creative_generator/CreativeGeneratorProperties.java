package com.drmangotea.tfmg.content.electricity.content.devices.generators.creative_generator;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.HybridConnectedNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CreativeGeneratorProperties extends ElectricalProperties {
    public CreativeGeneratorProperties(BlockPos pos) {
        super(pos);

        ElectricalNode N = new HybridConnectedNode(position, 0, List.of(Direction.values()),getRotation(0 ),NodeType.NEUTRAL);
        ElectricalNode L1 = new HybridConnectedNode(position, 1, List.of(Direction.values()),getRotation(1), NodeType.PHASE_1);
        ElectricalNode L2 = new HybridConnectedNode(position, 2, List.of(Direction.values()),getRotation(2),NodeType.PHASE_2);
        ElectricalNode L3 = new HybridConnectedNode(position, 3, List.of(Direction.values()),getRotation(3),NodeType.PHASE_3);


        nodes.put(0, N);
        nodes.put(1, L1);
        nodes.put(2, L2);
        nodes.put(3, L3);
        components.add(new IdealVoltageSource(L1, N, 100, 99999, 0, 0, position));
        components.add(new IdealVoltageSource(L2, N, 100, 99999, 120, 1, position));
        components.add(new IdealVoltageSource(L3, N, 100, 99999, 240, 2, position));


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
        return TFMGElectricalProperties.CREATIVE_GENERATOR.get();
    }

    @Override
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
    }

    public Vec3 getRotation(int i) {
		return switch (i) {
			case 0 -> new Vec3(1,13/16f,0);
			case 1 -> new Vec3(1,1,1);
			case 2 -> new Vec3(0,1,1);
			case 3 -> new Vec3(0,1,0);
			default -> new Vec3(0,0,0);
		};
    }
}
