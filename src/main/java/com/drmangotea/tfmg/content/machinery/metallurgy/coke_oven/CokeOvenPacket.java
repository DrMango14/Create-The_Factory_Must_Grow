package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class CokeOvenPacket extends BlockEntityDataPacket<SmartBlockEntity> {
    public CokeOvenPacket(BlockPos pos) {
        super(pos);
    }
    public CokeOvenPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }
    @Override
    protected void writeData(FriendlyByteBuf buffer) {}
    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {

        if(blockEntity instanceof CokeOvenBlockEntity be) {
            be.onPlaced();
        }

    }


}
