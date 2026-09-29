package com.drmangotea.tfmg.content.electricity.experimental.packets;


import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class AddElectricalComponentPacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, AddElectricalComponentPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            AddElectricalComponentPacket::new
    );


    public AddElectricalComponentPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if (blockEntity instanceof IRealisticElectric be) {
            be.onPlace();
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.ADD_ELECTRICAL_COMPONENT;
    }
}
