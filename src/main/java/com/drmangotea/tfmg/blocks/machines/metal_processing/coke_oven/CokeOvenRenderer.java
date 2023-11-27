package com.drmangotea.tfmg.blocks.machines.metal_processing.coke_oven;




import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CokeOvenRenderer extends SafeBlockEntityRenderer<CokeOvenBlockEntity> {

    public CokeOvenRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(CokeOvenBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {



        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);
        int lightInFront = Math.max(
                LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(be.getBlockState().getValue(FACING))),
                Math.max(
                        LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().below().relative(be.getBlockState().getValue(FACING))),
                        LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above().relative(be.getBlockState().getValue(FACING)))
                )
        )
                ;
        if(be.isController) {

          //  CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT, blockState)
          //          .centre()
          //          .translate(-1,-1,-1)
          //          .rotateY(-be.visualDoorAngle.getValue())
          //          .light(light)
          //          .renderInto(ms, vb);
          //  CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP, blockState)
          //          .centre()
          //          .translate(-1,0,-1)
          //          .rotateY(-be.visualDoorAngle.getValue())
          //          .light(light)
          //          .renderInto(ms, vb);


            if(be.getBlockState().getValue(FACING)== Direction.SOUTH) {
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT, blockState)
                        .centre()
                        .rotateY(be.visualDoorAngle.getValue())
                        .translate(-1, -2, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_TOP, blockState)
                        .centre()
                        .rotateY(be.visualDoorAngle.getValue())
                        .translate(-1, -1, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);



                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT, blockState)
                        .centre()
                        .translateX(-1)
                        .rotateY(-be.visualDoorAngle.getValue())
                        .translate(0, -2, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP, blockState)
                        .centre()
                        .translateX(-1)
                        .rotateY(-be.visualDoorAngle.getValue())
                        .translate(0, -1, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);


            }
            if(be.getBlockState().getValue(FACING)== Direction.NORTH) {
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT, blockState)
                        .centre()
                        .translateZ(-1.01)
                        .rotateY(-be.visualDoorAngle.getValue())
                        .translate(-1, -2, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_TOP, blockState)
                        .centre()
                        .translateZ(-1.01)
                        .rotateY(-be.visualDoorAngle.getValue())
                        .translate(-1, -1, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);



                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT, blockState)
                        .centre()
                        .translateZ(-1.01)
                        .translateX(-1)
                        .rotateY(be.visualDoorAngle.getValue())
                        .translate(0, -2, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP, blockState)
                        .centre()
                        .translateZ(-1.01)
                        .translateX(-1)
                        .rotateY(be.visualDoorAngle.getValue())
                        .translate(0, -1, -1)
                        .light(lightInFront)
                        .renderInto(ms, vb);


            }
            if(be.getBlockState().getValue(FACING)== Direction.WEST) {
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT, blockState)
                        .centre()
                        .translateX(-1)
                        .translateZ(-1)
                        .translateY(-2)
                        .rotateY(-be.visualDoorAngle.getValue()+90)
                        .translateZ(-1.01)
                        .translateX(-1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_TOP, blockState)
                        .centre()
                        .translateX(-1)
                        .translateY(-1)
                        .translateZ(-1)
                        .rotateY(-be.visualDoorAngle.getValue()+90)
                        .translateZ(-1.01)
                        .translateX(-1)
                        .light(lightInFront)
                        .renderInto(ms, vb);


                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT, blockState)
                        .centre()
                        .translateX(-1)
                        .translateY(-2)
                        .rotateY(be.visualDoorAngle.getValue()+90)
                        .translateZ(-1.01)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP, blockState)
                        .centre()
                        .translateX(-1)
                        .translateY(-1)
                        .rotateY(be.visualDoorAngle.getValue()+90)
                        .translateZ(-1.01)
                        .light(lightInFront)
                        .renderInto(ms, vb);



            }
            if(be.getBlockState().getValue(FACING)== Direction.EAST) {
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT, blockState)
                        .centre()
                        //.translateX(-1)
                        .translateZ(-1)
                        .translateY(-2)
                        .rotateY(be.visualDoorAngle.getValue()+90)
                        .translateZ(-0.99)
                        .translateX(-1)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_RIGHT_TOP, blockState)
                        .centre()
                        //.translateX(-1)
                        .translateY(-1)
                        .translateZ(-1)
                        .rotateY(be.visualDoorAngle.getValue()+90)
                        .translateZ(-0.99)
                        .translateX(-1)
                        .light(lightInFront)
                        .renderInto(ms, vb);


                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT, blockState)
                        .centre()
                        //.translateX(-1)
                        .translateY(-2)
                        .rotateY(-be.visualDoorAngle.getValue()+90)
                         .translateZ(-0.99)
                        .light(lightInFront)
                        .renderInto(ms, vb);
                CachedBufferer.partial(TFMGPartialModels.COKE_OVEN_DOOR_LEFT_TOP, blockState)
                        .centre()
                        //  .translateX(-1)
                        .translateY(-1)
                        .rotateY(-be.visualDoorAngle.getValue()+90)
                           .translateZ(-0.99)
                        .light(lightInFront)
                        .renderInto(ms, vb);



            }






        }



        ms.popPose();

    }
    @Override
    public int getViewDistance() {
        return 128;
    }




}