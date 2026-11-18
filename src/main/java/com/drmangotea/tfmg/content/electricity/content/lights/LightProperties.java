package com.drmangotea.tfmg.content.electricity.content.lights;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.List;

public class LightProperties extends ElectricalProperties {
    public LightProperties(BlockPos pos, Direction direction) {
        super(pos);
        ElectricalNode N = new DirectionalElectricalNode(position, 0,direction, NodeType.NEUTRAL);
        ElectricalNode L1 = new DirectionalElectricalNode(position, 1,direction, NodeType.PHASE_1);
        nodes.add(N);
        nodes.add(L1);
        components.add(new Resistance(N,L1,400,0,position));
    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.LIGHT.get();
    }

    @Override
    public boolean cableConnectable() {
        return true;
    }

    @Override
    public List<Direction> getConnectableDirections() {
        return List.of(Direction.values());
    }
}
