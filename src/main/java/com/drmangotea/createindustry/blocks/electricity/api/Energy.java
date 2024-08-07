package com.drmangotea.createindustry.blocks.electricity.api;

import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class Energy implements TransferVariant<Double> {
    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public Double getObject() {
        return null;
    }

    @Override
    public @Nullable CompoundTag getNbt() {
        return null;
    }

    @Override
    public CompoundTag toNbt() {
        return null;
    }

    @Override
    public void toPacket(FriendlyByteBuf buf) {

    }
}
