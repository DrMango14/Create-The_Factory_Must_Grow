package com.drmangotea.tfmg.content.electricity.experimental.testing;

public class Resistor {
        final double resistance;
        final int nodeA;
        final int nodeB;

        public Resistor(
                double resistance,
                int nodeA,
                int nodeB
        ) {
            if (resistance <= 0)
                throw new IllegalArgumentException(
                        "Resistance must be > 0"
                );

            this.resistance = resistance;
            this.nodeA = nodeA;
            this.nodeB = nodeB;
        }
    }