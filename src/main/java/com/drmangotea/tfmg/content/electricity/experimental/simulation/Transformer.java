package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.MergedNode;
import net.minecraft.world.level.Level;

import java.util.List;

public class Transformer extends ElectricalComponent {
    public ElectricalNode pA, pB, sA, sB;
    public int id;
    public double L1, turnsRatio, k;

    public final int localId;

    public final long pos;


    public Transformer(ElectricalNode pA, ElectricalNode pB, ElectricalNode sA, ElectricalNode sB, double L1, double turnsRatio, double k, int idx,long pos) {
        super(pA, pB);
        this.pA = pA;
        this.pB = pB;
        this.sA = sA;
        this.sB = sB;
        this.L1 = L1;
        this.turnsRatio = turnsRatio;
        this.k = k;
        this.id = idx;
        this.localId = idx;
        this.pos = pos;
    }


    @Override
    public List<ElectricalNode> getConnectedNodes() { return List.of(pA, pB, sA, sB); }
    @Override
    public int getExtraRows() { return 1; }
    public void setIndex(int idx) { this.id = idx; }
    @Override
    public void stamp(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset) {
        int idxPP = pA.networkId;
        int idxPN = pB.networkId;
        int idxSP = sA.networkId;
        int idxSN = sB.networkId;


        int row = extraRowOffset + id;

        ComplexValue one = new ComplexValue(1.0, 0.0);
        ComplexValue n = new ComplexValue(turnsRatio, 0.0);

        //voltage
        if (idxPP != 0) G[row][idxPP] = G[row][idxPP].plus(one);
        if (idxPN != 0) G[row][idxPN] = G[row][idxPN].minus(one);
        if (idxSP != 0) G[row][idxSP] = G[row][idxSP].minus(n);
        if (idxSN != 0) G[row][idxSN] = G[row][idxSN].plus(n);


        //primary current
        if (idxPP != 0) G[idxPP][row] = G[idxPP][row].plus(one);
        if (idxPN != 0) G[idxPN][row] = G[idxPN][row].minus(one);

        //secondary current
        if (idxSP != 0) G[idxSP][row] = G[idxSP][row].minus(n);
        if (idxSN != 0) G[idxSN][row] = G[idxSN][row].plus(n);

        //transformer's admittance
        double omega = 2 * Math.PI * 50;
        ComplexValue coreAdmittance = new ComplexValue(0.0, -1.0 / (omega * 2.0*1000));
        if (idxPP != 0) G[idxPP][idxPP] = G[idxPP][idxPP].plus(coreAdmittance);
        if (idxPN != 0) G[idxPN][idxPN] = G[idxPN][idxPN].plus(coreAdmittance);
    }
    /*
    public void stamp2(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset) {
        int idxPP = pA.networkId;
        int idxPN = pB.networkId;
        int idxSP = sA.networkId;
        int idxSN = sB.networkId;

        int row1 = extraRowOffset + id;
        int row2 = extraRowOffset + id + 1;
        ComplexValue one = new ComplexValue(1.0, 0.0);
        ComplexValue n = new ComplexValue(turnsRatio, 0.0);


        if (idxPP != 0) G[row1][idxPP] = G[row1][idxPP].plus(one);
        if (idxPN != 0) G[row1][idxPN] = G[row1][idxPN].minus(one);
        if (idxSP != 0) G[row1][idxSP] = G[row1][idxSP].minus(n);
        if (idxSN != 0) G[row1][idxSN] = G[row1][idxSN].plus(n);

        G[row2][row1] = G[row2][row1].plus(one);
        G[row2][row2] = G[row2][row2].plus(n);

        if (idxPP != 0) G[idxPP][row1] = G[idxPP][row1].plus(one);
        if (idxPN != 0) G[idxPN][row1] = G[idxPN][row1].minus(one);
        if (idxSP != 0) G[idxSP][row2] = G[idxSP][row2].plus(one);
        if (idxSN != 0) G[idxSN][row2] = G[idxSN][row2].minus(one);


        double omega = 2 * Math.PI * 50;
        ComplexValue coreAdmittance = new ComplexValue(0.0, -1.0 / (omega * 2.0));



        if (idxPP != 0) G[idxPP][idxPP] = G[idxPP][idxPP].plus(coreAdmittance);
        if (idxPN != 0) G[idxPN][idxPN] = G[idxPN][idxPN].plus(coreAdmittance);
    }
    public ComplexValue getPrimaryVoltagePhasor(Level level) {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        ElectricalNode node1 = nodeA;
        ElectricalNode node2 = nodeB;


        // if(!network.nodeVoltages.containsKey(nodeA.getNetworkId())||!network.nodeVoltages.containsKey(nodeB.getNetworkId()))
        //     return new ComplexValue(0,0);
        if (node1 == null || node2 == null || network.nodeVoltages.get(node1.getNetworkId()) == null || network.nodeVoltages.get(node2.getNetworkId()) == null) {
            return new ComplexValue(0, 0);
        }
        return network.nodeVoltages.get(node1.getNetworkId()).minus(network.nodeVoltages.get(node2.getNetworkId()));
    }

     */
}