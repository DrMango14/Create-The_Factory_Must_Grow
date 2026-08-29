package com.drmangotea.tfmg.content.electricity.experimental.simulation;

public class IdealVoltageSource extends ElectricalComponent {
    public double amplitude;
    public double phaseDegrees;
    public int id;

    public IdealVoltageSource(ElectricalNode nodePos, ElectricalNode nodeNeg, double amplitude, double phaseOffsetDeg, int id) {
        super(nodePos, nodeNeg);
        this.amplitude = amplitude;
        this.phaseDegrees = phaseOffsetDeg;
        this.id = id;
    }

    public ComplexValue getPhasor() {
        double radians = Math.toRadians(phaseDegrees);
        return new ComplexValue(amplitude * Math.cos(radians), amplitude * Math.sin(radians));
    }
}
