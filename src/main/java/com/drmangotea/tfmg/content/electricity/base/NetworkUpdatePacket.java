package com.drmangotea.tfmg.content.electricity.base;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class NetworkUpdatePacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, NetworkUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            NetworkUpdatePacket::new
    );


    public NetworkUpdatePacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if (blockEntity instanceof IElectric be) {
            be.updateNetwork();
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.NETWORK_UPDATE;
    }
}
