package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;

import java.util.List;

public class IdealVoltageSource extends ElectricalComponent {
    public double amplitude;
    public double phaseOffset;
    public double power;
    public int id;
    public int networkId=0;

    public IdealVoltageSource(ElectricalNode nodePos, ElectricalNode nodeNeg, double amplitude, int power, double phaseOffsetDeg, int id) {
        super(nodePos, nodeNeg);
        this.amplitude = amplitude;
        this.power = power;
        this.phaseOffset = phaseOffsetDeg;
        this.id = id;
    }

    @Override
    public List<ElectricalNode> getConnectedNodes() { return List.of(nodeA, nodeB); }
    @Override
    public int getExtraRows() { return 1; }
    public void setIndex(int idx) { this.id = idx; }

    @Override
    public void stamp(ComplexValue[][] G, ComplexValue[] I, int extraRowOffset) {
        int idxPos = nodeA.networkId;
        int idxNeg = nodeB.networkId;
        int currentBranchRow = extraRowOffset + id;
        ComplexValue one = new ComplexValue(1.0, 0.0);

        if (idxPos != 0) { G[currentBranchRow][idxPos] = G[currentBranchRow][idxPos].plus(one); G[idxPos][currentBranchRow] = G[idxPos][currentBranchRow].plus(one); }
        if (idxNeg != 0) { G[currentBranchRow][idxNeg] = G[currentBranchRow][idxNeg].minus(one); G[idxNeg][currentBranchRow] = G[idxNeg][currentBranchRow].minus(one); }

        double rad = Math.toRadians(phaseOffset);
        I[currentBranchRow] = I[currentBranchRow].plus(new ComplexValue(amplitude * Math.cos(rad), amplitude * Math.sin(rad)));


        ComplexValue gmin = new ComplexValue(1e-12, 0.0);
        if (idxPos != 0) G[idxPos][idxPos] = G[idxPos][idxPos].plus(gmin);
        if (idxNeg != 0) G[idxNeg][idxNeg] = G[idxNeg][idxNeg].plus(gmin);
    }
    public ComplexValue getPhasor() {
        double radians = Math.toRadians(phaseOffset);
        return new ComplexValue(amplitude * Math.cos(radians), amplitude * Math.sin(radians));
    }

}
