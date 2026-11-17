package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SegmentedDisplayRenderer extends SafeBlockEntityRenderer<SegmentedDisplayBlockEntity> {

    public SegmentedDisplayRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(SegmentedDisplayBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int l, int overlay) {

        BlockState blockState = be.getBlockState();
		Direction facing = blockState.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
		int light = LightTexture.pack(15, 15);
        VertexConsumer vb = bufferSource.getBuffer(RenderType.cutoutMipped());
        ms.pushPose();

        int color =  be.color.getTextColor();
		
		be.segmentsToRender.forEach(list -> list.forEach(seg ->
            CachedBuffers.partialFacing(getSegment(seg), blockState, facing)
				.light(light)
				.color(color)
				.renderInto(ms,vb)
		));

        ms.popPose();
    }



    public PartialModel getSegment(int id){
        return TFMGPartialModels.SEGMENTS.get(Math.min(id,20));
    }





}
