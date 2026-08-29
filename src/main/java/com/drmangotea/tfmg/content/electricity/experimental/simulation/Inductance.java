package com.drmangotea.tfmg.content.electricity.experimental.simulation;

import static com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork.OMEGA;

public class Inductance {
        public int nodeA;
    public int nodeB;
        double inductance; // Henries

        public Inductance(int nodeA, int nodeB, double inductance) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.inductance = inductance;
        }

        public ComplexValue getAdmittance() {
            return new ComplexValue(0.0, -1.0 / (OMEGA * inductance));
        }
    }