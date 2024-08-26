package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ConnectNeightborsPacket extends BlockEntityDataPacket<SmartBlockEntity> {




    public ConnectNeightborsPacket(BlockPos pos) {
        super(pos);


    }

    public ConnectNeightborsPacket(FriendlyByteBuf buffer) {
        super(buffer);


    }


    @Override
    protected void writeData(FriendlyByteBuf buffer) {}

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {
            be.connectNeighbors();

        }

    }


}
