package com.drmangotea.tfmg.content.electricity.experimental.packets;


import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class UpdateVoltagePacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public static final StreamCodec<ByteBuf, UpdateVoltagePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            ByteBufCodecs.INT, packet -> packet.newVoltage,
            UpdateVoltagePacket::new
    );
    private final int newVoltage;

    public UpdateVoltagePacket(SmartBlockEntity be, int newVoltage) {
        super(be.getBlockPos());
        this.newVoltage = newVoltage;
    }
    private UpdateVoltagePacket(BlockPos pos,  int newVoltage) {
        super(pos);
        this.newVoltage = newVoltage;
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {
        int meow = 0;
        if (blockEntity instanceof IRealisticElectric be) {
            RealElectricNetworkManager.getNetwork(be.getWorld()).setVoltageGen(be, newVoltage);
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.UPDATE_VOLTAGE;
    }
}
