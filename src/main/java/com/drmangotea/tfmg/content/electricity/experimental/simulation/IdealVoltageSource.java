package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;

public class IdealVoltageSource extends ElectricalComponent {
    public double amplitude;
    public double phaseOffset;
    public double power;
    public int id;

    public IdealVoltageSource(ElectricalNode nodePos, ElectricalNode nodeNeg, double amplitude, int power, double phaseOffsetDeg, int id) {
        super(nodePos, nodeNeg);
        this.amplitude = amplitude;
        this.power = power;
        this.phaseOffset = phaseOffsetDeg;
        this.id = id;
    }

    public ComplexValue getPhasor() {
        double radians = Math.toRadians(phaseOffset);
        return new ComplexValue(amplitude * Math.cos(radians), amplitude * Math.sin(radians));
    }
}
