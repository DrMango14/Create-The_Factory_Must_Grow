package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.content.electricity.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ElectricalProperties {

    public final Map<Integer, ElectricalNode> nodes = new HashMap<>();
    public final List<ElectricalComponent> components = new ArrayList<>();
    public BlockPos position;


    public ElectricalProperties(BlockPos pos) {
      this.position = pos;
    }

    public abstract ElectricalPropertiesType<?> getType();

    public boolean hasElectricityPort(Direction facing){
        return true;
    }

    public boolean cableConnectable(){
        return false;
    }

    public boolean needsUpdateData(){
        return false;
	}

    public List<Direction> getConnectableDirections(){
        return new ArrayList<>();
    }


}
