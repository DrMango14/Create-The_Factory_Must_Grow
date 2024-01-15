package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;


public class PumpjackRenderer extends KineticBlockEntityRenderer {





    public PumpjackRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {


        if(((PumpjackBlockEntity)be).crank == null)
            return;
        if(((PumpjackBlockEntity)be).base == null)
            return;
        if(!((PumpjackBlockEntity) be).running)
            return;


        renderPumpjackLink(
                false,
                ms,
                buffer,
                (PumpjackBlockEntity) be
        );
        renderPumpjackLink(
                true,
                ms,
                buffer,
                (PumpjackBlockEntity) be
        );
        renderFrontPumpjackLink(
                ms,
                buffer,
                (PumpjackBlockEntity) be
        );




    }
    private void renderPumpjackLink(boolean second, PoseStack pMatrixStack, MultiBufferSource pBuffer, PumpjackBlockEntity be) {
        pMatrixStack.pushPose();
        Direction direction = be.getBlockState().getValue(FACING);



      Vec3 vec3 = new Vec3(0,0,0);

       // vec3 = vec3.subtract(1,0,1);

        int q = 1;
        if(be.connectorAtFront)
            q = -1;

        float hModifier = 0;
        float x=0;
        float y=0;
        if(be.crank!=null) {
            hModifier = be.crank.heightModifier - be.crankConnectorDistance;
            float linkLenght =    be.crankConnectorDistance;



            if(direction == Direction.WEST) {
                if ((be.crank.angle>0&&be.crank.angle < 90||be.crank.angle > 270)||(be.crank.angle<0&&be.crank.angle > -90||be.crank.angle < -270)) {
                    x = (float) Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));
               } else
                   x = (float) -Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));


                y = (float) (be.connectorDistance - Math.sqrt(Math.pow(be.connectorDistance, 2) - Math.pow(be.crank.heightModifier, 2)));
            }
            if(direction == Direction.EAST) {

                if ((be.crank.angle>0&&be.crank.angle < 90||be.crank.angle > 270)||(be.crank.angle<0&&be.crank.angle > -90||be.crank.angle < -270)) {
                    x = (float) Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));
                } else
                    x = (float) -Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));


                y = (float) (be.connectorDistance - Math.sqrt(Math.pow(be.connectorDistance, 2) - Math.pow(be.crank.heightModifier, 2)));
            }
            if(direction == Direction.NORTH) {
                if ((be.crank.angle > 90&&be.crank.angle < 270)||(be.crank.angle < -90&&be.crank.angle > -270)) {
                    x = (float) Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));
                  } else
                      x = (float) -Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));


                y = (float) (be.connectorDistance - Math.sqrt(Math.pow(be.connectorDistance, 2) - Math.pow(be.crank.heightModifier, 2)));
            }
            if(direction == Direction.SOUTH) {
                if ((be.crank.angle > 90&&be.crank.angle < 270)||(be.crank.angle < -90&&be.crank.angle > -270)) {
                    x = (float) Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));
               } else
                   x = (float) -Math.sqrt(Math.pow(be.crank.crankRadius, 2) - Math.pow(be.crank.heightModifier, 2));


                y = (float) (be.connectorDistance - Math.sqrt(Math.pow(be.connectorDistance, 2) - Math.pow(be.crank.heightModifier, 2)));
            }



                vec3 = vec3.add(0,linkLenght,0);
        }


            x = x * q;

            y = y * q;




        if(direction==Direction.NORTH) {
            pMatrixStack.translate(0, hModifier +1.5, (be.connectorDistance + (.5*q) + x)*q);
            x = x * q;
            vec3 =  vec3.add(0,0,-x+y);

            if(second) {
                pMatrixStack.translate(-1,0,0);
            }
            pMatrixStack.translate(1,0,0);
        }

        if(direction==Direction.SOUTH){
            pMatrixStack.translate(0, hModifier+1.5, (-be.connectorDistance+(.5*q)+x)*q);
            x = x * q;
            vec3 = vec3.add(0,0,-x-y);

            if(second) {
                pMatrixStack.translate(1,0,0);
            }
           // pMatrixStack.translate(1,0,0);
        }

        if(direction==Direction.WEST){
            pMatrixStack.translate((be.connectorDistance+(.5*q)+x)*q, hModifier+1.5, 0);
            x = x * q;
            vec3 = vec3.add(-x-y,0,0);
            if(second) {
                pMatrixStack.translate(0,0,1);
            }

        }
        if(direction==Direction.EAST){
            pMatrixStack.translate((-be.connectorDistance+(.5*q)+x)*q, hModifier+1.5, 0);
            x = x * q;
            vec3 = vec3.add(-x+y,0,0);

            if(second) {
                pMatrixStack.translate(0,0,-1);
            }

            pMatrixStack.translate(0,0,1);

        }



        float f = (float)(vec3.x);
        float f1 = (float)(vec3.y );
        float f2 = (float)(vec3.z);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;


        int i =15;
        int j = 15;

        //int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        //int j = this.entityRenderDispatcher.getRenderer(pLeashHolder).getBlockLightLevel(pLeashHolder, blockpos1);
        //int k = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos);
        //int l = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos1);


        int k = 15;
        int l = 15;


        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        pMatrixStack.popPose();
    }
/////////////////////////////////////////////////////////////////

    private void renderFrontPumpjackLink(PoseStack pMatrixStack, MultiBufferSource pBuffer, PumpjackBlockEntity be) {
        pMatrixStack.pushPose();
        Direction direction = be.getBlockState().getValue(FACING);



        Vec3 vec3 = new Vec3(0,0,0);



        int q = -1;

        int g = 0;



        float hModifier= 0;

        if(be.headAtFront) {
            q = 1;
        }else g = 1;



        float y=0;
        if(be.crank!=null) {

            float linkLenght =    be.headBaseDistance;


            hModifier = (float) (be.headDistance*Math.sin(Math.toRadians(be.angle)));



           // if(direction == Direction.WEST) {
//
           //     y = (float) (be.headDistance);
           // }
           // if(direction == Direction.EAST) {
//
//
           //     y = (float) (be.headDistance );
           // }
           // if(direction == Direction.NORTH) {
//
           //     y = (float) (be.headDistance);
           // }
           // if(direction == Direction.SOUTH) {
//
           //     y = (float) (be.headDistance);
           // }

            y = -0.01f;

            vec3 = vec3.add(0,linkLenght,0);
        }




        hModifier = hModifier*q;


        if(direction==Direction.NORTH) {
            pMatrixStack.translate(0.5, -be.headBaseDistance+2, (-be.headDistance*q)+(.5*q)+g);
            vec3 =  vec3.add(0,hModifier-0.3,+y);



        }

        if(direction==Direction.SOUTH){
            pMatrixStack.translate(0.5, -be.headBaseDistance+2, (be.headDistance*q)+(.5*q)+g);
            vec3 = vec3.add(0,-hModifier-0.3,-y);


        }

        if(direction==Direction.WEST){
            pMatrixStack.translate((-be.headDistance*q)+(.5*q)+g, -be.headBaseDistance+2, 0.5);
            vec3 = vec3.add(-y,-hModifier-0.3,0);


        }
        if(direction==Direction.EAST){
            pMatrixStack.translate((be.headDistance*q)+(.5*q)+g, -be.headBaseDistance+2, 0.5);
            vec3 = vec3.add(+y,hModifier-0.3,0);




        }



        float f = (float)(vec3.x);
        float f1 = (float)(vec3.y );
        float f2 = (float)(vec3.z);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;


        int i =15;
        int j = 15;

        //int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        //int j = this.entityRenderDispatcher.getRenderer(pLeashHolder).getBlockLightLevel(pLeashHolder, blockpos1);
        //int k = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos);
        //int l = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos1);


        int k = 15;
        int l = 15;


        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        pMatrixStack.popPose();
    }

    private static void addVertexPair(VertexConsumer vertexConsumer, Matrix4f p_174309_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = p_174321_ % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        float f2 = 0.1F * f1;
        float f3 = 0.1F * f1;
        float f4 = 0.1F * f1;
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        vertexConsumer.vertex(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        vertexConsumer.vertex(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }



    @Override
    protected BlockState getRenderedBlockState(KineticBlockEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}