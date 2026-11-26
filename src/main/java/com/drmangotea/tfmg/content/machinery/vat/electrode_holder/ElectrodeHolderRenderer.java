package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrodeHolderRenderer extends SafeBlockEntityRenderer<ElectrodeHolderBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ElectrodeHolderRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    protected void renderSafe(ElectrodeHolderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        BlockState blockState = be.getBlockState();


        if (!be.electrode.isValid())
            return;
        if (be.getLevel() == null)
            return;

        be.electrode.renderInVat(be, partialTicks, ms, buffer, light, overlay, itemRenderer);
    }

}



