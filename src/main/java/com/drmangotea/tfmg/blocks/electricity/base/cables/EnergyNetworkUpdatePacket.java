package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class EnergyNetworkUpdatePacket extends BlockEntityDataPacket<SmartBlockEntity> {


    private long network;

    public EnergyNetworkUpdatePacket(BlockPos pos, long network) {
        super(pos);
        this.network = network;

    }

    public EnergyNetworkUpdatePacket(FriendlyByteBuf buffer) {
        super(buffer);
        network = buffer.readLong();

    }


    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeLong(network);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {
            be.setNetworkClient(network);

        }

    }


}
