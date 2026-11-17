package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.spark.ElectricitySparkPacket;
import com.drmangotea.tfmg.content.electricity.configuration_wrench.ElectriciansWrenchPacket;
import com.drmangotea.tfmg.content.electricity.packets.*;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonPacket;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonPacket;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenPacket;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationTowerPacket;
import com.drmangotea.tfmg.content.machinery.vat.base.VatEvaluationPacket;
import com.drmangotea.tfmg.integration.jei.util.EngineFuelsRecipe;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;


public enum TFMGPackets implements BasePacketPayload.PacketTypeProvider {

    // Server to Client
    ADVANCED_POTATO_CANNON(AdvancedPotatoCannonPacket.class, AdvancedPotatoCannonPacket.STREAM_CODEC),
    QUAD_POTATO_CANNON(QuadPotatoCannonPacket.class, QuadPotatoCannonPacket.STREAM_CODEC),

    VAT_EVALUATION(VatEvaluationPacket.class, VatEvaluationPacket.STREAM_CODEC),
    COKE_OVEN_PACKET(CokeOvenPacket.class, CokeOvenPacket.STREAM_CODEC),
    DISTILLATION_PACKET(DistillationTowerPacket.class, DistillationTowerPacket.STREAM_CODEC),

    //TRANSMISSION_REMOVE(TransmissionRemovePacket.class, TransmissionRemovePacket.STREAM_CODEC),
    ELECTRICITY_SPARK_EFFECT(ElectricitySparkPacket.class, ElectricitySparkPacket.STREAM_CODEC),
    ADD_ELECTRICAL_COMPONENT(AddElectricalComponentPacket.class, AddElectricalComponentPacket.STREAM_CODEC),
    NETWORK_LOAD(NetworkLoadPacket.class, NetworkLoadPacket.STREAM_CODEC),
    UPDATE_VOLTAGE(UpdateVoltagePacket.class, UpdateVoltagePacket.STREAM_CODEC),
    UPDATE_NETWORK(UpdateNetworkPacket.class, UpdateNetworkPacket.STREAM_CODEC),
    UPDATE_ACCUMULATOR(UpdateAccumulatorPacket.class, UpdateAccumulatorPacket.STREAM_CODEC),
    ENGINE_FUELS(EngineFuelsRecipe.SyncPacket.class, EngineFuelsRecipe.SyncPacket.STREAM_CODEC),


    // Client to Server
    ELECTRICIANS_WRENCH_PACKET(ElectriciansWrenchPacket.class, ElectriciansWrenchPacket.STREAM_CODEC),
    //ENGINE_CONTROLLER_INPUT(EngineControllerInputPacket.class, EngineControllerInputPacket.STREAM_CODEC),
   // ENGINE_CONTROLLER_BIND(EngineControllerBindPacket.class, EngineControllerBindPacket::new, PLAY_TO_SERVER),
    //ENGINE_TRANSMISSION_PACKET(TransmissionShiftPacket.class, TransmissionShiftPacket.STREAM_CODEC),
    //ENGINE_CONTROLLER_STOP_CONTROL(EngineControllerStopControllerPacket.class, EngineControllerStopControllerPacket.STREAM_CODEC),
    //ENGINE_START(EngineStartPacket.class, EngineStartPacket.STREAM_CODEC),
    ;


    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> TFMGPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(new CustomPacketPayload.Type<>(TFMG.asResource(name)), clazz, codec);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(TFMG.MOD_ID, 1);
        for (TFMGPackets packet : TFMGPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }
}
