package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.content.electricity.simulation.*;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.MergedNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodePos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;

public class ElectricalNetwork {
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
    public Map<BlockPos, ElectricalProperties> members = new HashMap<>();
    public List<WireConnection> connections = new ArrayList<>();

    public LevelAccessor world;

    public ElectricalNetwork(LevelAccessor world) {
        this.world = world;
    }


    public Map<Integer, ComplexValue> nodeVoltages = new HashMap<>();

    public Map<Integer, ElectricalNode> getNodes(BlockPos pos) {
		if (members.get(pos) instanceof ElectricalProperties member)
			return member.nodes;
		return Map.of();
	}
	
	public Optional<ElectricalNode> getNode(NodePos pos) {
		return Optional.ofNullable(getNodes(pos.position()).get(pos.localId()));
	}


    public void addMember(BlockPos pos, ElectricalProperties properties) {
        members.put(pos, properties);
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent, parent[i]);
    }
    
	public void setVoltageGen(BlockPos pos, int voltage) {
        ElectricalProperties properties = members.get(pos);
        if (properties == null)
            return;
        for (ElectricalComponent component : properties.components) {
            if (component instanceof IdealVoltageSource voltageSource) {
                voltageSource.amplitude = voltage;
            }

        }
        update();
    }

    public void setVoltageGen(IElectric be, int voltage) {
        this.setVoltageGen(be.getPos(), voltage);
    }

    public void setPhaseShift(BlockPos pos, int degrees) {
        ElectricalProperties properties = members.get(pos);
        if (properties == null)
            return;
        for (ElectricalComponent component : properties.components) {
            if (component instanceof IdealVoltageSource voltageSource) {
                voltageSource.phaseOffset = degrees;
            }

        }
        update();
    }

    public void setPhaseShift(IElectric be, int degrees) {
        this.setPhaseShift(be.getPos(), degrees);
    }

    public void setResistance(IElectric be, int id, double resistance) {
        this.setResistance(be.getPos(), id, resistance);
    }
    
	public void setResistance(BlockPos pos, int id, double resistance) {
        ElectricalProperties p = members.get(pos);
		for (ElectricalComponent component : p.components) {
			if (component instanceof Resistance resistor && resistor.localId == id) {
				resistor.resistance = resistance;
			}
		}
        update();
    }

    public void setTransformerRatio(IElectric be, int id, double ratio) {
        this.setTransformerRatio(be.getPos(), id, ratio);
    }
	
    public void setTransformerRatio(BlockPos pos, int id, double ratio) {
		ElectricalProperties p = members.get(pos);
		for (ElectricalComponent component : p.components) {
			if (component instanceof Transformer transformer && transformer.localId == id) {
				transformer.turnsRatio = ratio;
			}
		}
        update();
    }


    public void addComponents() {
        nodes = new ArrayList<>();
        resistors = new ArrayList<>();
        sources = new ArrayList<>();
        finalResistors = new ArrayList<>();
        finalSources = new ArrayList<>();
        transformers = new ArrayList<>();

        List<ElectricalComponent> components = new ArrayList<>();
        members.forEach((l, p) -> {

            if (p == null) return;
			
			nodes.addAll(p.nodes.values());
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
                if (v.amplitude == 0) {
                    resistors.add(new Resistance(v.nodeA, v.nodeB, 0.01, 0, v.pos));
                } else

                    sources.add(v);
            }
            if (c instanceof Transformer t&&t.turnsRatio!=0) {
                transformers.add(t);
                //    connections.add(new WireConnection((ConnectableElectricalNode) t.pB, (ConnectableElectricalNode) t.sB,1,false));
            }
        });
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
        MergedNode mergedNode = new MergedNode(node1.pos.position(), -1 + count);

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


        finalNodes = new ArrayList<>(nodes);
        finalResistors = new ArrayList<>();
        finalSources = new ArrayList<>();
        finalTransformers = new ArrayList<>();

        int count = 0;
        for (WireConnection connection : connections) {
            ElectricalNode node1 = getNode(connection.node1()).orElse(null);
            ElectricalNode node2 = getNode(connection.node2()).orElse(null);
			
			if (node1 == null || node2 == null) return;

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            if (isNode1Merged) {
				ElectricalNode merge = getMergedNode(node1);
				if (merge != null) node1 = merge;
            }
            if (isNode2Merged) {
				ElectricalNode merge = getMergedNode(node1);
				if (merge != null) node2 = merge;
            }

            MergedNode mergedNode = mergeNodes(node1, node2, count);
            count--;
            finalNodes.add(mergedNode);


        }

        for (Resistance resistor : resistors) {
            ElectricalNode node1 = resistor.nodeA;
            ElectricalNode node2 = resistor.nodeB;

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
            finalTransformers.add(new Transformer(node1, node2, snode1, snode2, transformer.L1, transformer.turnsRatio, transformer.k, transformer.id, transformer.pos));
        }
        for (IdealVoltageSource source : sources) {
            ElectricalNode node1 = source.nodeA;
            ElectricalNode node2 = source.nodeB;

            boolean isNode1Merged = !finalNodes.contains(node1);
            boolean isNode2Merged = !finalNodes.contains(node2);
            if (isNode1Merged) {
                node1 = getMergedNode(node1);
            }
            if (isNode2Merged) {
                node2 = getMergedNode(node2);
            }


            finalSources.add(new IdealVoltageSource(node1, node2, source.amplitude, (int) source.power, source.phaseOffset, source.id, source.pos));
        }

        for (int i = 0; i < finalNodes.size(); i++) {
            finalNodes.get(i).networkId = i;
        }
        for (int i = 0; i < finalTransformers.size(); i++) {
            finalTransformers.get(i).id = i;
        }


    }

    public void removeConnections(IElectric be) {
		BlockPos pos = be.getPos();
		connections = connections.stream().filter(
			c -> !c.connectedTo(pos)
		).toList();
    }


    public Resistance getResistance(BlockPos pos, int id) {
        for (Resistance r : finalResistors) {
            if (r.pos == pos && r.localId == id) {
                return r;
            }
        }

        return null;
    }

    public IdealVoltageSource getVoltageSource(BlockPos pos, int id) {

        for (IdealVoltageSource s : finalSources) {
            if (s.pos == pos && s.localId == id) {
                return s;
            }
        }

        return null;
    }

    public Transformer getTransformer(BlockPos pos, int id) {

        for (Transformer t : finalTransformers) {
            if (t.pos == pos && t.localId == id) {
                return t;
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

    public void optimize() {

    }

    public void sendDataToMembers() {
		LevelAccessor level = ElectricalNetworkManager.getWorldFromNetwork(this);
		if (level == null) return;
        new HashMap<>(members).forEach((pos, o) -> {
            if (o.needsUpdateData()) {
                if (level.getBlockEntity(pos) instanceof IElectric be) {
					be.onUpdated();
                }
            }
        });
    }

    public void solve() {

        List<ElectricalComponent> components = new ArrayList<>();

        components.addAll(finalResistors);
        components.addAll(finalTransformers);
        components.addAll(finalSources);
        if (finalNodes.isEmpty())
            return;
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


        for (ElectricalNode n : activeNodeList) {


            nodeVoltages.put(n.networkId, V[n.networkId]);
        }


        sendDataToMembers();
    }


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
