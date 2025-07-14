package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.crank;





import com.drmangotea.tfmg.registry.TFMGPartialModels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackCrankRenderer extends KineticBlockEntityRenderer<PumpjackCrankBlockEntity> {

    public PumpjackCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(PumpjackCrankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        var msr = TransformStack.of(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);
        CachedBuffers.partialFacing(TFMGPartialModels.PUMPJACK_CRANK, blockState,blockState.getValue(FACING))
                .translate(-0.5, -0.5, -0.5)
                .center()
                .rotateDegrees(be.angle-90,be.getBlockState().getValue(FACING).getCounterClockWise().getAxis())
                .uncenter()
                .light(light)
                .renderInto(ms,vb);
        ms.popPose();
    }

    @Override
    protected BlockState getRenderedBlockState(PumpjackCrankBlockEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}
