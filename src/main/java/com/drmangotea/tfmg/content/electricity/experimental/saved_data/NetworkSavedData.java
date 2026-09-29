package com.drmangotea.tfmg.content.electricity.experimental.saved_data;

import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.WireConnection;
import com.drmangotea.tfmg.content.electricity.experimental.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.ThreePhaseGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.DebugResistorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.lights.LightProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.adapters.FourSlotAdapterProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.adapters.TwoSlotAdapterProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.CableProperties;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.wires.ConnectorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class NetworkSavedData extends SavedData {

    private List<RealElectricalNetwork> list = new ArrayList<>();

    public NetworkSavedData() {
    }

    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {


        List<RealElectricalNetwork> list = RealElectricNetworkManager.networks.values().stream().toList();

        compound = new CompoundTag();

        for (RealElectricalNetwork network : list) {
            CompoundTag networkNBT = new CompoundTag();

            if (network.world instanceof ServerLevel serverLevel) {

                networkNBT.putString("Dimension", serverLevel.dimension().location().toString());

                CompoundTag members = new CompoundTag();
                networkNBT.putInt("Member Count", network.members.size());
                networkNBT.putInt("Node Count", network.totalNodes);
                for (int i = 0; i < network.members.size(); i++) {
                    CompoundTag member = new CompoundTag();
                    ElectricalProperties properties = network.members.values().stream().toList().get(i);
                    long pos = network.members.keySet().stream().toList().get(i);

                    member.putLong("Position", pos);
                    member.putInt("Property Id", properties.getId());
                    for (ElectricalComponent component : properties.components) {
                        if (component instanceof IdealVoltageSource source) {
                            member.putInt("Voltage " + source.id, (int) source.amplitude);
                        }
                        if (component instanceof Resistance resistance) {
                            member.putInt("Resistance " + resistance.localId, (int) resistance.resistance);
                        }
                    }
                    if (properties instanceof DirectionalElectricalProperties p)
                        member.putInt("direction", p.direction.get3DDataValue());

                    // for (ElectricalNode node : properties.nodes) {
                    //     if (node instanceof ConnectingElectricalNode connectingNode) {
                    //         CompoundTag nodeTag = new CompoundTag();
                    //         nodeTag.putDouble("X", connectingNode.getPosition().x());
                    //         nodeTag.putDouble("Y", connectingNode.getPosition().y());
                    //         nodeTag.putDouble("Z", connectingNode.getPosition().z());
                    //         member.put("node " + connectingNode.localId, nodeTag);
                    //     }
                    // }


                    members.put("member " + i, member);
                }
                networkNBT.put("blocks", members);
                ///
                CompoundTag connections = new CompoundTag();
                networkNBT.putInt("Connection Count", network.connections.size());
                for (int i = 0; i < network.connections.size(); i++) {
                    CompoundTag connectionTag = new CompoundTag();

                    WireConnection connection = network.connections.get(i);

                    connectionTag.putDouble("Resistance", connection.resistance);
                    connectionTag.putBoolean("Should Render",connection.render);

                    CompoundTag node1 = new CompoundTag();
                    node1.putLong("position", connection.node1.pos);
                    node1.putInt("local id", connection.node1.getLocalId());
                    connectionTag.put("Node1", node1);

                    CompoundTag node2 = new CompoundTag();
                    node2.putLong("position", connection.node2.pos);
                    node2.putInt("local id", connection.node2.getLocalId());
                    connectionTag.put("Node2", node2);


                    connections.put("connection " + i, connectionTag);

                }

                networkNBT.put("connections", connections);

                compound.put(serverLevel.dimension().location().toLanguageKey(), networkNBT);

            }
            ;
        }

        return compound;
    }

    public List<RealElectricalNetwork> getNetworks() {
        return list;
    }


    public static NetworkSavedData load(CompoundTag compound, HolderLookup.Provider registries) {
        NetworkSavedData sd = new NetworkSavedData();

        List<RealElectricalNetwork> list = RealElectricNetworkManager.networks.values().stream().toList();
        for (RealElectricalNetwork network : list) {
            if (network.world instanceof ServerLevel serverLevel) {
                CompoundTag networkTag = compound.getCompound(serverLevel.dimension().location().toLanguageKey());
                network.totalNodes = networkTag.getInt("Node Count");
                int memberCount = networkTag.getInt("Member Count");
                for (int i = 0; i < memberCount; i++) {
                    CompoundTag member = networkTag.getCompound("blocks").getCompound("member " + i);
                    long pos = member.getLong("Position");

                    Direction direction = Direction.NORTH;

                    if (member.contains("direction")) {
                        direction = Direction.from3DDataValue(member.getInt("direction"));
                    }
                    ElectricalProperties properties = getElectricalProperties(member.getInt("Property Id"), direction,pos);

                    properties.position = pos;
                    network.members.put(pos, properties);

                }
                int connectionCount = networkTag.getInt("Connection Count");
                for (int i = 0; i < connectionCount; i++) {
                    CompoundTag connection = networkTag.getCompound("connections").getCompound("connection " + i);
                    double resistance = connection.getDouble("Resistance");
                    boolean shouldRender = connection.getBoolean("Should Render");

                    ConnectableElectricalNode node1 = null;
                    CompoundTag node1Tag = connection.getCompound("Node1");
                    long pos1 = node1Tag.getLong("position");
                    int id1 = node1Tag.getInt("local id");
                    for (ElectricalNode node : network.getNodes(pos1)) {
                        if (node.localId == id1 && node instanceof ConnectableElectricalNode connectingNode) {
                            node1 = connectingNode;
                            node1.pos = pos1;
                        }
                    }


                    ConnectableElectricalNode node2 = null;
                    CompoundTag node2Tag = connection.getCompound("Node2");
                    long pos2 = node2Tag.getLong("position");
                    int id2 = node2Tag.getInt("local id");
                    for (ElectricalNode node : network.getNodes(pos2)) {
                        if (node.localId == id2 && node instanceof ConnectableElectricalNode connectingNode) {
                            node2 = connectingNode;
                            node2.pos = pos2;
                        }
                    }

                    if (node1 != null && node2 != null) {



                        network.connections.add(new WireConnection(node1, node2, resistance, shouldRender));
                    }

                }
            }
        }

        return sd;
    }

    public static ElectricalProperties getElectricalProperties(int id, Direction direction, long pos) {


        return switch (id) {
            case 1 -> new ThreePhaseGeneratorProperties(pos, direction);
            case 2 -> new ConnectorProperties(pos);
            case 3 -> new DebugResistorProperties(pos, direction);
            case 4 -> new CableProperties(pos, List.of(Direction.values()));
            case 5 -> new FourSlotAdapterProperties(pos, direction);
            case 6 -> new TwoSlotAdapterProperties(pos,  direction);
            case 7 -> new LightProperties(pos);
            default -> new ElectricalProperties(pos);

        };
    }

    public static Factory<NetworkSavedData> factory() {
        return new Factory<>(NetworkSavedData::new, NetworkSavedData::load);
    }


    public static NetworkSavedData load(MinecraftServer server) {
        return server.overworld()
                .getDataStorage()
                .computeIfAbsent(factory(), "tfmg_networks");
    }


}
