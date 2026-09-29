package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.MergedNode;
import net.minecraft.world.level.Level;

public class Resistance extends ElectricalComponent {

    public double resistance;

    public final int localId;

    public final long pos;

    public Resistance(ElectricalNode nodeA, ElectricalNode nodeB, double resistance, int localId, long pos) {
        super(nodeA, nodeB);
        this.resistance = resistance;
        this.localId = localId;
        this.pos = pos;
    }

    public ComplexValue getVoltagePhasor(Level level) {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        ElectricalNode node1 = nodeA;
        ElectricalNode node2 = nodeB;

        boolean isNode1Merged = !network.nodeVoltages.containsKey(node1.getNetworkId());
        boolean isNode2Merged = !network.nodeVoltages.containsKey(node2.getNetworkId());

        if(isNode1Merged){
            if(node1 instanceof MergedNode m1){

            }
        }

        // if(!network.nodeVoltages.containsKey(nodeA.getNetworkId())||!network.nodeVoltages.containsKey(nodeB.getNetworkId()))
        //     return new ComplexValue(0,0);
        if ( node1 == null||node2 == null||network.nodeVoltages.get(node1.getNetworkId()) == null || network.nodeVoltages.get(node2.getNetworkId()) == null) {
            return new ComplexValue(0, 0);
        }
        return network.nodeVoltages.get(node1.getNetworkId()).minus(network.nodeVoltages.get(node2.getNetworkId()));
    }

    public double getVoltage(Level level) {
        return getVoltagePhasor(level).abs();
    }

    public ComplexValue getAdmittance() {
        return new ComplexValue(1.0 / resistance, 0.0);
    }

}
