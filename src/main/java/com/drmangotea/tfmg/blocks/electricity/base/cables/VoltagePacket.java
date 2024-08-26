package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class VoltagePacket extends BlockEntityDataPacket<SmartBlockEntity> {

    int voltage=0;


    public VoltagePacket(BlockPos pos) {
        super(pos);
        this.voltage = voltage;

    }

    public VoltagePacket(FriendlyByteBuf buffer) {
        super(buffer);
        buffer.readInt();

    }


    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeInt(voltage);
    }

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {

            be.getOrCreateElectricNetwork().updateNetworkVoltage();



        }

    }


}
