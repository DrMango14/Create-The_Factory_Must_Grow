package com.drmangotea.tfmg.content.electricity.experimental.packets;


import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class RealNetworkUpdatePacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, RealNetworkUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            RealNetworkUpdatePacket::new
    );


    public RealNetworkUpdatePacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if (blockEntity instanceof IRealisticElectric be) {
            be.updateNetwork(blockEntity.getBlockPos());
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.REALISTIC_NETWORK_UPDATE;
    }
}
