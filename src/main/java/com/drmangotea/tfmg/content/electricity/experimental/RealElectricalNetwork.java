package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealElectricalNetwork {


    public static final double FREQUENCY = 50.0;
    public static final double OMEGA = 2.0 * Math.PI * FREQUENCY;

    public List<ElectricalNode> nodes = new ArrayList<>();
    public List<Resistance> resistors = new ArrayList<>();
    private final List<Capacitance> capacitors = new ArrayList<>();
    private final List<Inductance> inductors = new ArrayList<>();
    private List<IdealVoltageSource> sources = new ArrayList<>();


    public long id;
    public int totalNodes = 150;
    public Map<Long, ElectricalProperties> members = new HashMap<>();
    public List<WireConnection> connections = new ArrayList<>();

    public LevelAccessor world;

    public RealElectricalNetwork(LevelAccessor world) {
        this.world = world;
    }


    public Map<Integer, ComplexValue> nodeVoltages = new HashMap<>();

    public List<ElectricalNode> getNodes(long pos) {
        List<ElectricalNode> nodes = new ArrayList<>();

        this.members.forEach((l, p) -> {
            if (l == pos) {
                nodes.addAll(p.nodes);
            }
        });

        return nodes;
    }


    public void addMember(BlockPos pos, ElectricalProperties properties) {
        members.put(pos.asLong(), properties);
        ;
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent, parent[i]);
    }

    private void union(int[] parent, int i, int j) {
        int rootI = find(parent, i);
        int rootJ = find(parent, j);
        if (rootI != rootJ) {
            if (rootI < rootJ) parent[rootJ] = rootI;
            else parent[rootI] = rootJ;
        }
    }

    private void stampAdmittance(ComplexValue[][] A, int[] nodeToMatrixIndex, int nodeA, int nodeB, ComplexValue Y) {
        int idxA = nodeToMatrixIndex[nodeA];
        int idxB = nodeToMatrixIndex[nodeB];

        if (idxA != -1) A[idxA][idxA] = A[idxA][idxA].plus(Y);
        if (idxB != -1) A[idxB][idxB] = A[idxB][idxB].plus(Y);
        if (idxA != -1 && idxB != -1) {
            A[idxA][idxB] = A[idxA][idxB].minus(Y);
            A[idxB][idxA] = A[idxB][idxA].minus(Y);
        }
    }

    public void setVoltageGen(IRealisticElectric be, int voltage) {
        members.forEach((m, p) -> {

            if (m == be.getPos()) {
                for (ElectricalComponent component : p.components) {
                    if (component instanceof IdealVoltageSource voltageSource) {
                        voltageSource.amplitude = voltage;
                    }
                }
            }
        });
        update();
    }

    public void setResistance(IRealisticElectric be, int id, int resistance) {
        members.forEach((m, p) -> {
            if (m == be.getPos()) {
                for (ElectricalComponent component : p.components) {
                    if (component instanceof Resistance resistor && resistor.localId == id) {
                        resistor.resistance = resistance;
                    }
                }
            }
        });
        update();
    }


    public void addComponents() {


        nodes = new ArrayList<>();
        resistors = new ArrayList<>();
        sources = new ArrayList<>();
        List<ElectricalComponent> components = new ArrayList<>();
        members.forEach((l, p) -> {


            nodes.addAll(p.nodes);
            components.addAll(p.components);

        });

        for (int i = 0; i < nodes.size(); i++) {
            ElectricalNode node = nodes.get(i);

            node.networkId = i;

        }

        components.forEach(c -> {
            if (c instanceof Resistance r) {
                resistors.add(r);
            }
            if (c instanceof IdealVoltageSource v) {
                sources.add(v);
            }
        });
       // totalNodes = 20;


    }

    public void addConnections() {
        connections.forEach(c -> {

            ElectricalNode node1 = null;
            ElectricalNode node2 = null;

            for (ElectricalNode node : nodes) {


                if (node.getNetworkId() == c.node1().getNetworkId()) {
                    node1 = node;
                }
                if (node.getNetworkId() == c.node2().getNetworkId()) {
                    node2 = node;
                }
            }

            if (node1 == null || node2 == null) {
                return;
            }


            resistors.add(new Resistance(node1, node2, c.resistance(), 0));


        });
    }

    public void update() {

        addComponents();
        addConnections();
        solve();


    }


    public void solve() {

        if (totalNodes == 0)
            return;

        int[] parent = new int[totalNodes];
        for (int i = 0; i < totalNodes; i++) parent[i] = i;

        for (Resistance r : resistors) union(parent, r.nodeA.getNetworkId(), r.nodeB.getNetworkId());
        for (Capacitance c : capacitors) union(parent, c.nodeA, c.nodeB);
        for (Inductance l : inductors) union(parent, l.nodeA, l.nodeB);
        for (IdealVoltageSource v : sources) union(parent, v.nodeA.getNetworkId(), v.nodeB.getNetworkId());

        boolean[] isLocalGround = new boolean[totalNodes];
        isLocalGround[0] = true;

        for (int i = 1; i < totalNodes; i++) {
            if (find(parent, i) == i) {
                isLocalGround[i] = true;
            }
        }

        int[] nodeToMatrixIndex = new int[totalNodes];
        int matrixNodeCount = 0;
        for (int i = 0; i < totalNodes; i++) {
            nodeToMatrixIndex[i] = isLocalGround[i] ? -1 : matrixNodeCount++;
        }

        int matrixSize = matrixNodeCount + sources.size();
        ComplexValue[][] A = new ComplexValue[matrixSize][matrixSize];
        ComplexValue[] z = new ComplexValue[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            z[i] = ComplexValue.ZERO;
            for (int j = 0; j < matrixSize; j++) {
                A[i][j] = ComplexValue.ZERO;
            }
        }


        for (Resistance r : resistors)
            stampAdmittance(A, nodeToMatrixIndex, r.nodeA.getNetworkId(), r.nodeB.getNetworkId(), r.getAdmittance());
        for (Capacitance c : capacitors) stampAdmittance(A, nodeToMatrixIndex, c.nodeA, c.nodeB, c.getAdmittance());
        for (Inductance l : inductors) stampAdmittance(A, nodeToMatrixIndex, l.nodeA, l.nodeB, l.getAdmittance());


        for (int i = 0; i < sources.size(); i++) {
            IdealVoltageSource v = sources.get(i);
            int idxPos = nodeToMatrixIndex[v.nodeA.getNetworkId()];
            int idxNeg = nodeToMatrixIndex[v.nodeB.getNetworkId()];
            int matrixRow = matrixNodeCount + i;

            if (idxPos != -1) {
                A[idxPos][matrixRow] = A[idxPos][matrixRow].plus(ComplexValue.ONE);
                A[matrixRow][idxPos] = A[matrixRow][idxPos].plus(ComplexValue.ONE);
            }
            if (idxNeg != -1) {
                A[idxNeg][matrixRow] = A[idxNeg][matrixRow].minus(ComplexValue.ONE);
                A[matrixRow][idxNeg] = A[matrixRow][idxNeg].minus(ComplexValue.ONE);
            }
            z[matrixRow] = v.getPhasor();
        }

        ComplexValue[] x = luSolveComplex(A, z);

        ComplexValue[] finalVoltages = new ComplexValue[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            int matrixIdx = nodeToMatrixIndex[i];
            finalVoltages[i] = (matrixIdx == -1) ? ComplexValue.ZERO : x[matrixIdx];
        }


        for (int i = 0; i < totalNodes; i++) {
            int localGroundAnchor = find(parent, i);
            ComplexValue relativeVoltage = finalVoltages[i].minus(finalVoltages[localGroundAnchor]);
            // TFMG.LOGGER.debug("Node " + i + " has voltage " + relativeVoltage);
        }

        for (int i = 0; i < totalNodes; i++) {
            int localGroundAnchor = find(parent, i);
            ComplexValue voltage = finalVoltages[i].minus(finalVoltages[localGroundAnchor]);
            nodeVoltages.put(i, voltage);
        }

        for (int i = 0; i < resistors.size(); i++) {
            Resistance resistor = resistors.get(i);
            int localGroundAnchor = find(parent, resistor.nodeA.getNetworkId());
            ComplexValue relativeVoltage1 = finalVoltages[resistor.nodeA.getNetworkId()].minus(finalVoltages[localGroundAnchor]);
            ComplexValue relativeVoltage2 = finalVoltages[resistor.nodeB.getNetworkId()].minus(finalVoltages[localGroundAnchor]);


        }
    }

    private ComplexValue[] luSolveComplex(ComplexValue[][] A, ComplexValue[] b) {
        int n = b.length;
        ComplexValue[][] LU = new ComplexValue[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(A[i], 0, LU[i], 0, n);
        int[] pivot = new int[n];
        for (int i = 0; i < n; i++) pivot[i] = i;

        for (int j = 0; j < n; j++) {
            int maxRow = j;
            double maxVal = LU[j][j].abs();
            for (int i = j + 1; i < n; i++) {
                if (LU[i][j].abs() > maxVal) {
                    maxVal = LU[i][j].abs();
                    maxRow = i;
                }
            }
            if (maxRow != j) {
                ComplexValue[] tempRow = LU[j];
                LU[j] = LU[maxRow];
                LU[maxRow] = tempRow;
                int tempP = pivot[j];
                pivot[j] = pivot[maxRow];
                pivot[maxRow] = tempP;
            }
            for (int i = j + 1; i < n; i++) {
                LU[i][j] = LU[i][j].div(LU[j][j]);
                for (int k = j + 1; k < n; k++) {
                    LU[i][k] = LU[i][k].minus(LU[i][j].times(LU[j][k]));
                }
            }
        }
        ComplexValue[] y = new ComplexValue[n];
        for (int i = 0; i < n; i++) {
            ComplexValue forwardSum = ComplexValue.ZERO;
            for (int j = 0; j < i; j++) forwardSum = forwardSum.plus(LU[i][j].times(y[j]));
            y[i] = b[pivot[i]].minus(forwardSum);
        }
        ComplexValue[] x = new ComplexValue[n];
        for (int i = n - 1; i >= 0; i--) {
            ComplexValue backwardSum = ComplexValue.ZERO;
            for (int j = i + 1; j < n; j++) backwardSum = backwardSum.plus(LU[i][j].times(x[j]));
            x[i] = (y[i].minus(backwardSum)).div(LU[i][i]);
        }
        return x;
    }
}
