package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.content.electricity.base.ConnectNeightborsPacket;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class TransmissionRemovePacket extends BlockEntityDataPacket<SmartBlockEntity> {


    public static final StreamCodec<ByteBuf, TransmissionRemovePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            TransmissionRemovePacket::new
    );
    
    public TransmissionRemovePacket(BlockPos pos) {
        super(pos);
    }

    


    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof AbstractEngineBlockEntity be) {
            be.highestSignal = 0;
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.TRANSMISSION_REMOVE;
    }
}