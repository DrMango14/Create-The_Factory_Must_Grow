package com.drmangotea.tfmg.content.electricity.base;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class ConnectNeightborsPacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, ConnectNeightborsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            ConnectNeightborsPacket::new
    );



    public ConnectNeightborsPacket(BlockPos pos) {
        super(pos);


    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {
            be.onPlaced();

        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.CONNECT_NEIGHBORS;
    }
}