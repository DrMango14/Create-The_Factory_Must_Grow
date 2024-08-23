package com.drmangotea.createindustry.blocks.electricity.voltmeter.energy_meter;

import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class EnergyMeterRenderer extends SafeBlockEntityRenderer<EnergyMeterBlockEntity> {

    public EnergyMeterRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(EnergyMeterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {






        BlockState blockState = be.getBlockState();
        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(0.5, 0.5, 0.5);

        float dialPivot = 5.75f / 16;

        float dialPivot2 = 5.75f / 12;

        SuperByteBuffer dial = CachedBufferer.partial(TFMGPartialModels.VOLTMETER_DIAL, blockState);

        Direction direction = blockState.getValue(FACING).getCounterClockWise();

        if(direction.getAxis() == Direction.Axis.X)
            direction = direction.getOpposite();

        dial
                .rotateY(direction.toYRot())
                .unCentre()
                .translate(0, dialPivot, dialPivot2)
                .rotateX(Math.abs( be.angle.getValue(partialTicks)))
                .translate(0, -dialPivot, -dialPivot2)
                .light(light);

        dial.renderInto(ms,vb);


        ms.popPose();

    }
}
