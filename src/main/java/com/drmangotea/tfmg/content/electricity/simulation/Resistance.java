package com.drmangotea.tfmg.content.electricity.simulation;

import com.drmangotea.tfmg.content.electricity.ElectricalNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class Resistance extends ElectricalComponent {

    public double resistance;
    public final int localId;
    public final BlockPos pos;

    public Resistance(ElectricalNode nodeA, ElectricalNode nodeB, double resistance, int localId, BlockPos pos) {
        super(nodeA, nodeB);
        this.resistance = resistance;
        this.localId = localId;
        this.pos = pos;
    }

    public ComplexValue getVoltagePhasor(Level level) {
        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);
		
		ComplexValue cvA = network.nodeVoltages.get(nodeA.getNetworkId());
		ComplexValue cvB = network.nodeVoltages.get(nodeB.getNetworkId());
		
        if (cvA == null || cvB == null) {
            return new ComplexValue(0, 0);
        }
        return cvA.minus(cvB);
    }

    @Override
    public List<ElectricalNode> getConnectedNodes() { return List.of(nodeA, nodeB); }

    @Override
    public void stamp(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset) {
        int idxA = nodeA.networkId;
        int idxB = nodeB.networkId;
        ComplexValue conductance = new ComplexValue(1.0 / resistance, 0.0);

        if (idxA != 0) G[idxA][idxA] = G[idxA][idxA].plus(conductance);
        if (idxB != 0) G[idxB][idxB] = G[idxB][idxB].plus(conductance);
        if (idxA != 0 && idxB != 0) {
            G[idxA][idxB] = G[idxA][idxB].minus(conductance);
            G[idxB][idxA] = G[idxB][idxA].minus(conductance);
        }
    }

    public double getVoltage(Level level) {
        return getVoltageComplex(level).abs();
    }
    public ComplexValue getVoltageComplex(Level level) {
        return getVoltagePhasor(level);
    }

    public ComplexValue getAdmittance() {
        return new ComplexValue(1.0 / resistance, 0.0);
    }

}
