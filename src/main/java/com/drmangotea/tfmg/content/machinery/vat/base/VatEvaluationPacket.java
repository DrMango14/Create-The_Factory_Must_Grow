package com.drmangotea.tfmg.content.machinery.vat.base;


import com.drmangotea.tfmg.content.electricity.base.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class VatEvaluationPacket extends BlockEntityConfigurationPacket<SmartBlockEntity> {


    public static final StreamCodec<ByteBuf, VatEvaluationPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            VatEvaluationPacket::new
    );

    public VatEvaluationPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void applySettings(ServerPlayer player, SmartBlockEntity blockEntity) {
        if(blockEntity instanceof VatBlockEntity be) {
            be.evaluateNextTick =true;

        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.VAT_EVALUATION;
    }
}
