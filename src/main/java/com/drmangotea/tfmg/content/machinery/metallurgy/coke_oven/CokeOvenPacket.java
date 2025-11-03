package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class CokeOvenPacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, CokeOvenPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            CokeOvenPacket::new
    );

    public CokeOvenPacket(BlockPos pos) {
        super(pos);
    }


    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof CokeOvenBlockEntity be) {
            be.onPlaced();
        }

    }


    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.COKE_OVEN_PACKET;
    }
}
