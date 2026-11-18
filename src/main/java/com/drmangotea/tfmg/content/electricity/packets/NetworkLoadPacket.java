package com.drmangotea.tfmg.content.electricity.packets;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.WireConnection;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.saved_data.NetworkSavedData;
import com.drmangotea.tfmg.content.electricity.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class NetworkLoadPacket implements ClientboundPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, NetworkLoadPacket> STREAM_CODEC = StreamCodec.of((b, v) -> v.write(b), NetworkLoadPacket::new);
	
    public List<Pair<ResourceKey<Level>, ElectricalNetwork>> networks = new ArrayList<>();

    public NetworkLoadPacket(List<ElectricalNetwork> networks) {
        networks.forEach(n -> {
            if (n.world instanceof ServerLevel level) {
                this.networks.add(Pair.of(level.dimension(), n));
            }
        });
    }

    public NetworkLoadPacket(FriendlyByteBuf buffer) {
        int networkCount = buffer.readInt();

        DimensionPalette dimensions = DimensionPalette.receive(buffer);

        for (int i = 0; i < networkCount; i++) {
            ResourceKey<Level> dimension = dimensions.decode(i);

            ElectricalNetwork network = new ElectricalNetwork(null);
            network.id = buffer.readLong();
            network.totalNodes = buffer.readInt();

            int memberCount = buffer.readInt();

            for (int j = 0; j < memberCount; j++) {
                BlockPos position = buffer.readBlockPos();

                ResourceLocation id = buffer.readResourceLocation();
                Direction direction = Direction.from3DDataValue(buffer.readInt());
                // Direction direction = Direction.NORTH;
                List<Direction> directions = new ArrayList<>();
                int directionCount = buffer.readInt();
                for (int k = 0; k < directionCount; k++) {
                    directions.add(Direction.from3DDataValue(buffer.readInt()));
                }

                ElectricalProperties properties = NetworkSavedData.getElectricalProperties(id, direction, directions, position);
				if (properties == null) return;

                int resistorCount = buffer.readInt();
                List<Double> resistors = new ArrayList<>();
                for (int y = 0; y < resistorCount; y++) {
                    resistors.add(buffer.readDouble());
                }

                int sourceCount = buffer.readInt();
                List<Integer> sources = new ArrayList<>();
                for (int y = 0; y < sourceCount; y++) {
                    sources.add(buffer.readInt());
                }

                int transformerCount = buffer.readInt();
                List<Double> transformers = new ArrayList<>();
                for (int y = 0; y < transformerCount; y++) {
                    transformers.add(buffer.readDouble());
                }
                int rIndex = 0;
                int sIndex = 0;
                int tIndex = 0;

                for (ElectricalComponent c : properties.components) {

                    if (c instanceof Resistance r && !resistors.isEmpty()) {
                        r.resistance = resistors.get(rIndex);

                        rIndex++;
                    }
                    if (c instanceof IdealVoltageSource s && !sources.isEmpty()) {
                        s.amplitude = sources.get(sIndex);
                        sIndex++;
                    }
                    if (c instanceof Transformer t && !transformers.isEmpty()) {
                        t.turnsRatio = transformers.get(tIndex);
                        tIndex++;
                    }
                }
                network.members.put(position, properties);

            }

            int connectionCount = buffer.readInt();

            for (int j = 0; j < connectionCount; j++) {
                boolean shouldRender = buffer.readBoolean();
                ResourceLocation cableType = ResourceLocation.parse(buffer.readUtf().replace('.',':'));

                BlockPos pos1 = buffer.readBlockPos();
                int id1 = buffer.readInt();
                BlockPos pos2 = buffer.readBlockPos();
                int id2 = buffer.readInt();

                List<ElectricalNode> nodes1 = network.getNodes(pos1);
                List<ElectricalNode> nodes2 = network.getNodes(pos2);

                ConnectableElectricalNode n1 = null;
                ConnectableElectricalNode n2 = null;

                for (ElectricalNode electricalNode : nodes1) {
                    if (electricalNode instanceof ConnectableElectricalNode node && node.localId == id1) {
                        n1 = node;
                    }
                }
                for (ElectricalNode electricalNode : nodes2) {
                    if (electricalNode instanceof ConnectableElectricalNode node && node.localId == id2) {
                        n2 = node;
                    }
                }

                if (n1 != null && n2 != null) {
                    n1.pos = pos1;
                    n2.pos = pos2;
                    network.connections.add(new WireConnection(n1, n2, cableType, shouldRender));
                }

            }
            networks.add(Pair.of(dimension, network));
        }
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(networks.size());
        DimensionPalette dimensions = new DimensionPalette();
        networks.forEach(n -> dimensions.encode(n.getFirst()));
        dimensions.send(buffer);

        networks.forEach(a -> {

            ElectricalNetwork network = a.getSecond();

            buffer.writeLong(network.id);
            buffer.writeInt(network.totalNodes);

            int memberCount = network.members.size();
            List<BlockPos> positions = network.members.keySet().stream().toList();
            List<ElectricalProperties> properties = network.members.values().stream().toList();

            buffer.writeInt(memberCount);

            for (int i = 0; i < memberCount; i++) {
                buffer.writeBlockPos(positions.get(i));
                ElectricalProperties property = properties.get(i);
                buffer.writeResourceLocation(property.getType().id());

                //
                Direction direction = Direction.NORTH;
                if (property instanceof DirectionalElectricalProperties directionalProperties) {
                    direction = directionalProperties.direction;
                }
                buffer.writeInt(direction.get3DDataValue());

                buffer.writeInt(property.getConnectableDirections().size());
                for (Direction d : property.getConnectableDirections()) {
                    buffer.writeInt(d.get3DDataValue());
                }

                //
                List<Resistance> resistors = new ArrayList<>();
                List<IdealVoltageSource> sources = new ArrayList<>();
                List<Transformer> transformers = new ArrayList<>();
                for (ElectricalComponent component : property.components) {
                    if (component instanceof IdealVoltageSource source) {
                        sources.add(source);
                    }
                    if (component instanceof Resistance resistance) {
                        resistors.add(resistance);
                    }
                    if (component instanceof Transformer transformer) {
                        transformers.add(transformer);
                    }
                }
                buffer.writeInt(resistors.size());
                resistors.forEach(r -> buffer.writeDouble(r.resistance));
                buffer.writeInt(sources.size());
                sources.forEach(r -> buffer.writeInt((int) r.amplitude));
                buffer.writeInt(transformers.size());
                transformers.forEach(t -> buffer.writeDouble(t.turnsRatio));
            }
            List<WireConnection> connections = network.connections;
            int connectionCount = connections.size();
            buffer.writeInt(connectionCount);
            for (int i = 0; i < connectionCount; i++) {
                WireConnection connection = connections.get(i);
				buffer.writeBoolean(connection.render());
				buffer.writeUtf(connection.cableTypeKey().toString().replace(':','.'));
                buffer.writeBlockPos(connection.node1().pos);
                buffer.writeInt(connection.node1().getLocalId());
                buffer.writeBlockPos(connection.node2().pos);
                buffer.writeInt(connection.node2().getLocalId());


            }
        });
    }

    @Override
    public void handle(LocalPlayer player) {

        ResourceKey<Level> playerDimension = player.level().dimension();

        ElectricalNetwork network = null;
        List<Pair<ResourceKey<Level>, ElectricalNetwork>> networks = this.networks;
        //

        for (Pair<ResourceKey<Level>, ElectricalNetwork> n : networks) {
            if (n.getFirst() == playerDimension) {
                network = n.getSecond();
            }
        }

        if (network != null) {
            ElectricNetworkManager.getNetwork(player.level()).members.putAll(network.members);
            ElectricNetworkManager.getNetwork(player.level()).connections.addAll(network.connections);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.NETWORK_LOAD;
    }
}
