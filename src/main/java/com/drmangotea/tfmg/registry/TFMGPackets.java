package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.content.electricity.base.ConnectNeightborsPacket;
import com.drmangotea.tfmg.content.electricity.base.ConnectionPacket;
import com.drmangotea.tfmg.content.electricity.base.NetworkUpdatePacket;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.connection.cables.CablePlacePacket;
import com.drmangotea.tfmg.content.electricity.configuration_wrench.ElectriciansWrenchPacket;
import com.drmangotea.tfmg.content.engines.engine_controller.TransmissionRemovePacket;
import com.drmangotea.tfmg.content.engines.engine_controller.packets.*;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonPacket;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenPacket;
import com.drmangotea.tfmg.content.machinery.vat.base.VatEvaluationPacket;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;


public enum TFMGPackets implements BasePacketPayload.PacketTypeProvider {

    ADVANCED_POTATO_CANNON(AdvancedPotatoCannonPacket.class, AdvancedPotatoCannonPacket.STREAM_CODEC),
    //QUAD_POTATO_CANNON(QuadPotatoCannonPacket.class, QuadPotatoCannonPacket::new, PLAY_TO_CLIENT),
    CONNECT_NEIGHBORS(ConnectNeightborsPacket.class, ConnectNeightborsPacket.STREAM_CODEC),
    NETWORK_UPDATE(NetworkUpdatePacket.class, NetworkUpdatePacket.STREAM_CODEC),
    CONNECTION_PACKET(ConnectionPacket.class, ConnectionPacket.STREAM_CODEC),
    VAT_EVALUATION(VatEvaluationPacket.class, VatEvaluationPacket.STREAM_CODEC),
    COKE_OVEN_PACKET(CokeOvenPacket.class, CokeOvenPacket.STREAM_CODEC),
    UPDATE_IN_FRONT_PACKET(UpdateInFrontPacket.class, UpdateInFrontPacket.STREAM_CODEC),
    TRANSMISSION_REMOVE(TransmissionRemovePacket.class, TransmissionRemovePacket.STREAM_CODEC),
    CABLE_PLACE_PACKET(CablePlacePacket.class, CablePlacePacket.STREAM_CODEC),



    ELECTRICIANS_WRENCH_PACKET(ElectriciansWrenchPacket.class, ElectriciansWrenchPacket.STREAM_CODEC),
    ENGINE_CONTROLLER_INPUT(EngineControllerInputPacket.class, EngineControllerInputPacket.STREAM_CODEC),
   // ENGINE_CONTROLLER_BIND(EngineControllerBindPacket.class, EngineControllerBindPacket::new, PLAY_TO_SERVER),
    ENGINE_TRANSMISSION_PACKET(TransmissionShiftPacket.class, TransmissionShiftPacket.STREAM_CODEC),
    ENGINE_CONTROLLER_STOP_CONTROL(EngineControllerStopControllerPacket.class, EngineControllerStopControllerPacket.STREAM_CODEC),
    ENGINE_START(EngineStartPacket.class, EngineStartPacket.STREAM_CODEC),
    ;


    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> TFMGPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(Create.asResource(name)),
                clazz, codec
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(Create.ID, 1);
        for (TFMGPackets packet : TFMGPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }
}
