package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank;




import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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






        BlockState blockState = te.getBlockState();
        PumpjackCrankBlockEntity be = (PumpjackCrankBlockEntity) te;



        float angle = be.angle * partialTicks;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        renderCrank(te, ms, light, blockState, angle, vb);
    }

    private void renderCrank(KineticBlockEntity te, PoseStack ms, int light, BlockState blockState, float angle,
                             VertexConsumer vb) {


        //SuperByteBuffer hammer = CachedBufferer.block(blockState);
        ////kineticRotationTransform(hammer, te, getRotationAxisOf(te), AngleHelper.rad(angle), light);
        //hammer.renderInto(ms, vb);
    }
    private void renderBlock(PumpjackCrankBlockEntity be, PoseStack ms, int light,
                             MultiBufferSource buffer) {

        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

        float dialPivot = 5.75f / 16;

        SuperByteBuffer crank = CachedBufferer.partialFacing(TFMGPartialModels.PUMPJACK_CRANK, blockState,blockState.getValue(FACING));





        crank
                .translate(-0.5, -0.5, -0.5)
                .centre()
               // .translate(0, -.25, 0)
                .rotate(be.angle-90,be.getBlockState().getValue(FACING).getCounterClockWise().getAxis())
                //.translate(0, .25, 0)
                .unCentre()

                .light(light);

        crank.renderInto(ms,vb);


        ms.popPose();
    }

    @Override
    protected BlockState getRenderedBlockState(KineticBlockEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}
