package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import static com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork.OMEGA;

public class Capacitance {
    public int nodeA;
    public int nodeB;
    double capacitance; // Farads

    public Capacitance(int nodeA, int nodeB, double capacitance) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.capacitance = capacitance;
    }

    public ComplexValue getAdmittance() {
        return new ComplexValue(0.0, OMEGA * capacitance);
    }
}