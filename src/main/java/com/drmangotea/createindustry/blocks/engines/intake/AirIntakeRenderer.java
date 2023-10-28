package com.drmangotea.createindustry.blocks.engines.intake;

import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
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
            if (Backend.canUseInstancing(be.getLevel())) return;


            Direction direction = be.getBlockState()
                    .getValue(FACING);
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

            SuperByteBuffer frame;
            if (be.hasShaft) {
                frame = CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_FRAME, be.getBlockState(), direction);
            } else
                frame = CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_FRAME_CLOSED, be.getBlockState(), direction);
            ///
            if (direction == Direction.NORTH)
                frame.translateZ(0.001);
            if (direction == Direction.SOUTH)
                frame.translateZ(-0.001);
            if (direction == Direction.WEST)
                frame.translateX(0.001);
            if (direction == Direction.EAST)
                frame.translateX(-0.001);
            if (direction == Direction.UP)
                frame.translateY(-0.001);
            if (direction == Direction.DOWN)
                frame.translateY(0.001);

            frame.renderInto(ms, vb);

            ///
            if (be.diameter == 2) {
                SuperByteBuffer chassisMedium = CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_MEDIUM, be.getBlockState(), direction);
                if(direction == Direction.UP)
                    chassisMedium.translateZ(1);
                if(direction.getAxis().isVertical())
                    chassisMedium.translateX(1);


               chassisMedium.renderInto(ms, vb);
            }

            if (be.diameter == 3) {
                SuperByteBuffer chassisLarge = CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_LARGE, be.getBlockState(), direction);

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
                    CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction.getOpposite());




                SuperByteBuffer fanInner =
                        CachedBufferer.partialFacing(AllPartialModels.ENCASED_FAN_INNER, be.getBlockState(), direction.getOpposite());



            if (be.diameter == 2) {
                fanInner =
                        CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_FAN_MEDIUM, be.getBlockState(), direction.getOpposite());
            }

            if (be.diameter == 3) {
                fanInner =
                        CachedBufferer.partialFacing(TFMGPartialModels.AIR_INTAKE_FAN_LARGE, be.getBlockState(), direction.getOpposite());

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