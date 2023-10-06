package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base;


import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class PumpjackBaseRenderer extends SafeBlockEntityRenderer<PumpjackBaseBlockEntity> {

    public PumpjackBaseRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(PumpjackBaseBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        BlockState blockState = te.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);
        float dialPivot = 5.75f / 16;
            if(te.isComplete()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_FRONT_ROPE, blockState)
                        //  .rotateY(d.toYRot())
                        .unCentre()
                        .translateY(1)
                        .light(light)
                        .renderInto(ms, vb);
            }



        ms.popPose();

    }


    @Override
    public boolean shouldRenderOffScreen(PumpjackBaseBlockEntity te) {
        return false;
    }

}
