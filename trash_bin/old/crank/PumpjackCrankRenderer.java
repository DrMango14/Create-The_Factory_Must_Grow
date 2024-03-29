package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.old.crank;



import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackCrankRenderer extends KineticBlockEntityRenderer {

    public PumpjackCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        renderBlock((PumpjackCrankBlockEntity) te, ms, light,buffer);





       // if (Backend.canUseInstancing(te.getLevel()))
       //     return;

        BlockState blockState = te.getBlockState();
        PumpjackCrankBlockEntity wte = (PumpjackCrankBlockEntity) te;



        float angle = wte.angle * partialTicks;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        renderCrank(te, ms, light, blockState, angle, vb);
    }

    private void renderCrank(KineticBlockEntity te, PoseStack ms, int light, BlockState blockState, float angle,
                             VertexConsumer vb) {


        SuperByteBuffer hammer = CachedBufferer.block(blockState);
        //kineticRotationTransform(hammer, te, getRotationAxisOf(te), AngleHelper.rad(angle), light);
        hammer.renderInto(ms, vb);
    }
    private void renderBlock(PumpjackCrankBlockEntity be, PoseStack ms, int light,
                             MultiBufferSource buffer) {

        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

        float dialPivot = 5.75f / 16;

        SuperByteBuffer crank = CachedBufferer.partialFacing(TFMGPartialModels.PUMPJACK_CRANK, blockState,blockState.getValue(HorizontalDirectionalBlock.FACING));
        CachedBufferer.partialFacing(TFMGPartialModels.PUMPJACK_CRANK_BLOCK, blockState,blockState.getValue(HorizontalDirectionalBlock.FACING))
                .translate(-0.5, -0.5, -0.5)
                .light(light)
                .renderInto(ms,vb);


        crank
                .translate(-0.5, -0.5, -0.5)
                .centre()
                .translate(0, -.25, 0)
                .rotate(be.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise(), -AngleHelper.rad(be.angle))
                .translate(0, .25, 0)
                .unCentre()

                .light(light);

        crank.renderInto(ms,vb);



        if (be.direction == Direction.NORTH){


            if(be.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)

                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(be.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(be.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
    }
        if(be.direction == Direction.EAST) {

            if(be.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(270)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(be.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(be.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
        }
        if(be.direction == Direction.SOUTH) {


            if(be.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(180)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(be.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(be.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
        }
        if(be.direction == Direction.WEST) {

            if(be.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(90)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(be.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(be.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
        }


        ms.popPose();
    }

    @Override
    protected BlockState getRenderedBlockState(KineticBlockEntity te) {
        return KineticBlockEntityRenderer.shaft(KineticBlockEntityRenderer.getRotationAxisOf(te));
    }

}
