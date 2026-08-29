package com.drmangotea.tfmg.content.electricity.experimental.packets;


import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.WireConnection;
import com.drmangotea.tfmg.content.electricity.experimental.blocks.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.saved_data.NetworkSavedData;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.*;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class NetworkLoadPacket implements ClientboundPacketPayload {


    public static final StreamCodec<FriendlyByteBuf, NetworkLoadPacket> STREAM_CODEC = StreamCodec.of((b, v) -> v.write(b), NetworkLoadPacket::new);


    public List<Pair<ResourceKey<Level>, RealElectricalNetwork>> networks = new ArrayList<>();

    public NetworkLoadPacket(List<RealElectricalNetwork> networks) {
        networks.forEach(n -> {
            if (n.world instanceof ServerLevel level) {
                this.networks.add(Pair.of((level).dimension(), n));
            }
        });
    }


    public NetworkLoadPacket(FriendlyByteBuf buffer) {
        int networkCount = buffer.readInt();

        DimensionPalette dimensions = DimensionPalette.receive(buffer);

        for (int i = 0; i < networkCount; i++) {
            ResourceKey<Level> dimension = dimensions.decode(i);

            RealElectricalNetwork network = new RealElectricalNetwork(null);
            network.id = buffer.readLong();
            network.totalNodes = buffer.readInt();

            int memberCount = buffer.readInt();

            for (int j = 0; j < memberCount; j++) {
                long position = buffer.readLong();

                int id = buffer.readInt();
                Direction direction = Direction.from3DDataValue(buffer.readInt());
               // Direction direction = Direction.NORTH;


                ElectricalProperties properties = NetworkSavedData.getElectricalProperties(id, direction);


                int resistorCount = buffer.readInt();
                List<Integer> resistors = new ArrayList<>();
                for (int y = 0; y < resistorCount; y++) {
                    resistors.add(buffer.readInt());
                }

                int sourceCount = buffer.readInt();
                List<Integer> sources = new ArrayList<>();
                for (int y = 0; y < sourceCount; y++) {
                    sources.add(buffer.readInt());
                }
                int rIndex = 0;
                int sIndex = 0;

                for (ElectricalComponent c : properties.components) {

                    if (c instanceof Resistance r && !resistors.isEmpty()) {
                        r.resistance = resistors.get(rIndex);
                        rIndex++;
                    }
                    if (c instanceof IdealVoltageSource s && !sources.isEmpty()) {
                        s.amplitude = sources.get(sIndex);
                        sIndex++;
                    }
                }
                network.members.put(position, properties);

            }


            int connectionCount = buffer.readInt();

            for (int j = 0; j < connectionCount; j++) {
                double resistance = buffer.readDouble();

                long pos1 = buffer.readLong();
                int id1 = buffer.readInt();
                long pos2 = buffer.readLong();
                int id2 = buffer.readInt();

                List<ElectricalNode> nodes1 = network.getNodes(pos1);
                List<ElectricalNode> nodes2 = network.getNodes(pos2);

                ConnectingElectricalNode n1 = null;
                ConnectingElectricalNode n2 = null;

                for (ElectricalNode electricalNode : nodes1) {
                    if (electricalNode instanceof ConnectingElectricalNode node && node.localId == id1) {
                        n1 = node;
                    }
                }
                for (ElectricalNode electricalNode : nodes2) {
                    if (electricalNode instanceof ConnectingElectricalNode node && node.localId == id2) {
                        n2 = node;
                    }
                }

                if (n1 != null && n2 != null) {
                    n1.pos = pos1;
                    n2.pos = pos2;
                    network.connections.add(new WireConnection(n1, n2, resistance));
                }

            }
            int test = network.connections.size();
            networks.add(Pair.of(dimension, network));
        }

    }


    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(networks.size());
        DimensionPalette dimensions = new DimensionPalette();
        networks.forEach(n -> dimensions.encode(n.getFirst()));
        dimensions.send(buffer);

        networks.forEach(a -> {

            RealElectricalNetwork network = a.getSecond();

            buffer.writeLong(network.id);
            buffer.writeInt(network.totalNodes);

            int memberCount = network.members.size();
            List<Long> positions = network.members.keySet().stream().toList();
            List<ElectricalProperties> properties = network.members.values().stream().toList();

            buffer.writeInt(memberCount);

            for (int i = 0; i < memberCount; i++) {
                buffer.writeLong(positions.get(i));
                ElectricalProperties property = properties.get(i);
                buffer.writeInt(property.getId());

                //
                Direction direction = Direction.NORTH;
                if (property instanceof DirectionalElectricalProperties directionalProperties) {
                    direction = directionalProperties.direction;
                }
                buffer.writeInt(direction.get3DDataValue());

                //
                List<Resistance> resistors = new ArrayList<>();
                List<IdealVoltageSource> sources = new ArrayList<>();
                for (ElectricalComponent component : property.components) {
                    if (component instanceof IdealVoltageSource source) {
                        sources.add(source);
                    }
                    if (component instanceof Resistance resistance) {
                        resistors.add(resistance);
                    }
                }
                buffer.writeInt(resistors.size());
                resistors.forEach(r -> {
                    buffer.writeInt((int) r.resistance);
                });
                buffer.writeInt(sources.size());
                sources.forEach(r -> {
                    buffer.writeInt((int) r.amplitude);
                });
            }
            List<WireConnection> connections = network.connections;
            int connectionCount = connections.size();
            buffer.writeInt(connectionCount);
            for (int i = 0; i < connectionCount; i++) {
                WireConnection connection = connections.get(i);
                buffer.writeDouble(connection.resistance());
                long meow = connection.node1().pos;
                buffer.writeLong(connection.node1().pos);
                buffer.writeInt(connection.node1().getLocalId());
                buffer.writeLong(connection.node2().pos);
                buffer.writeInt(connection.node2().getLocalId());


            }
        });


    }

    @Override
    public void handle(LocalPlayer player) {

        ResourceKey<Level> playerDimension = player.level().dimension();

        RealElectricalNetwork network = null;
        List<Pair<ResourceKey<Level>, RealElectricalNetwork>> networks = this.networks;
        //

        for (Pair<ResourceKey<Level>, RealElectricalNetwork> n : networks) {
            if (n.getFirst() == playerDimension) {
                network = n.getSecond();
            }
        }

        if (network != null) {
            RealElectricNetworkManager.getNetwork(player.level()).members.putAll(network.members);
            RealElectricNetworkManager.getNetwork(player.level()).connections.addAll(network.connections);
        }
    }


    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.NETWORK_LOAD;
    }


}
