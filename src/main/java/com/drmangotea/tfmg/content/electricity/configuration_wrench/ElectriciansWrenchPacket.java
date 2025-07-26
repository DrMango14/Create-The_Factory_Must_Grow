package com.drmangotea.tfmg.content.electricity.configuration_wrench;


import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGPackets;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ElectriciansWrenchPacket implements ServerboundPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectriciansWrenchPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, packet -> packet.group,
            CatnipStreamCodecs.HAND, packet -> packet.hand,
            ElectriciansWrenchPacket::new
    );
    public final int group;
    public final InteractionHand hand;

    public ElectriciansWrenchPacket(int group, InteractionHand hand) {
        this.group = group;
        this.hand = hand;
    }


    @Override
    public void handle(ServerPlayer player) {
        if (player == null) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ElectriciansWrenchItem) {
            applyGroup(stack);
        }


    }

    public void applyGroup(ItemStack stack) {


        stack.set(TFMGDataComponents.CONFIGURATION_WRENCH_NUMBER, group);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.ELECTRICIANS_WRENCH_PACKET;
    }
}
