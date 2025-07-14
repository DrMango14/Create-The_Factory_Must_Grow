package com.drmangotea.tfmg.content.electricity.connection.cables;


import com.drmangotea.tfmg.content.electricity.base.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CablePlacePacket extends BlockEntityDataPacket<SmartBlockEntity> {



    public static final StreamCodec<ByteBuf, CablePlacePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            CablePlacePacket::new
    );

    public CablePlacePacket(BlockPos pos) {
        super(pos);


    }





    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof CableConnectorBlockEntity be) {
            be.onConnected();
        }

    }


    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.CABLE_PLACE_PACKET;
    }
}
