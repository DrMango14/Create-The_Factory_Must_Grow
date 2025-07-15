package com.drmangotea.tfmg.content.machinery.misc.air_intake;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class AirIntakeRenderer  extends KineticBlockEntityRenderer<AirIntakeBlockEntity> {


    public AirIntakeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


        @Override
        protected void renderSafe(AirIntakeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                  int light, int overlay) {

            Direction direction = be.getBlockState()
                    .getValue(FACING);
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

            SuperByteBuffer frame;
            if (be.hasShaft) {
                frame = CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_FRAME, be.getBlockState(), direction).light(light);
            } else
                frame = CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_FRAME_CLOSED, be.getBlockState(), direction).light(light);
            ///
            if (direction == Direction.NORTH)
                frame.translateZ(0.001f);
            if (direction == Direction.SOUTH)
                frame.translateZ(-0.001f);
            if (direction == Direction.WEST)
                frame.translateX(0.001f);
            if (direction == Direction.EAST)
                frame.translateX(-0.001f);
            if (direction == Direction.UP)
                frame.translateY(-0.001f);
            if (direction == Direction.DOWN)
                frame.translateY(0.001f);

            frame.renderInto(ms, vb);

            ///
            if (be.diameter == 2) {
                SuperByteBuffer chassisMedium = CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_MEDIUM, be.getBlockState(), direction).light(light);
                if(direction == Direction.UP)
                    chassisMedium.translateZ(1);
                if(direction.getAxis().isVertical())
                    chassisMedium.translateX(1);


               chassisMedium.renderInto(ms, vb);
            }

            if (be.diameter == 3) {
                SuperByteBuffer chassisLarge = CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_LARGE, be.getBlockState(), direction).light(light);

                if (direction.getAxis().isHorizontal()) {
                    chassisLarge.translateY(1);
                    if (direction == Direction.NORTH)
                        chassisLarge.translateX(1);
                    if (direction == Direction.SOUTH)
                        chassisLarge.translateX(-1);
                    if (direction == Direction.EAST)
                        chassisLarge.translateZ(1);
                    if (direction == Direction.WEST)
                        chassisLarge.translateZ(-1);
                } else {
                    chassisLarge.translateZ(1);

                    chassisLarge.translateX(1);


                }

                chassisLarge.renderInto(ms, vb);

            }


            /////
            int lightBehind = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(direction.getOpposite()));
            int lightInFront = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(direction));

            SuperByteBuffer shaftHalf =
                    CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction.getOpposite());




                SuperByteBuffer fanInner =
                        CachedBuffers.partialFacing(AllPartialModels.ENCASED_FAN_INNER, be.getBlockState(), direction.getOpposite());



            if (be.diameter == 2) {
                fanInner =
                        CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_FAN_MEDIUM, be.getBlockState(), direction.getOpposite());
            }

            if (be.diameter == 3) {
                fanInner =
                        CachedBuffers.partialFacing(TFMGPartialModels.AIR_INTAKE_FAN_LARGE, be.getBlockState(), direction.getOpposite());

            }


            if(direction.getAxis().isHorizontal()) {
                float x = 0;
                float z = 0;
                if(direction == Direction.NORTH)
                    x=1f;
                if(direction == Direction.SOUTH)
                    x=-1f;
                if(direction == Direction.WEST)
                    z=-1f;
                if(direction == Direction.EAST)
                    z=1f;

                if(be.diameter==3)
                    fanInner.translate(x,1,z);
                if(be.diameter==2)
                    fanInner.translate(x/2,0.5,z/2);
            }else {
                if(be.diameter==2)
                    fanInner.translate(0.5,0,0.5);
                if(be.diameter==3)
                    fanInner.translate(1,0,1);
            }


            float time = AnimationTickHolder.getRenderTime(be.getLevel());
            float speed = be.maxShaftSpeed * 2;
            if (speed > 0)
                speed = Mth.clamp(speed, 80, 64 * 20);
            if (speed < 0)
                speed = Mth.clamp(speed, -64 * 20, -80);
            float angle = (time * speed * 3 / 10f) % 360;
            angle = angle / 180f * (float) Math.PI;

            standardKineticRotationTransform(shaftHalf, be, lightBehind).renderInto(ms, vb);
            if(!be.isUsedByController)
                kineticRotationTransform(fanInner, be, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);
        }


    }