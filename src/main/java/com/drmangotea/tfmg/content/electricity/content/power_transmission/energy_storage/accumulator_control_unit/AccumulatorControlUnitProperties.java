package com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_control_unit;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AccumulatorControlUnitProperties extends DirectionalElectricalProperties {
    public AccumulatorControlUnitProperties(BlockPos pos, Direction direction) {
        super(pos, direction);


        List<Direction> directions = new ArrayList<>();
        directions.add(direction);
        directions.add(direction.getOpposite());

        DirectionalElectricalNode N = new DirectionalElectricalNode(position, 0, directions, NodeType.NEUTRAL);
        DirectionalElectricalNode IN = new DirectionalElectricalNode(position, 1, direction, NodeType.PHASE_1);
        DirectionalElectricalNode OUT = new DirectionalElectricalNode(position, 4, direction.getOpposite(), NodeType.PHASE_1);
        ElectricalNode Internal = new ElectricalNode(position, 5);
		
		//TODO: These node ids don't seem right...
        nodes.put(0, N);
        nodes.put(1, IN);
        nodes.put(4, OUT);
        nodes.put(5, Internal);

        components.add(new Resistance(IN,N,10,0,position));
        components.add(new Resistance(OUT,Internal,5,1,position));
        components.add(new IdealVoltageSource(Internal,N,100,9999999,0,0,position));


    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public List<Vec3> getRotation(Direction direction) {
        return List.of();
    }

    @Override
    public ElectricalPropertiesType<?> getType() {
        return TFMGElectricalProperties.ACCUMULATOR_CONTROL.get();
    }
}
