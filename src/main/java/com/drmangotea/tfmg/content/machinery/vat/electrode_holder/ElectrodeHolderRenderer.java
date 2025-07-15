package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrodeHolderRenderer extends SafeBlockEntityRenderer<ElectrodeHolderBlockEntity> {


    public ElectrodeHolderRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(ElectrodeHolderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {


        BlockState blockState = be.getBlockState();


        if (be.electrodeType == ElectrodeHolderBlockEntity.ElectrodeType.NONE)
            return;
        if (be.electrodeType.model == null)
            return;


        CachedBuffers.partial(be.electrodeType.model, blockState)
                .light(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().below()))
                .translateY(-1)
                .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

}



