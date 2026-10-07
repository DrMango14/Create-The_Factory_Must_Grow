package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.*;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.MergedNode;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;

public class RealElectricalNetwork {


    public static final double FREQUENCY = 50.0;
    public static final double OMEGA = 2.0 * Math.PI * FREQUENCY;

    public List<ElectricalNode> nodes = new ArrayList<>();
    public List<Resistance> resistors = new ArrayList<>();
    private final List<Capacitance> capacitors = new ArrayList<>();
    private final List<Inductance> inductors = new ArrayList<>();
    private List<IdealVoltageSource> sources = new ArrayList<>();

    public List<Transformer> transformers = new ArrayList<>();

    public List<ElectricalNode> finalNodes = new ArrayList<>();
    public List<Resistance> finalResistors = new ArrayList<>();
    public List<IdealVoltageSource> finalSources = new ArrayList<>();

    public List<Transformer> finalTransformers = new ArrayList<>();

    public int updateInTicks = -1;

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

    public void setVoltageGen(IRealisticElectric be, int voltage) {

        ElectricalProperties properties = members.get(be.getPos());
        if (properties == null)
            return;

        for (ElectricalComponent component : properties.components) {
            if (component instanceof IdealVoltageSource voltageSource) {
                voltageSource.amplitude = voltage;
            }

        }

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
        finalResistors = new ArrayList<>();
        finalSources = new ArrayList<>();


        transformers = new ArrayList<>();


        //ElectricalNode node5 = new ElectricalNode(0, 5);
        //ElectricalNode node4 = new ElectricalNode(0, 4);
        //ElectricalNode node3 = new ElectricalNode(0, 3);
        //ElectricalNode node2 = new ElectricalNode(0, 2);
        //ElectricalNode node1 = new ElectricalNode(0, 1);
        //nodes.add(node1);
        //nodes.add(node2);
        //nodes.add(node3);
        //nodes.add(node4);
        //nodes.add(node5);
////
        //sources.add(new IdealVoltageSource(node5, node1, 230, 1000, 0, 1));
//
        //resistors.add(new Resistance(node2, node5, 1000, 67, 0));
//
        //resistors.add(new Resistance(node3, node4, 100, 67, 0));
        //transformers.add(new Transformer(node1, node2, node3, node1, 1, 10, 0.999, 1));


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
            if (c instanceof Transformer t) {
                transformers.add(t);
            //    connections.add(new WireConnection((ConnectableElectricalNode) t.pB, (ConnectableElectricalNode) t.sB,1,false));
            }
        });
        // totalNodes = 20;


    }


    public MergedNode getMergedNode(ElectricalNode node) {

        for (ElectricalNode finalNode : finalNodes) {
            if (finalNode instanceof MergedNode m && m.mergedNodes.contains(node)) {
                return m;
            }
        }

        return null;
    }

    public MergedNode mergeNodes(ElectricalNode node1, ElectricalNode node2, int count) {
        MergedNode mergedNode = new MergedNode(node1.pos, -1 + count);

        if (node1 instanceof MergedNode m1) {
            for (ElectricalNode node : m1.mergedNodes) {
                if (!mergedNode.mergedNodes.contains(node)) {
                    mergedNode.mergedNodes.add(node);
                }
            }

        } else mergedNode.mergedNodes.add(node1);

        if (node2 instanceof MergedNode m2) {
            for (ElectricalNode node : m2.mergedNodes) {
                if (!mergedNode.mergedNodes.contains(node)) {
                    mergedNode.mergedNodes.add(node);
                }
            }
        } else mergedNode.mergedNodes.add(node2);
        finalNodes.remove(node1);
        finalNodes.remove(node2);


        return mergedNode;
    }

    public void addConnections() {
        /*
        connections.forEach(c -> {

            ElectricalNode node1 = null;
            ElectricalNode node2 = null;

            for (ElectricalNode node : nodes) {


                if (node.getNetworkId() == c.node1.getNetworkId()) {
                    node1 = node;
                }
                if (node.getNetworkId() == c.node2.getNetworkId()) {
                    node2 = node;
                }
            }

            if (node1 == null || node2 == null) {
                return;
            }


            resistors.add(new Resistance(node1, node2, c.resistance, 0));


        });
        */

        finalNodes = new ArrayList<>(nodes);
        finalResistors = new ArrayList<>();
        finalSources = new ArrayList<>();
        finalTransformers = new ArrayList<>();


        //ElectricalNode n = finalNodes.getFirst();
        //finalNodes.set(0, new MergedNode(n.pos,n.localId, n));
        //finalNodes.remove(finalNodes.getLast());

        int count = 0;
        for (WireConnection connection : connections) {
            ElectricalNode node1 = connection.node1;
            ElectricalNode node2 = connection.node2;

            //   TFMG.LOGGER.debug("Created Merged ElectricalNode from "+node1.localId+" & "+node2.localId);

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            if (isNode1Merged) {
                if (getMergedNode(node1) != null) {
                    node1 = getMergedNode(node1);
                }
            }
            if (isNode2Merged) {
                if (getMergedNode(node2) != null) {
                    node2 = getMergedNode(node2);
                }

            }

            MergedNode mergedNode = mergeNodes(node1, node2, count);
            count--;
            finalNodes.add(mergedNode);


        }

        for (Resistance resistor : resistors) {
            ElectricalNode node1 = resistor.localNodeA;
            ElectricalNode node2 = resistor.localNodeB;

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            if (isNode1Merged) {
                node1 = getMergedNode(node1);
            }
            if (isNode2Merged) {
                node2 = getMergedNode(node2);
            }
            finalResistors.add(new Resistance(node1, node2, resistor.resistance, resistor.localId, resistor.pos));
        }
        for (Transformer transformer : transformers) {
            ElectricalNode node1 = transformer.pA;
            ElectricalNode node2 = transformer.pB;

            ElectricalNode snode1 = transformer.sA;
            ElectricalNode snode2 = transformer.sB;

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            boolean isNodeS1Merged = !finalNodes.contains(snode1);
            boolean isNodeS2Merged = !finalNodes.contains(snode2);
            if (isNode1Merged) {
                node1 = getMergedNode(node1);
            }
            if (isNode2Merged) {
                node2 = getMergedNode(node2);
            }
            if (isNodeS1Merged) {
                snode1 = getMergedNode(snode1);
            }
            if (isNodeS2Merged) {
                snode2 = getMergedNode(snode2);
            }
            finalTransformers.add(new Transformer(node1, node2, snode1, snode2, transformer.L1, transformer.turnsRatio, transformer.k, transformer.id));
        }
        for (IdealVoltageSource source : sources) {
            ElectricalNode node1 = source.localNodeA;
            ElectricalNode node2 = source.localNodeB;

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            if (isNode1Merged) {
                node1 = getMergedNode(node1);
            }
            if (isNode2Merged) {
                node2 = getMergedNode(node2);
            }


            finalSources.add(new IdealVoltageSource(node1, node2, source.amplitude, (int) source.power, source.phaseOffset, source.id));
        }

        for (int i = 0; i < finalNodes.size(); i++) {
            finalNodes.get(i).networkId = i;
        }
        for (int i = 0; i < finalTransformers.size(); i++) {
            finalTransformers.get(i).id = i;
        }

        if (!RealElectricNetworkManager.getWorldFromNetwork(this).isClientSide()) {
            TFMG.LOGGER.debug("node count " + finalNodes.size());
            for (int i = 0; i < finalNodes.size(); i++) {
                ElectricalNode node = finalNodes.get(i);
                if (node instanceof MergedNode m) {
                    TFMG.LOGGER.debug("MergeNode " + i + " " + m.mergedNodes.size());
                    for (int i1 = 0; i1 < m.mergedNodes.size(); i1++) {
                        ElectricalNode n = m.mergedNodes.get(i1);
                        TFMG.LOGGER.debug("  " + i1 + " " + n.localId);
                    }

                } else TFMG.LOGGER.debug("ElectricalNode " + i + " local: " + finalNodes.get(i).localId);
            }
        }

    }

    public void removeConnections(IRealisticElectric be) {
        List<WireConnection> connectionsCopy = new ArrayList<>(connections);
        for (WireConnection connection : connectionsCopy) {
            if (connection.node1.pos == be.getPos() || connection.node2.pos == be.getPos()) {
                connections.remove(connection);
            }
        }

    }


    public Resistance getResistance(long pos, int id) {

        for (Resistance r : finalResistors) {
            if (r.pos == pos && r.localId == id) {
                return r;
            }
        }

        return null;
    }

    public void update() {

        addComponents();
        addConnections();
        totalNodes = finalNodes.size();
        optimize();



        solve();


    }

    public void startSolving(int[] parent, int[] nodeToMatrixIndex, ComplexValue[][] A, ComplexValue[] z) {
        RealElectricNetworkManager.updateQueue.put(RealElectricNetworkManager.getWorldFromNetwork(this), Pair.of(this, new NetworkCalculationData(parent, nodeToMatrixIndex, A, z)));
    }

    public void finishUpdate(NetworkCalculationData data) {

        int n = data.z.length;

        ComplexValue[] y = new ComplexValue[n];
        for (int i = 0; i < n; i++) {
            ComplexValue forwardSum = ComplexValue.ZERO;
            for (int j = 0; j < i; j++) forwardSum = forwardSum.plus(data.LU[i][j].times(y[j]));
            y[i] = data.z[data.pivot[i]].minus(forwardSum);
        }
        ComplexValue[] x = new ComplexValue[n];
        for (int i = n - 1; i >= 0; i--) {
            ComplexValue backwardSum = ComplexValue.ZERO;
            for (int j = i + 1; j < n; j++) backwardSum = backwardSum.plus(data.LU[i][j].times(x[j]));

            x[i] = (y[i].minus(backwardSum)).div(data.LU[i][i]);
        }

        //


        ComplexValue[] finalVoltages = new ComplexValue[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            int matrixIdx = data.nodeToMatrixIndex[i];
            finalVoltages[i] = (matrixIdx == -1) ? ComplexValue.ZERO : x[matrixIdx];


        }

        for (int i = 0; i < totalNodes; i++) {
            int localGroundAnchor = find(data.parent, i);
            ComplexValue voltage = finalVoltages[i].minus(finalVoltages[localGroundAnchor]);
            nodeVoltages.put(i, voltage);
            //TFMG.LOGGER.debug("node " + finalVoltages[i].abs() + " " + finalNodes.get(i).localId);

        }
        for (int i = 0; i < finalSources.size(); i++) {
            IdealVoltageSource source = finalSources.get(i);
            TFMG.LOGGER.debug("source " + i + " Network: " + source.nodeA.networkId + " " + source.nodeB.networkId + " Local: " + source.nodeA.localId + " " + source.nodeB.localId);
        }
        for (int i = 0; i < finalResistors.size(); i++) {
            Resistance r = finalResistors.get(i);
            ComplexValue voltage1 = nodeVoltages.get(r.nodeA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(r.nodeB.getNetworkId());
            double voltage = 0;
            if (voltage1 != null && voltage2 != null) {
                voltage = voltage1.minus(voltage2).abs();
            }
            TFMG.LOGGER.debug("resistor " + i + " Network: " + r.nodeA.networkId + " " + r.nodeB.networkId + " Local: " + r.nodeA.localId + " " + r.nodeB.localId + " Voltage: " + voltage);
        }
        for (int i = 0; i < finalTransformers.size(); i++) {
            Transformer t = finalTransformers.get(i);
            ComplexValue voltage1 = nodeVoltages.get(t.nodeA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(t.nodeB.getNetworkId());

            ComplexValue voltage3 = nodeVoltages.get(t.sA.getNetworkId());
            ComplexValue voltage4 = nodeVoltages.get(t.sB.getNetworkId());
            TFMG.LOGGER.debug("transformer " + i + " Network: " + t.pA.networkId + " " + voltage1 + " " + t.pB.networkId + " " + voltage2 + " Local: " + t.nodeA.localId + " " + t.nodeB.localId + "Secondary " + voltage3 + " " + voltage4);

        }

        sendDataToMembers();
    }

    public void optimize() {

    }

    public void sendDataToMembers() {

        new HashMap<>(members).forEach((l, o) -> {


            if (o.needsUpdateData()) {
                LevelAccessor level = RealElectricNetworkManager.getWorldFromNetwork(this);
                if (level != null) {
                    if (level.getBlockEntity(BlockPos.of(l)) instanceof IRealisticElectric be) {
                        be.onUpdated();
                    }
                }
            }

        });
    }

    public void solve() {

        List<ElectricalComponent> components = new ArrayList<>();

        components.addAll(finalResistors);
        components.addAll(finalTransformers);
        components.addAll(finalSources);

        ElectricalNode groundNode = finalNodes.getFirst();
        Set<ElectricalNode> uniqueNodes = new LinkedHashSet<>();
        uniqueNodes.add(groundNode);
        for (ElectricalComponent c : components) uniqueNodes.addAll(c.getConnectedNodes());

        List<ElectricalNode> activeNodeList = new ArrayList<>(uniqueNodes);
        for (int i = 0; i < activeNodeList.size(); i++)
            activeNodeList.get(i).networkId = i;

       
        int extraRowsCount = 0;
        for (ElectricalComponent c : components) {
            if (c instanceof IdealVoltageSource) {
                ((IdealVoltageSource) c).setIndex(extraRowsCount);
                extraRowsCount += c.getExtraRows();
            } else if (c instanceof Transformer) {
                ((Transformer) c).setIndex(extraRowsCount);
                extraRowsCount += c.getExtraRows();
            }
        }

        int size = activeNodeList.size() + extraRowsCount;
        ComplexValue[][] G = new ComplexValue[size][size];
        ComplexValue[] I = new ComplexValue[size];

        for (int i = 0; i < size; i++) {
            I[i] = new ComplexValue(0, 0);
            for (int j = 0; j < size; j++) G[i][j] = new ComplexValue(0, 0);
        }

        for (ElectricalComponent c : components) c.stamp(G, I, activeNodeList.size());


        for (int j = 0; j < size; j++) G[0][j] = new ComplexValue(0, 0);
        G[0][0] = new ComplexValue(1.0, 0.0);
        I[0] = new ComplexValue(0, 0);

        ComplexValue[] V = solveMNA(G, I, size);

        TFMG.LOGGER.debug("Circuit Values");
        for (ElectricalNode n : activeNodeList) {
            TFMG.LOGGER.debug("ElectricalNode '" + n.getLocalId() + "': " + V[n.networkId]);

            nodeVoltages.put(n.networkId, V[n.networkId]);
        }
        TFMG.LOGGER.debug("TRANSFORMER DATA");
        //for (ElectricalComponent c : components) {
        //    if (c instanceof Transformer t)
        //        t.reportPower(V, activeNodeList.size());
        //}
        for (int i = 0; i < finalSources.size(); i++) {
            IdealVoltageSource source = finalSources.get(i);
            TFMG.LOGGER.debug("source " + i + " Network: " + source.nodeA.networkId + " " + source.nodeB.networkId + " Local: " + source.nodeA.localId + " " + source.nodeB.localId + " voltage " + source.amplitude);
        }
        for (int i = 0; i < finalResistors.size(); i++) {
            Resistance r = finalResistors.get(i);
            ComplexValue voltage1 = nodeVoltages.get(r.nodeA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(r.nodeB.getNetworkId());
            ComplexValue voltage = ComplexValue.ZERO;
            if (voltage1 != null && voltage2 != null) {
                voltage = voltage1.minus(voltage2);
            }
            TFMG.LOGGER.debug("resistor " + i + " Network: " + r.nodeA.networkId + " " + r.nodeB.networkId + " Local: " + r.nodeA.localId + " " + r.nodeB.localId + " Voltage: " + voltage);
        }
        for (int i = 0; i < finalTransformers.size(); i++) {
            Transformer t = finalTransformers.get(i);
            ComplexValue voltage1 = nodeVoltages.get(t.pA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(t.pB.getNetworkId());

            ComplexValue voltage3 = nodeVoltages.get(t.sA.getNetworkId());
            ComplexValue voltage4 = nodeVoltages.get(t.sB.getNetworkId());
            TFMG.LOGGER.debug("transformer " + i + " Network: " + t.pA.networkId+ " " + t.pB.localId + " " + t.sA.networkId+ " " + t.sB.localId + " " + voltage1  + " " + voltage2 + " Secondary " + voltage3 + " " + voltage4);

        }
        sendDataToMembers();
    }
    /*
    public void solve() {

        if (totalNodes == 0)
            return;


        int[] parent = new int[totalNodes];
        for (int i = 0; i < totalNodes; i++) parent[i] = i;

        for (Resistance r : finalResistors) union(parent, r.nodeA.getNetworkId(), r.nodeB.getNetworkId());
        //for (Capacitance c : capacitors) union(parent, c.nodeA, c.nodeB);
        //for (Inductance l : inductors) union(parent, l.nodeA, l.nodeB);
        for (IdealVoltageSource v : finalSources) union(parent, v.nodeA.getNetworkId(), v.nodeB.getNetworkId());

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

        int matrixSize = matrixNodeCount + sources.size()
                + (2 * finalTransformers.size());
        ComplexValue[][] A = new ComplexValue[matrixSize][matrixSize];
        ComplexValue[] z = new ComplexValue[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            z[i] = ComplexValue.ZERO;
            for (int j = 0; j < matrixSize; j++) {
                A[i][j] = ComplexValue.ZERO;
            }
        }


        for (Resistance r : finalResistors)
            stampAdmittance(A, nodeToMatrixIndex, r.nodeA.getNetworkId(), r.nodeB.getNetworkId(), r.getAdmittance());
        //  for (Capacitance c : capacitors) stampAdmittance(A, nodeToMatrixIndex, c.nodeA, c.nodeB, c.getAdmittance());
        //  for (Inductance l : inductors) stampAdmittance(A, nodeToMatrixIndex, l.nodeA, l.nodeB, l.getAdmittance());


        //for (Transformer t : finalTransformers) {
        //    t.stamp(A, z, t.sB.networkId, finalSources.size());
        //}

        for (int i = 0; i < finalSources.size(); i++) {
            IdealVoltageSource v = finalSources.get(i);
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


        ComplexValue[] x = luSolveComplex2(A, z);
        ComplexValue[] finalVoltages = new ComplexValue[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            int matrixIdx = nodeToMatrixIndex[i];
            finalVoltages[i] = (matrixIdx == -1) ? ComplexValue.ZERO : x[matrixIdx];
            nodeVoltages.put(i, (matrixIdx == -1) ? ComplexValue.ZERO : x[matrixIdx]);
        }

        for (int i = 0; i < finalSources.size(); i++) {
            IdealVoltageSource source = finalSources.get(i);
            TFMG.LOGGER.debug("source " + i + " Network: " + source.nodeA.networkId + " " + source.nodeB.networkId + " Local: " + source.nodeA.localId + " " + source.nodeB.localId + " voltage " + source.amplitude);
        }
        for (int i = 0; i < finalResistors.size(); i++) {
            Resistance r = finalResistors.get(i);
            ComplexValue voltage1 = nodeVoltages.get(r.nodeA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(r.nodeB.getNetworkId());
            double voltage = 0;
            if (voltage1 != null && voltage2 != null) {
                voltage = voltage1.minus(voltage2).abs();
            }
            TFMG.LOGGER.debug("resistor " + i + " Network: " + r.nodeA.networkId + " " + r.nodeB.networkId + " Local: " + r.nodeA.localId + " " + r.nodeB.localId + " Voltage: " + voltage);
        }
        for (int i = 0; i < finalTransformers.size(); i++) {
            Transformer t = finalTransformers.get(i);
            ComplexValue voltage1 = nodeVoltages.get(t.nodeA.getNetworkId());
            ComplexValue voltage2 = nodeVoltages.get(t.nodeB.getNetworkId());

            ComplexValue voltage3 = nodeVoltages.get(t.sA.getNetworkId());
            ComplexValue voltage4 = nodeVoltages.get(t.sB.getNetworkId());
            TFMG.LOGGER.debug("transformer " + i + " Network: " + t.pA.networkId + " " + voltage1 + " " + t.pB.networkId + " " + voltage2 + " Local: " + t.nodeA.localId + " " + t.nodeB.localId + "Secondary " + voltage3 + " " + voltage4);

        }

        nodeVoltages.forEach((i, v) -> {
            TFMG.LOGGER.debug("node " + i + " is " + v.abs());
        });

        // startSolving(parent, nodeToMatrixIndex, A, z);


        //for (int i = 0; i < resistors.size(); i++) {
        //    Resistance resistor = resistors.get(i);
        //    int localGroundAnchor = find(parent, resistor.nodeA.getNetworkId());
        //    ComplexValue relativeVoltage1 = finalVoltages[resistor.nodeA.getNetworkId()].minus(finalVoltages[localGroundAnchor]);
        //    ComplexValue relativeVoltage2 = finalVoltages[resistor.nodeB.getNetworkId()].minus(finalVoltages[localGroundAnchor]);


        //}
    }


     */

    /// /////////////////////////
    private ComplexValue[] solveMNA(ComplexValue[][] A, ComplexValue[] b, int n) {
        ComplexValue[][] M = new ComplexValue[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }
        for (int p = 0; p < n; p++) {
            int maxRow = p;
            double maxMag = M[p][p].abs();
            for (int i = p + 1; i < n; i++) {
                if (M[i][p].abs() > maxMag) {
                    maxMag = M[i][p].abs();
                    maxRow = i;
                }
            }
            ComplexValue[] temp = M[p];
            M[p] = M[maxRow];
            M[maxRow] = temp;
            if (M[p][p].abs() < 1e-12) continue;
            for (int i = p + 1; i < n; i++) {
                ComplexValue alpha = M[i][p].div(M[p][p]);
                M[i][n] = M[i][n].minus(alpha.times(M[p][n]));

                for (int j = p; j < n; j++) {
                    M[i][j] = M[i][j].minus(alpha.times(M[p][j]));
                }
            }
        }
        ComplexValue[] x = new ComplexValue[n];
        for (int i = n - 1; i >= 0; i--) {
            ComplexValue sum = new ComplexValue(0, 0);
            for (int j = i + 1; j < n; j++) sum = sum.plus(M[i][j].times(x[j]));
            x[i] = (M[i][n].minus(sum)).div(M[i][i]);
        }
        return x;
    }




}
