package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.ConnectNeightborsPacket;
import com.drmangotea.tfmg.content.electricity.base.ElectricPlacementPacket;
import com.drmangotea.tfmg.content.electricity.base.NetworkUpdatePacket;
import com.drmangotea.tfmg.content.electricity.base.ElectricalBlockFailPacket;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.configuration_wrench.ElectriciansWrenchPacket;
import com.drmangotea.tfmg.content.electricity.connection.cables.CablePlacePacket;
import com.drmangotea.tfmg.content.engines.engine_controller.TransmissionRemovePacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.EngineControllerBindPacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.EngineControllerInputPacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.EngineControllerStopControllerPacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.EngineStartPacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.TransmissionShiftPacket;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonPacket;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonPacket;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenPacket;
import com.drmangotea.tfmg.content.machinery.vat.base.VatEvaluationPacket;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public enum TFMGPackets {

    ADVANCED_POTATO_CANNON(AdvancedPotatoCannonPacket.class, AdvancedPotatoCannonPacket::new, PLAY_TO_CLIENT),
    QUAD_POTATO_CANNON(QuadPotatoCannonPacket.class, QuadPotatoCannonPacket::new, PLAY_TO_CLIENT),
    CONNECT_NEIGHBORS(ConnectNeightborsPacket.class, ConnectNeightborsPacket::new, PLAY_TO_CLIENT),
    NETWORK_UPDATE(NetworkUpdatePacket.class, NetworkUpdatePacket::new, PLAY_TO_CLIENT),
    ELECTRICAL_BLOCK_FAIL(ElectricalBlockFailPacket.class, ElectricalBlockFailPacket::new, PLAY_TO_CLIENT),
    CONNECTION_PACKET(ElectricPlacementPacket.class, ElectricPlacementPacket::new, PLAY_TO_CLIENT),
    VAT_EVALUATION(VatEvaluationPacket.class, VatEvaluationPacket::new, PLAY_TO_CLIENT),
    COKE_OVEN_PACKET(CokeOvenPacket.class, CokeOvenPacket::new, PLAY_TO_CLIENT),
    UPDATE_IN_FRONT_PACKET(UpdateInFrontPacket.class, UpdateInFrontPacket::new, PLAY_TO_CLIENT),
    TRANSMISSION_REMOVE(TransmissionRemovePacket.class, TransmissionRemovePacket::new, PLAY_TO_CLIENT),
    CABLE_PLACE_PACKET(CablePlacePacket.class, CablePlacePacket::new, PLAY_TO_CLIENT),
    ELECTRICIANS_WRENCH_PACKET(ElectriciansWrenchPacket.class, ElectriciansWrenchPacket::new, PLAY_TO_SERVER),
    ENGINE_CONTROLLER_INPUT(EngineControllerInputPacket.class, EngineControllerInputPacket::new, PLAY_TO_SERVER),
    ENGINE_CONTROLLER_BIND(EngineControllerBindPacket.class, EngineControllerBindPacket::new, PLAY_TO_SERVER),
    ENGINE_TRANSMISSION_PACKET(TransmissionShiftPacket.class, TransmissionShiftPacket::new, PLAY_TO_SERVER),
    ENGINE_CONTROLLER_STOP_CONTROL(EngineControllerStopControllerPacket.class, EngineControllerStopControllerPacket::new, PLAY_TO_SERVER),
    ENGINE_START(EngineStartPacket.class, EngineStartPacket::new, PLAY_TO_SERVER),
    ;


    public static final ResourceLocation CHANNEL_NAME = TFMG.asResource("main");
    public static final int NETWORK_VERSION = 4;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private PacketType<?> packetType;

    <T extends SimplePacketBase> TFMGPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                             NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        for (TFMGPackets packet : values())
            packet.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        getChannel().send(
                PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
                message);
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }

}
