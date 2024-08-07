package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonPacket;
import com.drmangotea.createindustry.items.weapons.quad_potato_cannon.QuadPotatoCannonPacket;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.fabricators_of_create.porting_lib.util.NetworkDirection;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.github.fabricators_of_create.porting_lib.util.NetworkDirection.PLAY_TO_CLIENT;


public enum TFMGPackets {

    ADVANCED_POTATO_CANNON(AdvancedPotatoCannonPacket.class, AdvancedPotatoCannonPacket::new, PLAY_TO_CLIENT),
    QUAD_POTATO_CANNON(QuadPotatoCannonPacket.class, QuadPotatoCannonPacket::new, PLAY_TO_CLIENT)
    ;

    public static final ResourceLocation CHANNEL_NAME = CreateTFMG.asResource("main");
    public static final int NETWORK_VERSION = 3;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private PacketType<?> packetType;

    <T extends SimplePacketBase> TFMGPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                            NetworkDirection direction) {
        packetType = new TFMGPackets.PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = new SimpleChannel(CHANNEL_NAME);
        for (TFMGPackets packet : values())
            packet.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        getChannel().sendToClientsAround((S2CPacket) message, (ServerLevel) world, pos, range);
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private Function<FriendlyByteBuf, T> decoder;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            decoder = factory;
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            switch (direction) {
                case PLAY_TO_CLIENT -> getChannel().registerS2CPacket(type, index++, decoder);
                case PLAY_TO_SERVER -> getChannel().registerC2SPacket(type, index++, decoder);
            }
        }
    }

}
