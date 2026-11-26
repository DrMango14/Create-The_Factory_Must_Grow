package com.drmangotea.tfmg.content.machinery.vat.base.registry.operations;

import com.drmangotea.tfmg.registry.TFMGVatOperations;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record VatOperation(ResourceLocation id) {
    public static final Codec<VatOperation> CODEC =
            ResourceLocation.CODEC.xmap(
                    VatOperation::new,
                    VatOperation::id
            ).stable();

    public static final StreamCodec<ByteBuf, VatOperation> STREAM_CODEC =
            ResourceLocation.STREAM_CODEC.map(
                    VatOperation::new,
                    VatOperation::id
            );

    public boolean isNone() {
        return this == TFMGVatOperations.NONE.get();
    }

    public boolean is(VatOperation operation) {
        return this == operation;
    }
}
