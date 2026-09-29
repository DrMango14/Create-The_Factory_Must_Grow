package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;


public class ElectricalProperties {

    public final List<ElectricalNode> nodes = new ArrayList<>();
    public final List<ElectricalComponent> components = new ArrayList<>();
    public long position;


    public ElectricalProperties(long pos) {
      this.position = pos;
    }

    public int getId(){
        return 1;
    }


    public boolean hasElectricityPort(Direction facing){
        return true;
    }

    public boolean cableConnectable(){
        return false;
    };

    public boolean needsUpdateData(){
        return false;
    };

    public List<ElectricalComponent> createProperties(IRealisticElectric be) {
        return new ArrayList<>();
    }



    public static int getFreeId(Level level, int base) {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        if (network.nodes.isEmpty())
            return base;

        return network.nodes.getLast().getNetworkId() + 1+base;
    }

}
