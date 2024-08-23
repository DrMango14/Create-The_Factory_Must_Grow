package com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace;



import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceRenderer extends SafeBlockEntityRenderer<BlastFurnaceOutputBlockEntity> {

    public BlastFurnaceRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(BlastFurnaceOutputBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

            float coalCokeLevel = be.coalCokeHeight.getValue()/64;

        int lightInside = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above().relative(be.getBlockState().getValue(FACING).getOpposite()));

        if(be.timer<=0)
            if(be.validHeight >=4) {

                if(be.getBlockState().getValue(FACING)== Direction.NORTH)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, coalCokeLevel,Direction.NORTH);

                if(be.getBlockState().getValue(FACING)== Direction.SOUTH)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, coalCokeLevel,Direction.SOUTH);

                if(be.getBlockState().getValue(FACING)== Direction.WEST)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, coalCokeLevel,Direction.WEST);

                if(be.getBlockState().getValue(FACING)== Direction.EAST)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, coalCokeLevel,Direction.EAST);






        }

        ms.popPose();

    }

    protected void renderPile(BlastFurnaceOutputBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay, float height,Direction direction) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        //small
        if(height!=0) {
            int angle = 0;

            if(direction == Direction.SOUTH)
                angle = 180;
            if(direction == Direction.WEST)
                angle = 90;
            if(direction == Direction.EAST)
                angle = 270;



            if (be.type == BlastFurnaceOutputBlockEntity.BlastFurnaceType.SMALL) {
                CachedBufferer.partial(TFMGPartialModels.COAL_COKE_DUST_LAYER, blockState)
                        .rotateY(angle)
                        .centre()
                        .translateX(-1)
                        .translateY(height)
                        .light(light)
                        .renderInto(ms, vb);
            }




            }






    }

    @Override
    public boolean shouldRenderOffScreen(BlastFurnaceOutputBlockEntity te) {
        return true;
    }

}
