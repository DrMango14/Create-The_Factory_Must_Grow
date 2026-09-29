package com.drmangotea.tfmg.content.electricity.experimental.testing;

 public class VoltageSource {
        final Complex voltage;
        final int positive;
        final int negative;

        int matrixIndex;

        public VoltageSource(
                Complex voltage,
                int positive,
                int negative
        ) {
            this.voltage = voltage;
            this.positive = positive;
            this.negative = negative;
        }
    }