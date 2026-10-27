package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_control_unit;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.NodeType;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class AccumulatorControlUnitProperties extends DirectionalElectricalProperties {
    public AccumulatorControlUnitProperties(long pos, Direction direction) {
        super(pos, direction);


        List<Direction> directions = new ArrayList<>();
        directions.add(direction);
        directions.add(direction.getOpposite());

        DirectionalElectricalNode N = new DirectionalElectricalNode(position, 0, directions, NodeType.NEUTRAL);
        DirectionalElectricalNode IN = new DirectionalElectricalNode(position, 1, direction, NodeType.PHASE_1);
        DirectionalElectricalNode OUT = new DirectionalElectricalNode(position, 4, direction.getOpposite(), NodeType.PHASE_1);
        ElectricalNode Internal = new ElectricalNode(position, 5);


        nodes.add(N);
        nodes.add(IN);
        nodes.add(OUT);
        nodes.add(Internal);

        components.add(new Resistance(IN,N,10,0,position));
        components.add(new Resistance(OUT,Internal,5,1,position));
        components.add(new IdealVoltageSource(Internal,N,100,9999999,0,0,position));


    }

    @Override
    public boolean needsUpdateData() {
        return true;
    }

    @Override
    public List<CablePos> getRotation(Direction direction) {
        return List.of();
    }

    @Override
    public int getId() {
        return 13;
    }
}
