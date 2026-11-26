package com.drmangotea.tfmg.content.machinery.vat.base;


import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public class VatEvaluationPacket extends BlockEntityDataPacket<VatBlockEntity> {

    public static final StreamCodec<ByteBuf, VatEvaluationPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(
            VatEvaluationPacket::new, packet -> packet.pos
    );

    public VatEvaluationPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void handlePacket(VatBlockEntity blockEntity) {
        blockEntity.evaluateNextTick = true;
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.VAT_EVALUATION;
    }
}
