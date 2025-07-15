package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlock.CONTROLLER_TYPE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CokeOvenRenderer extends SafeBlockEntityRenderer<CokeOvenBlockEntity> {


    public CokeOvenRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(CokeOvenBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        BlockState blockState = be.getBlockState();

        if (be.getLevel() == null) return;

        if (be.getLevel().getBlockState(be.getBlockPos().relative(blockState.getValue(FACING))).is(TFMGBlocks.COKE_OVEN.get()))
            return;

        renderDoors(be, ms, buffer);
        renderFire(be, ms, buffer);

    }

    private void renderFire(CokeOvenBlockEntity be, PoseStack ms, MultiBufferSource buffer) {
        BlockState state = be.getBlockState();

        // Pick model variant based on controller type
        PartialModel fireModel = switch (state.getValue(CONTROLLER_TYPE)) {
            case TOP_ON -> TFMGPartialModels.COKE_OVEN_FIRE_TOP;
            case MIDDLE_ON -> TFMGPartialModels.COKE_OVEN_FIRE_MIDDLE;
            case BOTTOM_ON -> TFMGPartialModels.COKE_OVEN_FIRE_BOTTOM;
            default -> null;
        };

        if (fireModel == null)
            return;

        ms.pushPose();

        // Fullbright animated fire
        CachedBuffers.partial(fireModel, state)
                .light(LightTexture.FULL_BRIGHT)
                .center()
                .rotateYDegrees(state.getValue(FACING).getAxis() == Direction.Axis.Z ? Math.abs(state.getValue(FACING).toYRot()-180) : state.getValue(FACING).toYRot())
                .uncenter()
                .renderInto(ms, buffer.getBuffer(RenderType.translucent()));

        ms.popPose();
    }

    private void renderDoors(CokeOvenBlockEntity be, PoseStack ms, MultiBufferSource buffer) {

        BlockState state = be.getBlockState();
        int lightInFront = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(be.getBlockState().getValue(FACING)));


        PartialModel right_door = TFMGPartialModels.COKE_OVEN_DOOR_RIGHT;
        PartialModel left_door = TFMGPartialModels.COKE_OVEN_DOOR_LEFT;

        switch (state.getValue(CONTROLLER_TYPE)){
            case TOP_ON -> {
                right_door = TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_TOP;
                left_door = TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP;
            }
            case BOTTOM_ON ->{
                right_door = TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_BOTTOM;
                left_door = TFMGPartialModels.COKE_OVEN_DOOR_LEFT_BOTTOM;
            }
            case MIDDLE_ON ->{
                right_door = TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_MIDDLE;
                left_door = TFMGPartialModels.COKE_OVEN_DOOR_LEFT_MIDDLE;
            }
            case CASUAL -> {}
        }

        ms.pushPose();
        CachedBuffers.partial(right_door, state)
                .light(lightInFront)
                .center()
                .rotateYDegrees(state.getValue(FACING).getAxis() == Direction.Axis.Z ? Math.abs(state.getValue(FACING).toYRot()-180) : state.getValue(FACING).toYRot())
                .translateZ(-0.5f)
                .translateX(-0.5f)
                .rotateYDegrees(be.doorAngle.getValue())
                .translateZ(0.5f)
                .translateX(0.5f)
                .uncenter()
                .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        ms.popPose();
        ms.pushPose();
        CachedBuffers.partial(left_door, state)
                .light(lightInFront)
                .center()
                .rotateYDegrees(state.getValue(FACING).getAxis() == Direction.Axis.Z ? Math.abs(state.getValue(FACING).toYRot()-180) : state.getValue(FACING).toYRot())
                .translateZ(-0.5f)
                .translateX(0.5f)
                .rotateYDegrees(-be.doorAngle.getValue())
                .translateZ(0.5f)
                .translateX(-0.5f)
                .uncenter()
                .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        ms.popPose();
    }

}
