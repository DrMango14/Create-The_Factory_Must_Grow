package com.drmangotea.tfmg.content.electricity.measurement;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class VoltMeterRenderer extends SafeBlockEntityRenderer<VoltMeterBlockEntity> {

    public VoltMeterRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(VoltMeterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {



        BlockState blockState = be.getBlockState();
        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
        ms.pushPose();
        var msr = TransformStack.of(ms);
        msr.translate(0.5, 0.5, 0.5);

        float dialPivot = 5.75f / 16;

        float dialPivot2 = 5.75f / 12;

        SuperByteBuffer dial = CachedBuffers.partial(TFMGPartialModels.VOLTMETER_DIAL, blockState);

        Direction direction = blockState.getValue(FACING).getCounterClockWise();

        if(direction.getAxis() == Direction.Axis.X)
            direction = direction.getOpposite();
      //  TFMG.LOGGER.debug(String.valueOf(be.angle.getValue(partialTicks)));
        dial
                .rotateYDegrees(direction.toYRot())
                .uncenter()
                .translate(0, dialPivot, dialPivot2)
                .rotateXDegrees(Math.abs(Math.min( be.angle.getValue(partialTicks),180)))
                .translate(0, -dialPivot, -dialPivot2)
                .light(light);

        dial.renderInto(ms,vb);


        ms.popPose();

    }
}
