package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank;



import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.backend.Backend;
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

public class PumpjackCrankRenderer extends KineticBlockEntityRenderer {

    public PumpjackCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        renderBlock((PumpjackCrankBlockEntity) te, ms, light,buffer);





        if (Backend.canUseInstancing(te.getLevel()))
            return;

        BlockState blockState = te.getBlockState();
        PumpjackCrankBlockEntity wte = (PumpjackCrankBlockEntity) te;



        float angle = wte.angle * partialTicks;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        renderCrank(te, ms, light, blockState, angle, vb);
    }

    private void renderCrank(KineticBlockEntity te, PoseStack ms, int light, BlockState blockState, float angle,
                             VertexConsumer vb) {

        /**
         * check
         */

        SuperByteBuffer hammer = CachedBufferer.block(blockState);
        //kineticRotationTransform(hammer, te, getRotationAxisOf(te), AngleHelper.rad(angle), light);
        hammer.renderInto(ms, vb);
    }
    private void renderBlock(PumpjackCrankBlockEntity te, PoseStack ms, int light,
                             MultiBufferSource buffer) {

        BlockState blockState = te.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

        float dialPivot = 5.75f / 16;


        if (te.direction == Direction.NORTH){
            CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CRANK_BLOCK, blockState)
                    //  .rotateY(d.toYRot())
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);

            if(te.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)

                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(te.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(te.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
    }
        if(te.direction == Direction.EAST) {
            CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CRANK_BLOCK, blockState)
                    .rotateY(270)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
            if(te.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(270)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(te.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(te.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
        }
        if(te.direction == Direction.SOUTH) {
            CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CRANK_BLOCK, blockState)
                    .rotateY(180)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);

            if(te.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(180)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(te.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(te.angle))
                        .unCentre()
                        .light(light)
                        .translateY(0.4)
                        .renderInto(ms, vb);
            }
        }
        if(te.direction == Direction.WEST) {
            CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CRANK_BLOCK, blockState)
                    .rotateY(90)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
            if(te.isValid()) {

                CachedBufferer.partial(TFMGPartialModels.PUMPJACK_CONNECTOR, blockState)
                        .rotateY(90)
                        .translate(-0.5, -0.75, -0.5)
                        .centre()
                        .rotate(Direction.WEST, -AngleHelper.rad(te.angle))
                        .unCentre()
                        .translateY(0.4)
                        .centre()
                        .rotate(Direction.WEST, AngleHelper.rad(te.angle))
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
        return shaft(getRotationAxisOf(te));
    }

}
