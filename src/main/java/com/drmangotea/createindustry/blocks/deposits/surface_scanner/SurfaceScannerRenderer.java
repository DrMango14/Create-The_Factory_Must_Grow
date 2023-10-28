package com.drmangotea.createindustry.blocks.deposits.surface_scanner;

import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class SurfaceScannerRenderer extends KineticBlockEntityRenderer<SurfaceScannerBlockEntity> {

    public SurfaceScannerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

    }


    @Override
    protected SuperByteBuffer getRotatedModel(SurfaceScannerBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                .getValue(HorizontalKineticBlock.HORIZONTAL_FACING)
                .getOpposite());
    }

    @Override
    protected void renderSafe(SurfaceScannerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();




      CachedBufferer.partial(TFMGPartialModels.SURFACE_SCANNER_FLAG, blockState)
              .translateY(0.25)
              .centre()
              .rotateToFace(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING))
              .rotateX(-be.visualFlagAngle.getValue(partialTicks))
              .unCentre()

              .light(light)
              .renderInto(ms, vb);
        ///


            if(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)==Direction.NORTH) {

                CachedBufferer.partial(TFMGPartialModels.SURFACE_SCANNER_DIAL, blockState)

                        .translateY(-0.07)
                        .translateZ(.065)
                        .centre()
                        .rotateY(be.visualAngle.getValue(partialTicks))
                        .unCentre()
                        .light(light)
                        .renderInto(ms, vb);
            }
        if(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)==Direction.SOUTH) {

            CachedBufferer.partial(TFMGPartialModels.SURFACE_SCANNER_DIAL, blockState)

                    .translateY(-0.08)
                    .translateZ(-.065)
                    .centre()
                    .rotateY(be.visualAngle.getValue(partialTicks))
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }
        if(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)==Direction.WEST) {

            CachedBufferer.partial(TFMGPartialModels.SURFACE_SCANNER_DIAL, blockState)

                    .translateY(-0.08)
                    .translateX(.065)
                    .centre()
                    .rotateY(be.visualAngle.getValue(partialTicks))
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }
        if(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING)==Direction.EAST) {

            CachedBufferer.partial(TFMGPartialModels.SURFACE_SCANNER_DIAL, blockState)

                    .translateY(-0.08)
                    .translateX(-.065)
                    .centre()
                    .rotateY(be.visualAngle.getValue(partialTicks))
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }

    }


}