package com.drmangotea.tfmg.content.electricity.base;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ConnectionPacket extends BlockEntityDataPacket<SmartBlockEntity> {


    public static final StreamCodec<ByteBuf, ConnectionPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            ConnectionPacket::new
    );

    public ConnectionPacket(BlockPos pos) {
        super(pos);
    }


    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {
            be.onConnected();
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.CONNECTION_PACKET;
    }
}
