package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.base.TFMGUtils.toYRot;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class EngineControllerRenderer extends SafeBlockEntityRenderer<EngineControllerBlockEntity> {

    public EngineControllerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(EngineControllerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(FACING);

        boolean move = be.getBlockPos() == EngineControllerClientHandler.controllerPos;


        ms.pushPose();

        CachedBuffers.partial(TFMGPartialModels.STEERING_WHEEL, state)
                .center()
                .rotateYDegrees(toYRot(facing))
                .translateY(0.1f)
                .rotateXDegrees(30)
                .rotateZDegrees(move ? be.steeringWheelAngle.getValue(partialTicks) : 0)
                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

        CachedBuffers.partial(TFMGPartialModels.PEDAL, state)
                .center()
                .rotateYDegrees(toYRot(facing))
                .translateX(-4/16f)
                .translateZ(move ? be.gasPedalMotion.getValue(partialTicks) : 0)
                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
        CachedBuffers.partial(TFMGPartialModels.PEDAL, state)
                .center()
                .rotateYDegrees(toYRot(facing))
                .translateX(-0/16f)
                .translateZ(move ? be.brakePedalMotion.getValue(partialTicks) : 0)

                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
        CachedBuffers.partial(TFMGPartialModels.PEDAL, state)
                .center()
                .rotateYDegrees(toYRot(facing))
                .translateX(4/16f)
                .translateZ(move ? be.clutchPedalMotion.getValue(partialTicks) : 0)

                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
        CachedBuffers.partial(TFMGPartialModels.TRANSMISSION_LEVER,state)
                .center()
                .rotateYDegrees(toYRot(facing))
                .translateY(-4/16f)
                //.translateZ(5/16f)
                .rotateXDegrees(be.transmissionLeverAngle.getValue(partialTicks))
                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));




        CachedBuffers.partial(TFMGPartialModels.ENGINE_CONTROLLER_DIAL, state)
                .center()

                .rotateYDegrees(toYRot(facing))
                .rotateXDegrees(22.5f)
                .translateX(2.5f/16f)
                .translateZ(6.7f/16f)
                .translateY(5.3f/16f)
                .rotateZDegrees(be.fuelDial.getValue(partialTicks)-90)
                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

        CachedBuffers.partial(TFMGPartialModels.ENGINE_CONTROLLER_DIAL, state)
                .center()

                .rotateYDegrees(toYRot(facing))
                .rotateXDegrees(22.5f)
                .translateX(-2.5f/16f)
                .translateZ(6.7f/16f)
                .translateY(5.3f/16f)
                .rotateZDegrees(be.rpmDial.getValue(partialTicks)-90)
                .uncenter()
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
        ms.popPose();


    }
}
