package com.drmangotea.tfmg.content.electricity.base;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ElectricPlacementPacket extends BlockEntityDataPacket<SmartBlockEntity> {




    public ElectricPlacementPacket(BlockPos pos) {
        super(pos);


    }

    public ElectricPlacementPacket(FriendlyByteBuf buffer) {
        super(buffer);


    }


    @Override
    protected void writeData(FriendlyByteBuf buffer) {}

    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof IElectric be) {
            be.getData().connectNextTick = true;
        }

    }


}
