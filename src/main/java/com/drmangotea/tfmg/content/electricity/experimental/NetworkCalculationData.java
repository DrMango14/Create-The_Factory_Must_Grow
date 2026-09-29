package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.ComplexValue;

public class NetworkCalculationData {

    int[] parent;
    int[] nodeToMatrixIndex;
    ComplexValue[][] A;
    ComplexValue[] z;

    boolean firstIteration = true;
    int iterationsLeft;

    int[] pivot;
    ComplexValue[][] LU;

    ComplexValue[] x;

    public NetworkCalculationData(int[] parent, int[] nodeToMatrixIndex, ComplexValue[][] A, ComplexValue[] z) {
        this.parent = parent;
        this.nodeToMatrixIndex = nodeToMatrixIndex;
        this.A = A;
        this.z = z;
        iterationsLeft = z.length;
    }
}
