package com.drmangotea.tfmg.content.engines.types.large_engine;


import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class LargeEngineRenderer extends SafeBlockEntityRenderer<LargeEngineBlockEntity> {

    public LargeEngineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(LargeEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        //if (Backend.canUseInstancing(te.getLevel()))
        //	return;

        Float angle = be.getTargetAngle();
        if (angle == null)
            return;

        BlockState blockState = be.getBlockState();
        Direction facing = LargeEngineBlock.getFacing(blockState);
        Axis facingAxis = facing.getAxis();
        Axis axis = Axis.Y;

        PoweredShaftBlockEntity shaft = be.getShaft();
        if (shaft != null)
            axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);

        boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
        float sine = Mth.sin(angle);
        float sine2 = Mth.sin(angle - Mth.HALF_PI);
        float piston = ((1 - sine) / 4) * 24 / 16f;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());


        transformed(be.isSimpleEngine() ? TFMGPartialModels.SIMPLE_LARGE_ENGINE_PISTON : TFMGPartialModels.LARGE_ENGINE_PISTON, blockState, facing, roll90)
                .translate(0, piston, 0)
                .light(light)
                .renderInto(ms, vb);


        transformed(be.isSimpleEngine() ? TFMGPartialModels.SIMPLE_LARGE_ENGINE_LINKAGE : TFMGPartialModels.LARGE_ENGINE_LINKAGE, blockState, facing, roll90)
                .center()
                .translate(0, 1, 0)
                .uncenter()
                .translate(0, piston, 0)
                .translate(0, 4 / 16f, 8 / 16f)
                .rotateXDegrees(sine2 * 23f)
                .translate(0, -4 / 16f, -8 / 16f)
                .light(light)
                .renderInto(ms, vb);

        transformed(AllPartialModels.ENGINE_CONNECTOR, blockState, facing, roll90)
                .translate(0, 2, 0)
                .center()
                .rotateX(-angle + Mth.HALF_PI)
                .uncenter()
                .light(light)
                .renderInto(ms, vb);


    }

    private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing, boolean roll90) {
        return CachedBuffers.partial(model, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(facing))
                .rotateXDegrees(AngleHelper.verticalAngle(facing) + 90)
                .rotateYDegrees(roll90 ? -90 : 0)
                .uncenter();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

}