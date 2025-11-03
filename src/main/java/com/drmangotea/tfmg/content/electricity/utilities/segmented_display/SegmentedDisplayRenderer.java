package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import com.drmangotea.tfmg.registry.TFMGPartialModels;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SegmentedDisplayRenderer extends SafeBlockEntityRenderer<SegmentedDisplayBlockEntity> {

    public SegmentedDisplayRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(SegmentedDisplayBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        VertexConsumer vb = bufferSource.getBuffer(RenderType.cutoutMipped());
        ms.pushPose();
        var msr = TransformStack.of(ms);
    //    msr.translate(0.5, 0.5, 0.5);

        int color =  be.color.getTextColor();

        for(int i =0;i<be.segmentsToRender.size();i++){

            CachedBuffers.partialFacing(getSegment(be.segmentsToRender.get(i)-10), blockState,blockState.getValue(HorizontalDirectionalBlock.FACING).getOpposite())
                    .light(999999)
                    .color(color)
                    .renderInto(ms,vb);
        }

        for(int i =0;i<be.segmentsToRender2.size();i++){

            CachedBuffers.partialFacing(getSegment(be.segmentsToRender2.get(i)-10), blockState,blockState.getValue(HorizontalDirectionalBlock.FACING).getOpposite())
                    .light(999999)
                    .color(color)
                    .renderInto(ms,vb);
        }



        ms.popPose();
    }



    public PartialModel getSegment(int id){
        return TFMGPartialModels.SEGMENTS.get(Math.min(id,20));
    }





}
