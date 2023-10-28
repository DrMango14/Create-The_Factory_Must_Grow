package com.drmangotea.createindustry.blocks.engines.small;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class EngineBackRenderer extends SafeBlockEntityRenderer<EngineBackBlockEntity> {

    public EngineBackRenderer(BlockEntityRendererProvider.Context context) {}


    @Override
    protected void renderSafe(EngineBackBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

    }
}

