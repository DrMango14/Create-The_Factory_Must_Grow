package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import net.minecraft.world.level.Level;

public class Resistance extends ElectricalComponent{

    public double resistance;

    public final int localId;

    public Resistance(ElectricalNode nodeA, ElectricalNode nodeB, double resistance,int localId) {
        super(nodeA,nodeB);
        this.resistance = resistance;
        this.localId = localId;
    }

    public ComplexValue getVoltagePhasor(Level level) {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        if(!network.nodeVoltages.containsKey(nodeA.getNetworkId())||!network.nodeVoltages.containsKey(nodeB.getNetworkId()))
            return new ComplexValue(0,0);

        return network.nodeVoltages.get(nodeA.getNetworkId()).minus(network.nodeVoltages.get(nodeB.getNetworkId()));
    }

    public double getVoltage(Level level){
        return getVoltagePhasor(level).abs();
    }

    public ComplexValue getAdmittance() {
        return new ComplexValue(1.0 / resistance, 0.0);
    }

}
