package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;

import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;


public class StatorRenderer extends SafeBlockEntityRenderer<StatorBlockEntity> {

    public StatorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(StatorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ms.pushPose();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        SuperByteBuffer output = CachedBufferer.partialFacing(TFMGPartialModels.STATOR_OUTPUT, be.getBlockState(),be.getBlockState().getValue(FACING).getOpposite());
        if(be.hasOutput)
            output.renderInto(ms, vb);

        ms.popPose();
    }
}
