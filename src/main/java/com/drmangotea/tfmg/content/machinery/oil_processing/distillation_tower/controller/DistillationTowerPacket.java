package com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller;


import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.compat.computercraft.AttachedComputerPacket;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class DistillationTowerPacket extends BlockEntityDataPacket<SmartBlockEntity> {

    public boolean assemble;

    public BlockPos posToUpdate;


    public static final StreamCodec<ByteBuf, DistillationTowerPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            BlockPos.STREAM_CODEC, packet -> packet.posToUpdate,
            ByteBufCodecs.BOOL, packet -> packet.assemble,
            DistillationTowerPacket::new
    );
    public DistillationTowerPacket(BlockPos pos,BlockPos posToUpdate,boolean assemble) {
        super(pos);
        this.assemble = assemble;
        this.posToUpdate = posToUpdate;
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof DistillationControllerBlockEntity be) {
            SteelTankBlock.updateTowerState(be.getLevel(), posToUpdate, assemble, false);
        }

    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.DISTILLATION_PACKET;
    }
}
