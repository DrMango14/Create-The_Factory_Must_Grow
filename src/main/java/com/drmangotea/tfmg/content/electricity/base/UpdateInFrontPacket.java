package com.drmangotea.tfmg.content.electricity.base;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class UpdateInFrontPacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, UpdateInFrontPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            UpdateInFrontPacket::new
    );

    public UpdateInFrontPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if (blockEntity instanceof VoltageAlteringBlockEntity be) {
            be.updateInFrontNextTick();

        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.UPDATE_IN_FRONT_PACKET;
    }
}
