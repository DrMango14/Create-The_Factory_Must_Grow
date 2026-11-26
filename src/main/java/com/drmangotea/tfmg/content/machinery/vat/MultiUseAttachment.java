package com.drmangotea.tfmg.content.machinery.vat;

import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperationEntry;
import com.drmangotea.tfmg.registry.TFMGVatOperations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface MultiUseAttachment<BE extends IVatMachine> {
    VatOperationEntry getOperation();

    default VatOperationEntry getOperationId() {
        return isValid() ? getOperation() : TFMGVatOperations.NONE;
    }

    default boolean isValid() {
        return getOperation() != null && !getOperation().get().isNone();
    }

    void renderInVat(BE be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, @Nullable ItemRenderer itemRenderer);

    void tick(VatBlockEntity controllerVat, Level level, BlockPos pos, boolean active, boolean clientTick);
}
