package com.drmangotea.tfmg.content.electricity.experimental.testing;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DebugSolving {
    public static Complex polar(double magnitude, double degrees) {
        double a = Math.toRadians(degrees);

        return new Complex(
                magnitude * Math.cos(a),
                magnitude * Math.sin(a)
        );
    }
    public static Solution solve(int nodeCount, ArrayList<Resistor> resistors, ArrayList<VoltageSource> voltageSources) {

        SparseMatrix A = buildMatrix(nodeCount,resistors,voltageSources);

        Complex[] b = buildRHS(nodeCount,voltageSources);

        Complex[] x = SparseSolver.solve(A, b);


        Complex[] nodeVoltages = new Complex[nodeCount];

        nodeVoltages[0] = new Complex();

        for (int node = 1;
             node < nodeCount;
             node++) {

            nodeVoltages[node] =
                    x[node - 1];
        }


        Complex[] sourceCurrents =
                new Complex[
                        voltageSources.size()
                        ];


        int nodeVariables =
                nodeCount - 1;


        for (int k = 0;
             k < voltageSources.size();
             k++) {

            sourceCurrents[k] =
                    x[nodeVariables + k];
        }


        return new Solution(
                nodeVoltages,
                sourceCurrents
        );
    }

    static SparseMatrix buildMatrix(int nodeCount,ArrayList<Resistor> resistors,ArrayList<VoltageSource> voltageSources) {

        int nodeVariables = nodeCount - 1;
        int sourceVariables = voltageSources.size();

        int size =
                nodeVariables + sourceVariables;

        SparseMatrix A =
                new SparseMatrix(size);


        // ----------------------------------------------------
        // Resistors
        // ----------------------------------------------------

        for (Resistor r : resistors) {

            double conductance =
                    1.0 / r.resistance;

            Complex g =
                    new Complex(conductance, 0);

            /*
             *
             *       a              b
             *       o----R---------o
             *
             *       +G             +G
             *       -G             -G
             */

            if (r.nodeA != 0) {

                int a = r.nodeA - 1;

                A.add(a, a, g);
            }

            if (r.nodeB != 0) {

                int b = r.nodeB - 1;

                A.add(b, b, g);
            }

            if (r.nodeA != 0 &&
                    r.nodeB != 0) {

                int a = r.nodeA - 1;
                int b = r.nodeB - 1;

                A.add(a, b, g.neg());
                A.add(b, a, g.neg());
            }
        }


        // ----------------------------------------------------
        // Voltage sources
        // ----------------------------------------------------

        for (int k = 0;
             k < voltageSources.size();
             k++) {

            VoltageSource vs =
                    voltageSources.get(k);

            int sourceIndex =
                    nodeVariables + k;

            vs.matrixIndex =
                    sourceIndex;


            /*
             * Current through voltage source is an
             * additional MNA unknown.
             *
             *        + I
             *        |
             *        a
             *
             *        |
             *        source
             *        |
             *        b
             */


            if (vs.positive != 0) {

                int p =
                        vs.positive - 1;

                A.add(
                        p,
                        sourceIndex,
                        new Complex(1, 0)
                );

                A.add(
                        sourceIndex,
                        p,
                        new Complex(1, 0)
                );
            }


            if (vs.negative != 0) {

                int n =
                        vs.negative - 1;

                A.add(
                        n,
                        sourceIndex,
                        new Complex(-1, 0)
                );

                A.add(
                        sourceIndex,
                        n,
                        new Complex(-1, 0)
                );
            }
        }

        return A;
    }


    // ========================================================
    // BUILD RHS
    // ========================================================

    static Complex[] buildRHS(int nodeCount,ArrayList<VoltageSource> voltageSources) {

        int nodeVariables =
                nodeCount - 1;

        int size =
                nodeVariables +
                        voltageSources.size();

        Complex[] b =
                new Complex[size];

        Arrays.fill(
                b,
                new Complex()
        );


        for (int k = 0;
             k < voltageSources.size();
             k++) {

            VoltageSource vs =
                    voltageSources.get(k);

            b[nodeVariables + k] =
                    new Complex(
                            vs.voltage.re,
                            vs.voltage.im
                    );
        }

        return b;
    }


    // ========================================================
    // RESISTOR CURRENT
    // ========================================================

    Complex resistorCurrent(
            Resistor r,
            Solution solution
    ) {

        Complex va =
                solution.voltage(r.nodeA);

        Complex vb =
                solution.voltage(r.nodeB);

        return va
                .sub(vb)
                .div(
                        new Complex(
                                r.resistance,
                                0
                        )
                );
    }


    double resistorPower(
            Resistor r,
            Solution solution
    ) {

        Complex i =
                resistorCurrent(
                        r,
                        solution
                );

        double magnitude =
                i.abs();

        return magnitude *
                magnitude *
                r.resistance;
    }


    // ========================================================
    // SOLUTION
    // ========================================================

    static final class Solution {

        final Complex[] nodeVoltages;
        final Complex[] sourceCurrents;


        Solution(
                Complex[] nodeVoltages,
                Complex[] sourceCurrents
        ) {
            this.nodeVoltages =
                    nodeVoltages;

            this.sourceCurrents =
                    sourceCurrents;
        }


        Complex voltage(int node) {

            if (node == 0)
                return new Complex();

            return nodeVoltages[node];
        }
    }
}


// ============================================================
// DEMO
// ============================================================

 class SparseSolver {

    /**
     * Sparse Gaussian elimination.
     * <p>
     * This implementation modifies A and b.
     */
    static Complex[] solve(
            SparseMatrix A,
            Complex[] b
    ) {

        int n = b.length;


        // ----------------------------------------------------
        // Forward elimination
        // ----------------------------------------------------

        for (int k = 0; k < n; k++) {

            // Find pivot
            int pivot = k;
            double best =
                    A.get(k, k).abs();


            for (int i = k + 1; i < n; i++) {

                double value =
                        A.get(i, k).abs();

                if (value > best) {
                    best = value;
                    pivot = i;
                }
            }


            if (best < 1e-12) {

                throw new IllegalStateException(
                        "Singular circuit matrix at " +
                                "column " + k
                );
            }


            // Swap rows
            if (pivot != k) {

                SparseRow temp =
                        A.rows[k];

                A.rows[k] =
                        A.rows[pivot];

                A.rows[pivot] =
                        temp;


                Complex tempB =
                        b[k];

                b[k] =
                        b[pivot];

                b[pivot] =
                        tempB;
            }


            Complex pivotValue =
                    A.get(k, k);


            // ------------------------------------------------
            // Eliminate rows below pivot
            // ------------------------------------------------

            for (int i = k + 1; i < n; i++) {

                Complex aik =
                        A.get(i, k);


                if (aik.abs() < 1e-15)
                    continue;


                Complex factor =
                        aik.div(pivotValue);


                /*
                 * We only need to modify columns that
                 * actually exist in the pivot row.
                 */
                for (
                        Map.Entry<Integer, Complex> entry :
                        A.rows[k].values.entrySet()
                ) {

                    int j =
                            entry.getKey();

                    if (j < k)
                        continue;


                    Complex pivotElement =
                            entry.getValue();


                    Complex change =
                            factor.mul(
                                    pivotElement
                            );


                    A.add(
                            i,
                            j,
                            change.neg()
                    );
                }


                b[i] =
                        b[i].sub(
                                factor.mul(b[k])
                        );


                /*
                 * Explicitly remove the element
                 * below the pivot.
                 */
                A.rows[i].values.remove(k);
            }
        }


        // ----------------------------------------------------
        // Back substitution
        // ----------------------------------------------------

        Complex[] x =
                new Complex[n];


        for (int i = n - 1; i >= 0; i--) {

            Complex sum =
                    new Complex();


            for (
                    Map.Entry<Integer, Complex> entry :
                    A.rows[i].values.entrySet()
            ) {

                int j =
                        entry.getKey();

                if (j <= i)
                    continue;


                sum =
                        sum.add(
                                entry.getValue()
                                        .mul(x[j])
                        );
            }


            Complex diagonal =
                    A.get(i, i);


            x[i] =
                    b[i]
                            .sub(sum)
                            .div(diagonal);
        }


        return x;
    }
}
